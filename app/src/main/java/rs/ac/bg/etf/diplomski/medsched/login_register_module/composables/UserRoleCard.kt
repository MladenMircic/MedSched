package rs.ac.bg.etf.diplomski.medsched.login_register_module.composables

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.ui.theme.Blue85
import rs.ac.bg.etf.diplomski.medsched.ui.theme.Quicksand
import rs.ac.bg.etf.diplomski.medsched.ui.theme.RoundedShape60
import rs.ac.bg.etf.diplomski.medsched.utils.ROLE_IMAGE_SIZE
import rs.ac.bg.etf.diplomski.medsched.utils.USER_CARD_SIZE

@Composable
fun UserRoleCard(
    roleName: String,
    @DrawableRes roleImage: Int,
    roleImageDescription: String,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedShape60,
        backgroundColor = Blue85,
        modifier = modifier
            .size(USER_CARD_SIZE)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = roleImage),
                contentDescription = roleImageDescription,
                modifier = Modifier
                    .size(ROLE_IMAGE_SIZE)
                    .padding(bottom = 10.dp)
            )
            Text(
                text = roleName,
                fontFamily = Quicksand,
                fontWeight = FontWeight.Bold
            )
        }
    }
}