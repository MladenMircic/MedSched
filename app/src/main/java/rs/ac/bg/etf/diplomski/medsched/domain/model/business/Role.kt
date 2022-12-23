package rs.ac.bg.etf.diplomski.medsched.domain.model.business

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import rs.ac.bg.etf.diplomski.medsched.R

data class Role(
    @StringRes val roleName: Int,
    @DrawableRes val roleImage: Int,
)

val roles = listOf(
    Role(
        roleName = R.string.doctor_role,
        roleImage = R.drawable.doctor_icon
    ),
    Role(
        roleName = R.string.patient_role,
        roleImage = R.drawable.patient_icon
    )
)