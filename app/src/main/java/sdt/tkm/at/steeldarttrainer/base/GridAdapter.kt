package sdt.tkm.at.steeldarttrainer.base

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import sdt.tkm.at.steeldarttrainer.R

/**
 * [Add class description here]
 *
 * Created 22.01.18
 *
 * @author Thomas Krainz-Mischitz (Level1 GmbH)
 * @version %I%, %G%
 */
class GridAdapter(val context: Context) : BaseAdapter() {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.grid_item, null)
        val textView = view.findViewById<TextView>(R.id.gridItemText)

        val pos = position + 1
        var text: String = pos.toString()
        when (position) {
            20 ->
                text = "Bull"
        }

        textView.text = text

        view.setBackgroundColor(Color.WHITE)
        return view
    }

    override fun getItem(position: Int): Any {
        return position + 1
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return 21
    }
}