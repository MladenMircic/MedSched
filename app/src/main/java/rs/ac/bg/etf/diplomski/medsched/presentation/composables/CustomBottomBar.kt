package rs.ac.bg.etf.diplomski.medsched.presentation.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.PatientHome
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.*
import rs.ac.bg.etf.diplomski.medsched.presentation.utils.BottomBarNavDestination

@Composable
fun CustomBottomBar(
    destinations: List<BottomBarNavDestination>,
    navController: NavHostController
) {

    val backStackEntry by navController.currentBackStackEntryAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.1f)
            .clip(RoundedShape40.copy(bottomStart = ZeroCornerSize, bottomEnd = ZeroCornerSize))
            .background(MaterialTheme.colors.secondary),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        destinations.forEach { destination ->
            val currentDestination = backStackEntry?.destination
            val currentRoute = currentDestination?.route ?: PatientHome.route

            val selected = currentRoute.startsWith(destination.route)
            val backgroundColor =
                if (selected) Blue85 else Color.Transparent
            val contentColor =
                if (selected) MaterialTheme.colors.selectableBottomNavbar else BackgroundPrimaryLight

            Box(
                modifier = Modifier
                    .height(50.dp)
                    .clip(RoundedShape20)
                    .background(backgroundColor)
                    .clickable {
                        navController.navigate(destination.route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(PatientHome.route) {
                                saveState = true
                            }
                        }
                    }
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(10.dp)
                ) {
                    Icon(
                        imageVector =
                        if (selected) destination.imageSelected else destination.imageUnselected,
                        contentDescription = "Navbar Icon",
                        tint = contentColor
                    )
                    AnimatedVisibility(visible = selected) {
                        Text(
                            text = stringResource(id = destination.title),
                            fontFamily = Quicksand,
                            color = contentColor
                        )
                    }
                }
            }
        }
    }
}