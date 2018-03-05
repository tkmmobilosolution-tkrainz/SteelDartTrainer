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
import com.shawnlin.numberpicker.NumberPicker
import sdt.tkm.at.steeldarttrainer.R

/**
 * [Add class description here]
 *
 * Created 05.03.18
 *
 * @author Thomas Krainz-Mischitz (Level1 GmbH)
 * @version %I%, %G%
 */
class PickerDialogFragment : DialogFragment() {

    var title: String? = null
        private set

    var buttonTitleString: String? = null
        private set

    var stringArrays: Array<String>? = null
        private set

    var listener: PickerDialogListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val arguments = arguments

        title = arguments?.getString(ARG_TITLE)
        buttonTitleString = arguments?.getString(ARG_POSITIVE)
        stringArrays = arguments?.getStringArray(ARG_ITEMS)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(false)
        isCancelable = false

        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.chooser_dialog, container)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var pickerValue = 1
        val targetTitle = view.findViewById<TextView>(R.id.xxChooserTitle)
        targetTitle.text = title

        val targetPicker = view.findViewById<NumberPicker>(R.id.xxChooserPicker)
        targetPicker.minValue = 0
        targetPicker.maxValue = stringArrays!!.size - 1
        targetPicker.displayedValues = stringArrays

        targetPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            pickerValue = newVal
        }

        val chooserButton = view.findViewById<Button>(R.id.xxChooserButton)
        chooserButton.text = this.getString(R.string.xoi_chooser_button_title)

        chooserButton.setOnClickListener {
            val listener = listener ?: return@setOnClickListener
            listener.buttonClicked(pickerValue)
        }
    }

    interface PickerDialogListener {
        fun buttonClicked(value: Int)
    }

    companion object {
        private val ARG_TITLE = "title"
        private val ARG_POSITIVE = "positive"
        private val ARG_ITEMS = "items"

        fun newPickerDialog(title: String, items: Array<String>,
                             buttonText: String): PickerDialogFragment {

            val pickerDialog = PickerDialogFragment()

            val arguments = Bundle()
            arguments.putString(ARG_TITLE, title)
            arguments.putString(ARG_POSITIVE, buttonText)
            arguments.putStringArray(ARG_ITEMS, items)

            pickerDialog.arguments = arguments

            return pickerDialog
        }
    }

}