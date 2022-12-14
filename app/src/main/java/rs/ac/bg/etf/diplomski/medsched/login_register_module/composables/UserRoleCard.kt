package rs.ac.bg.etf.diplomski.medsched.login_register_module.composables

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
import rs.ac.bg.etf.diplomski.medsched.ui.theme.*
import rs.ac.bg.etf.diplomski.medsched.utils.ROLE_IMAGE_SIZE
import rs.ac.bg.etf.diplomski.medsched.utils.USER_CARD_SIZE

@Composable
fun UserRoleCard(
    roleName: String,
    @DrawableRes roleImage: Int,
    selectedRole: String?,
    onRoleSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val cardRotation by animateIntAsState(
        targetValue = if (selectedRole == roleName) -60 else 0,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    Card(
        shape = RoundedShape60,
        backgroundColor =
            if (selectedRole == roleName)
                Blue40
            else
                Blue85,
        modifier = modifier
            .size(USER_CARD_SIZE)
            .rotate(60f + cardRotation)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onRoleSelect(roleName)
            }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .rotate(-60f - cardRotation)
        ) {
            Image(
                painter = painterResource(id = roleImage),
                contentDescription = "",
                modifier = Modifier
                    .size(ROLE_IMAGE_SIZE)
                    .padding(bottom = 10.dp),
                colorFilter =
                    if (selectedRole == roleName)
                        ColorFilter.tint(color = Blue95)
                    else
                        null
            )
            Text(
                text = roleName,
                fontFamily = Quicksand,
                fontWeight = FontWeight.Bold,
                color =
                    if (selectedRole == roleName)
                        Blue95
                    else
                        Color.Unspecified
            )
        }
    }
}