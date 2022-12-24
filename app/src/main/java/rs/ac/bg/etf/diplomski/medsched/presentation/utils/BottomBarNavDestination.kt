package rs.ac.bg.etf.diplomski.medsched.presentation.utils

import androidx.compose.ui.graphics.vector.ImageVector

interface BottomBarNavDestination {
    val title: Int
    val route: String
    val imageUnselected: ImageVector
    val imageSelected: ImageVector
}