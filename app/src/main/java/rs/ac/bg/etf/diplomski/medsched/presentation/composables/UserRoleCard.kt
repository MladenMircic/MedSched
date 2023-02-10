package rs.ac.bg.etf.diplomski.medsched.presentation.composables

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import rs.ac.bg.etf.diplomski.medsched.commons.CARD_IMAGE_SIZE
import rs.ac.bg.etf.diplomski.medsched.commons.USER_CARD_SIZE
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Role
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.Blue85
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.RoundedShape60
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.selectable
import java.util.*

@Composable
fun UserRoleCard(
    role: Role,
    @DrawableRes roleImage: Int,
    selectedRole: Role?,
    onRoleSelect: (Role) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val cardRotation by animateIntAsState(
        targetValue = if (selectedRole == role) -45 else 0,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    Card(
        shape = RoundedShape60,
        backgroundColor =
            if (selectedRole == role)
                MaterialTheme.colors.selectable
            else
                Blue85,
        modifier = modifier
            .size(USER_CARD_SIZE)
            .rotate(45f + cardRotation)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onRoleSelect(role)
            }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .rotate(-45f - cardRotation)
        ) {
            Image(
                painter = painterResource(id = roleImage),
                contentDescription = "",
                modifier = Modifier
                    .size(CARD_IMAGE_SIZE)
                    .padding(bottom = 10.dp),
                colorFilter =
                    if (selectedRole == role)
                        ColorFilter.tint(color = Color.White)
                    else
                        ColorFilter.tint(color = MaterialTheme.colors.primary)
            )
            Text(
                text = role.name
                    .lowercase()
                    .replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                    },
                fontWeight = FontWeight.Bold,
                color =
                    if (selectedRole == role)
                        Color.White
                    else
                        MaterialTheme.colors.primary
            )
        }
    }
}