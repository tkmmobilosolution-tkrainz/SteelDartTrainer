package sdt.tkm.at.steeldarttrainer.dialog

import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import sdt.tkm.at.steeldarttrainer.R
import sdt.tkm.at.steeldarttrainer.base.LogEventsHelper

/**
 * [Add class description here]
 *
 * Created 05.03.18
 *
 * @author Thomas Krainz-Mischitz (Level1 GmbH)
 * @version %I%, %G%
 */
class ChooserDialogFragment : DialogFragment() {

    var title: String? = null
        private set

    var message: String? = null
        private set

    var postiveButtonTitle: String? = null
        private set

    var negativeButtonTitle: String? = null
        private set

    var listener: ChooserDialogListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val arguments = arguments

        title = arguments?.getString(ARG_TITLE)
        message = arguments?.getString(ARG_MESSAGE)
        postiveButtonTitle = arguments?.getString(ARG_POSITIVE)
        negativeButtonTitle = arguments?.getString(ARG_NEGATIVE)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(false)
        isCancelable = false

        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.multiple_button_dialog, container)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dialogText = view.findViewById<TextView>(R.id.dialogText)
        dialogText.text = message
        val exitButton = view.findViewById<Button>(R.id.newGameButton)
        exitButton.text = negativeButtonTitle
        val closeButton = view.findViewById<Button>(R.id.closeButton)
        closeButton.text = postiveButtonTitle


        exitButton.setOnClickListener {
            listener!!.negativeButtonClicked()
        }

        closeButton.setOnClickListener {
            listener!!.positivButtonClicked()
        }
    }

    interface ChooserDialogListener {
        fun positivButtonClicked()
        fun negativeButtonClicked()
    }

    companion object {
        private val ARG_TITLE = "title"
        private val ARG_MESSAGE = "mesage"
        private val ARG_POSITIVE = "positive"
        private val ARG_NEGATIVE = "items"

        fun newChooserDialog(title: String, message: String,
                             posText: String, negText: String): ChooserDialogFragment {

            val chooserDialog = ChooserDialogFragment()

            val arguments = Bundle()
            arguments.putString(ARG_TITLE, title)
            arguments.putString(ARG_MESSAGE, message)
            arguments.putString(ARG_POSITIVE, posText)
            arguments.putString(ARG_NEGATIVE, negText)

            chooserDialog.arguments = arguments

            return chooserDialog
        }
    }
}