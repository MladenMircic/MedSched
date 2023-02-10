package rs.ac.bg.etf.diplomski.medsched.presentation.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.Quicksand
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.RoundedShape20
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.textOnSecondary

@Composable
fun AccountInfoPartCard(
    icon: ImageVector,
    headerText: String,
    contentText: String
) {
    Card(
        backgroundColor = MaterialTheme.colors.secondary,
        shape = RoundedShape20,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colors.textOnSecondary,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Column {
                Text(
                    text = headerText,
                    fontFamily = Quicksand,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colors.textOnSecondary
                )
                Text(
                    text = contentText,
                    fontFamily = Quicksand,
                    fontSize = 16.sp,
                    color = MaterialTheme.colors.textOnSecondary
                )
            }
        }
    }
}