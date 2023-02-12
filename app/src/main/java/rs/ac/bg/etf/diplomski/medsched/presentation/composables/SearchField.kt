package rs.ac.bg.etf.diplomski.medsched.presentation.composables

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.Quicksand
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.RoundedShape20
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.textFieldOutline

@Composable
fun SearchField(
    modifier: Modifier = Modifier,
    searchKeyWord: String,
    onKeyWordChange: (String) -> Unit,
    @StringRes label: Int,
    onSearchSubmit: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.CenterEnd
        ) {
            OutlinedTextField(
                value = searchKeyWord,
                onValueChange = onKeyWordChange,
                shape = RoundedShape20,
                placeholder = {
                    Text(
                        text = stringResource(id = label),
                        fontFamily = Quicksand
                    )
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = { onSearchSubmit() }
                ),
                colors = defaultButtonColors(),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(60.dp)
            )
            Button(
                onClick = onSearchSubmit,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.textFieldOutline
                ),
                shape = RoundedShape20,
                modifier = Modifier
                    .height(60.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search button"
                )
            }
        }
    }
}