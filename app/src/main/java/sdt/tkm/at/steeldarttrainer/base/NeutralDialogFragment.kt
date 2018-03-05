package sdt.tkm.at.steeldarttrainer.base

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
import sdt.tkm.at.steeldarttrainer.R.id.hintDialogTitle

/**
 * [Add class description here]
 *
 * Created 05.03.18
 *
 * @author Thomas Krainz-Mischitz (Level1 GmbH)
 * @version %I%, %G%
 */
class NeutralDialogFragment: DialogFragment() {

    var title: String? = null
        private set

    var message: String? = null
        private set

    var buttonTitleString: String? = null
        private set


    /**
     * Defines actions to happen if views are pressed.
     */
    var listener: NeutralListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //get properties from arguments Bundle which has been assigned on creation
        val arguments = arguments

        message = arguments?.getString(ARG_MESSAGE)
        title = arguments?.getString(ARG_TITLE)
        buttonTitleString = arguments?.getString(ARG_POSITIVE)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(false)
        isCancelable = false

        return dialog
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.neutral_dialog, container)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val hintDialogTitle = view.findViewById<TextView>(R.id.hintDialogTitle)
        hintDialogTitle.text = title

        val hintDialogMessage = view.findViewById<TextView>(R.id.hintDialogMessage)
        hintDialogMessage.text = message

        val hintButton = view.findViewById<Button>(R.id.hintDialogButton)
        hintButton.text = buttonTitleString
        hintButton.setOnClickListener {

            val listener = listener ?: return@setOnClickListener
            listener.buttonClicked()
        }
    }

    interface NeutralListener {
        fun buttonClicked()
    }
    companion object {
        private val ARG_TITLE = "title"
        private val ARG_MESSAGE = "message"
        private val ARG_POSITIVE = "positive"

        fun newNeutralDialog(title: String, message: String,
                             buttonText: String): NeutralDialogFragment {

            val neutralDialog = NeutralDialogFragment()

            val arguments = Bundle()
            arguments.putString(ARG_MESSAGE, message)
            arguments.putString(ARG_TITLE, title)
            arguments.putString(ARG_POSITIVE, buttonText)

            neutralDialog.arguments = arguments

            return neutralDialog
        }
    }
}