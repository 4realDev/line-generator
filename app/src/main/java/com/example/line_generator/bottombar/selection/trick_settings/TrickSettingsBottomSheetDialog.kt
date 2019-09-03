package com.example.line_generator.bottombar.selection.trick_settings

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import co.ceryle.segmentedbutton.SegmentedButtonGroup
import com.example.line_generator.R
import com.example.line_generator.data.trick.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.layout_bottomsheet_delete.*


class TrickSettingsBottomSheetDialog : BottomSheetDialogFragment() {

    private lateinit var trickName: TextInputEditText

    private lateinit var cancelBtn: ImageView
    private lateinit var deleteBtn: ImageView
    private lateinit var saveBtn: ImageView

    private lateinit var difficultyGroup: SegmentedButtonGroup
    private lateinit var difficulty: Difficulty

    private lateinit var directionInGroup: SegmentedButtonGroup
    private lateinit var directionIn: DirectionIn

    private lateinit var directionOutGroup: SegmentedButtonGroup
    private lateinit var directionOut: DirectionOut

    private lateinit var longPressedTrick: TrickViewState

    private var name: String? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        check(parentFragment is ClickListener) { "Parent fragment must implement `SwitchOffBottomSheetDialog.ClickListener`." }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_bottomsheet_delete, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        trickName = view.findViewById(R.id.bottomsheet_trick_edit__trick_name_input)
        cancelBtn = view.findViewById(R.id.bottomsheet_trick_edit_cancel)
        deleteBtn = view.findViewById(R.id.bottomsheet_trick_edit_delete)
        saveBtn = view.findViewById(R.id.bottomsheet_trick_edit_save)
        difficultyGroup = view.findViewById(R.id.bottomsheet_trick_edit__difficulty_group)
        directionInGroup = view.findViewById(R.id.bottomsheet_trick_edit__direction_in_group)
        directionOutGroup = view.findViewById(R.id.bottomsheet_trick_edit__direction_out_group)



        val bundle = arguments
        longPressedTrick = bundle?.getSerializable("longPressedTrick") as TrickViewState
        setItemAttributes(longPressedTrick)

        trickName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(trickName: Editable?) {
                bottomsheet_trick_edit__text_input_layout.isHintEnabled = true
                bottomsheet_trick_edit__text_input_layout.isErrorEnabled = true
                name = trickName.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        directionInGroup.setOnClickedButtonListener(object: SegmentedButtonGroup.OnClickedButtonListener {
            override fun onClickedButton(position: Int) {
                directionInGroup.setPosition(position, false)
                when(position){
                    0 -> directionIn = DirectionIn.REGULAR
                    1 -> directionIn = DirectionIn.FAKIE
                }
            }
        })

        directionOutGroup.setOnClickedButtonListener(object: SegmentedButtonGroup.OnClickedButtonListener {
            override fun onClickedButton(position: Int) {
                directionOutGroup.setPosition(position, false)
                when(position){
                    0 -> directionOut = DirectionOut.TO_REGULAR
                    1 -> directionOut = DirectionOut.TO_FAKIE
                }
            }
        })

        difficultyGroup.setOnClickedButtonListener(object: SegmentedButtonGroup.OnClickedButtonListener {
            override fun onClickedButton(position: Int) {
                difficultyGroup.setPosition(position, false)
                when(position){
                    0 -> difficulty = Difficulty.SAVE
                    1 -> difficulty = Difficulty.EASY
                    2 -> difficulty = Difficulty.MIDDLE
                    3 -> difficulty = Difficulty.HARD
                    4 -> difficulty = Difficulty.CRAZY
                }
            }
        })

        cancelBtn.setOnClickListener {
            dismiss()
        }

        deleteBtn.setOnClickListener {
            (parentFragment as ClickListener).onDeleteClicked(longPressedTrick)
            dismiss()
        }

        saveBtn.setOnClickListener {
            validateInput(longPressedTrick)
        }
    }

    private fun setItemAttributes(longPressedTrick: TrickViewState) {
        directionIn = longPressedTrick.directionIn
        directionOut = longPressedTrick.directionOut
        difficulty = longPressedTrick.difficulty
        setTrickName(longPressedTrick)
        setDifficulty(longPressedTrick)
        setDirectionIn(longPressedTrick)
        setDirectionOut(longPressedTrick)
    }

    private fun setTrickName(longPressedTrick: TrickViewState){
        if (longPressedTrick.userCreatedName != null) {
            name = longPressedTrick.userCreatedName
            trickName.setText(name)
        } else if (longPressedTrick.name != null){
            name = getString(TrickTypeHelper.getString(longPressedTrick.trickType)!!)
            trickName.setText(name)
        }
    }

    private fun setDifficulty(longPressedTrick: TrickViewState){
        when (longPressedTrick.difficulty) {
            Difficulty.SAVE -> difficultyGroup.position = 0
            Difficulty.EASY -> difficultyGroup.position = 1
            Difficulty.MIDDLE -> difficultyGroup.position = 2
            Difficulty.HARD -> difficultyGroup.position = 3
            Difficulty.CRAZY -> difficultyGroup.position = 4
        }
    }

    private fun setDirectionIn(longPressedTrick: TrickViewState){
        when (longPressedTrick.directionIn) {
            DirectionIn.REGULAR -> directionInGroup.position = 0
            DirectionIn.FAKIE -> directionInGroup.position = 1
        }
    }

    private fun setDirectionOut(longPressedTrick: TrickViewState){
        when (longPressedTrick.directionOut) {
            DirectionOut.TO_REGULAR -> directionOutGroup.position = 0
            DirectionOut.TO_FAKIE -> directionOutGroup.position = 1
        }
    }

    private fun validateInput(longPressedTrick: TrickViewState): Boolean {
        return if (name.isNullOrEmpty()) {
            trickName.error = "Field can't be empty"
            false
        } else {
            trickName.error = null
            (parentFragment as ClickListener).onSaveClicked(longPressedTrick.id, name!!, difficulty, directionIn, directionOut)
            dismiss()
            true
        }
    }

    interface ClickListener {
        fun onDeleteClicked(item: TrickViewState)
        fun onSaveClicked(
            id: String,
            name: String,
            difficulty: Difficulty,
            directionIn: DirectionIn,
            directionOut: DirectionOut
        )
    }

    companion object {
        fun newInstance(): TrickSettingsBottomSheetDialog = TrickSettingsBottomSheetDialog()
    }

}