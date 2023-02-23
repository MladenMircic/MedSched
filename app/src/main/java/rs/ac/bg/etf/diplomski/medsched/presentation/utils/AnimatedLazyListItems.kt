package rs.ac.bg.etf.diplomski.medsched.presentation.utils

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.delay

data class VisibleItem<T>(
    val visible: Boolean,
    val data: T,
    val state: State,
) {
    enum class State {
        ADDING,
        REMOVING,
        ADDED,
        REMOVED
    }
}

class VisibilityList<T>(core: SnapshotStateList<T>, initialAnimated: Boolean = true) {
    private var _originalSize by mutableStateOf(0)
    private var showDelay by mutableStateOf(0L)
    private val visibilityList = mutableStateListOf<VisibleItem<T>>()
        .apply {
            val state = if(initialAnimated)
                VisibleItem.State.ADDING
            else
                VisibleItem.State.ADDED
            val visible = !initialAnimated
            addAll(
                core.map {
                    VisibleItem(
                        visible = visible,
                        data = it,
                        state = state
                    )
                }
            )
            _originalSize = core.size
        }
    val actualSize
        get() = visibilityList.size
    val size
        get() = _originalSize
    val list: SnapshotStateList<VisibleItem<T>>
        get() = visibilityList

    fun getDelayAndIncrement(): Long {
        val delay = showDelay
        showDelay += 200L
        return delay
    }

    fun add(item: T) {
        visibilityList.add(
            VisibleItem(
                visible = false,
                data = item,
                state = VisibleItem.State.ADDING
            )
        )
        ++_originalSize
    }

    fun addAll(items: Iterable<T>, initialAnimated: Boolean = false) {
        val state = if (initialAnimated)
            VisibleItem.State.ADDING
        else
            VisibleItem.State.ADDED
        val visible = !initialAnimated
        visibilityList.addAll(items.map {
            VisibleItem(
                visible = visible,
                data = it,
                state = state
            )
        })
        _originalSize += items.count()
    }

    operator fun set(index: Int, item: T) {
        val size = visibilityList.size
        if(index in 0 until size) {
            val visibleItem = visibilityList[index]
            visibilityList[index] = visibleItem.copy(data = item)
        }
    }

    fun add(index: Int, item: T) {
        visibilityList.add(index,VisibleItem(
            visible = false,
            data = item,
            state = VisibleItem.State.ADDING
        ))
        ++_originalSize
    }

    fun remove(item: T) {
        val index = visibilityList.indexOfFirst {
            it.data == item
        }
        if(index > -1){
            visibilityList[index] = VisibleItem(
                visible = false,
                data = item,
                state = VisibleItem.State.REMOVING
            )
            --_originalSize
        }
    }

    fun remove(block: (T)->Boolean) {
        val index = visibilityList.indexOfFirst {
            block(it.data)
        }
        if (index > -1) {
            val item = visibilityList[index]
            visibilityList[index] = VisibleItem(
                visible = false,
                data = item.data,
                state = VisibleItem.State.REMOVING
            )
            --_originalSize
        }
    }

    fun makeVisible(item: VisibleItem<T>) {
        val index = visibilityList.indexOf(item)
        if (index > -1) {
            visibilityList[index] = item.copy(
                visible = true,
                data = item.data,
                state = VisibleItem.State.ADDED
            )
        }
    }

    fun makeInvisible(item: VisibleItem<T>) {
        val index = visibilityList.indexOf(item)
        if (index > -1) {
            visibilityList[index] = item.copy(
                visible = false,
                data = item.data,
                state = VisibleItem.State.REMOVED
            )
        }
    }

    fun delete(item: VisibleItem<T>) {
        val index = visibilityList.indexOfFirst {
            it.data == item.data
        }
        visibilityList.removeAt(index)
    }

    fun clear(animated: Boolean = false){
        if(!animated) {
            visibilityList.clear()
        }
        else {
            val list = visibilityList.map {
                VisibleItem(
                    visible = false,
                    data = it.data,
                    state = VisibleItem.State.REMOVING
                )
            }
            visibilityList.clear()
            visibilityList.addAll(list)
        }
        _originalSize = 0
    }

    fun indexOf(item: T): Int {
        return visibilityList.indexOfFirst {
            it.data==item
        }
    }

    fun shuffle() {
        visibilityList.shuffle()
    }

    fun isNotEmpty(): Boolean {
        return _originalSize != 0
    }
}

val <T> SnapshotStateList<T>.animated: VisibilityList<T>
    get() {
        return VisibilityList(this)
    }

@OptIn(ExperimentalFoundationApi::class)
inline fun <T> LazyListScope.animatedItems(
    items: VisibilityList<T>,
    noinline key: ((item: T) -> Any)? = null,
    enter: EnterTransition = EnterTransition.None,
    exit: ExitTransition = ExitTransition.None,
    exitDuration: Long = 0,
    animateItemPlacementSpec: FiniteAnimationSpec<IntOffset>? = spring(
        stiffness = Spring.StiffnessMediumLow,
        visibilityThreshold = IntOffset.VisibilityThreshold
    ),
    crossinline itemContent: @Composable LazyItemScope.(item: T) -> Unit
){
    items(
        items.list,
        key = if(key==null)
            null
        else { item: VisibleItem<T> ->
            key(item.data)
        }
    ) {
        LaunchedEffect(key1 = it.visible){
            if (!it.visible && it.state==VisibleItem.State.ADDING) {
                items.makeVisible(it)
                return@LaunchedEffect
            }
            if (!it.visible && it.state==VisibleItem.State.REMOVING) {
                if (exitDuration > 0) {
                    items.makeInvisible(it)
                    delay(exitDuration)
                }
                items.delete(it)
            }
        }
        AnimatedVisibility(
            visible = it.visible,
            enter = enter,
            exit = exit,
            modifier = Modifier.then(
                if (animateItemPlacementSpec != null)
                    Modifier.animateItemPlacement(animateItemPlacementSpec)
                else Modifier
            )
        ) {
            itemContent(it.data)
        }
    }
}