package sdt.tkm.at.steeldarttrainer.social

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ProgressBar
import android.widget.TextView
import sdt.tkm.at.steeldarttrainer.R

class AchivementListAdapter(val context: Context, val achivements: ArrayList<Achivement>): BaseAdapter() {

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater: LayoutInflater
                = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        var item: View
        val achivement = achivements[position]
        if (!achivement.successful) {
            item = inflater.inflate(R.layout.achivement_item, parent, false)
            val title = item.findViewById<TextView>(R.id.achivement_title)
            val description = item.findViewById<TextView>(R.id.achivement_desc)
            val amountView = item.findViewById<TextView>(R.id.success_achivement_amount)
            val progressBar = item.findViewById<ProgressBar>(R.id.achivement_progress)

            title.text = achivement.title
            description.text = achivement.description

            var progress = achivement.currentAmount
            var maxProgress = achivement.amount

            progressBar.progress = progress
            progressBar.max = maxProgress

            amountView.text = String.format("%d / %d", achivement.currentAmount, achivement.amount)
        } else {
            item = inflater.inflate(R.layout.achivement_success_item, parent, false)
            val title = item.findViewById<TextView>(R.id.success_achivement_title)
            val description = item.findViewById<TextView>(R.id.success_achivement_desc)
            val amountView = item.findViewById<TextView>(R.id.success_achivement_amount)

            title.text = achivement.title
            description.text = achivement.description

            amountView.text = String.format("%d / %d", achivement.currentAmount, achivement.amount)
        }

        return item
    }

    override fun getItem(position: Int): Any {
        return achivements[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return achivements.size //To change body of created functions use File | Settings | File Templates.
    }
}