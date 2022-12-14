package rs.ac.bg.etf.diplomski.medsched.login_register_module.models

import androidx.annotation.DrawableRes
import rs.ac.bg.etf.diplomski.medsched.R

data class RoleModel(
    val roleName: Int,
    @DrawableRes val roleImage: Int,
)

val roles = listOf(
    RoleModel(
        roleName = R.string.doctor_role,
        roleImage = R.drawable.doctor_icon
    ),
    RoleModel(
        roleName = R.string.patient_role,
        roleImage = R.drawable.patient_icon
    )
)