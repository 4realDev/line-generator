package com.example.line_generator.data.trick

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.line_generator.R
import com.example.line_generator.data.trick.TrickType.*

object TrickTypeHelper {

    // How to implement missing enums - comment out else case - "add remaining branches"
    @DrawableRes
    fun getDrawable(trickType: TrickType): Int {
        return when (trickType) {
            TAIL_STALL -> R.drawable.ic_letter_s
            NOSE_STALL -> R.drawable.ic_letter_s
            ROCK_TO_FAKIE -> R.drawable.ic_letter_s
            ROCK_N_ROLL -> R.drawable.ic_letter_s
            HALFCAB_ROCK -> R.drawable.ic_letter_s
            FULLCAB_ROCK -> R.drawable.ic_letter_s

            AXLE_STALL -> R.drawable.ic_letter_g
            PIVOT -> R.drawable.ic_letter_g
            FIVE_O_GRIND -> R.drawable.ic_letter_g
            FEEBLE_GRIND -> R.drawable.ic_letter_g
            SMITH_GRIND -> R.drawable.ic_letter_g
            CROOKED_GRIND -> R.drawable.ic_letter_g
            NOSE_GRIND -> R.drawable.ic_letter_g

            BONELESS -> R.drawable.ic_letter_o
            NO_COMPLY -> R.drawable.ic_letter_o
            DISTASTER -> R.drawable.ic_letter_o
            OLLIE -> R.drawable.ic_letter_o

            USER_CREATED_SLIDE -> R.drawable.ic_letter_s
            USER_CREATED_GRINDE -> R.drawable.ic_letter_g
            USER_CREATED_OTHER -> R.drawable.ic_letter_o
            UNDEFINED -> throw IllegalStateException("\"Could not find Drawable for trickType \" + trickType")
        }
    }

    @StringRes
    fun getString(trickType: TrickType): Int? {
        return when (trickType) {
            TAIL_STALL -> R.string.trick_tail_stall
            NOSE_STALL -> R.string.trick_nose_stall
            ROCK_TO_FAKIE -> R.string.trick_rock_to_fakie
            ROCK_N_ROLL -> R.string.trick_rock_n_roll
            HALFCAB_ROCK -> R.string.trick_half_cab_rock
            FULLCAB_ROCK -> R.string.trick_full_cab_rock

            AXLE_STALL -> R.string.trick_axle_stall
            PIVOT -> R.string.trick_pivot
            FIVE_O_GRIND -> R.string.trick_five_o
            FEEBLE_GRIND -> R.string.trick_feeble
            SMITH_GRIND -> R.string.trick_smith
            CROOKED_GRIND -> R.string.trick_crooked
            NOSE_GRIND -> R.string.trick_nose_grind

            BONELESS -> R.string.trick_boneless
            NO_COMPLY -> R.string.trick_no_comply
            DISTASTER -> R.string.trick_distaster
            OLLIE -> R.string.trick_ollie_air

            USER_CREATED_GRINDE -> null
            USER_CREATED_SLIDE -> null
            USER_CREATED_OTHER -> null
            UNDEFINED -> throw IllegalStateException("Could not find String for trickType $trickType")
        }
    }
}
