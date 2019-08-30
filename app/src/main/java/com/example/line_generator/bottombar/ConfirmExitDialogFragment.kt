package com.example.line_generator.bottombar

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.example.line_generator.R

class ConfirmExitDialogFragment : DialogFragment() {

    companion object {
        fun newInstance() = ConfirmExitDialogFragment()
    }

    private var exitDialogClickListener: ExitDialogClickListener? = null
    private lateinit var dialogFragmentCancelBtn: Button
    private lateinit var dialogFragmentExitBtn: Button

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val parentFragment = parentFragment
        try {
            exitDialogClickListener = if (parentFragment != null) parentFragment as ExitDialogClickListener
            else context as ExitDialogClickListener
        } catch (e: ClassCastException) {
            val hostClass = parentFragment?.javaClass ?: context.javaClass
            throw ClassCastException(String.format("Host %s does not implement %s", hostClass.simpleName, ExitDialogClickListener::class.java.simpleName))
        }
    }

    override fun onDetach() {
        exitDialogClickListener = null
        super.onDetach()
    }

//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        return AlertDialog.Builder(requireActivity())
//            .setTitle(R.string.alert_confirm_exit_title)
//            .setMessage(R.string.alert_confirm_exit_message)
//            .setPositiveButton(R.string.action_exit) { dialog, _ ->
//                exitDialogClickListener?.onExitClicked()
//                dialog.dismiss()
//            }
//            .setNegativeButton(R.string.action_cancel) { dialog, _ ->
//                dialog.dismiss()
//            }
//            .create()
//    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = requireActivity().layoutInflater.inflate(R.layout.activity_bottom_exit_dialog, null)
        dialogFragmentCancelBtn = view.findViewById(R.id.activity_exit_dialog__cancle)
        dialogFragmentExitBtn = view.findViewById(R.id.activity_exit_dialog__exit)
        loadAnimation()
        dialogFragmentCancelBtn.setOnClickListener { dismiss() }
        dialogFragmentExitBtn.setOnClickListener {
            exitDialogClickListener?.onExitClicked()
            dismiss()
        }
        val dialog = AlertDialog.Builder(requireActivity())
            .setView(view)
            .create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    private fun loadAnimation() {
        val animSlideFromLeft = AnimationUtils.loadAnimation(requireContext(), R.anim.item_animation_slide_from_left)
        val animSlideFromRight = AnimationUtils.loadAnimation(requireContext(), R.anim.item_animation_slide_from_right)
        dialogFragmentCancelBtn.startAnimation(animSlideFromLeft)
        dialogFragmentExitBtn.startAnimation(animSlideFromRight)
    }

    interface ExitDialogClickListener {
        fun onExitClicked()
    }
}