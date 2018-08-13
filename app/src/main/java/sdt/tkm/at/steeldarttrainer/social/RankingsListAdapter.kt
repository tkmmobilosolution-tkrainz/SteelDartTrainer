package sdt.tkm.at.steeldarttrainer.social

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import sdt.tkm.at.steeldarttrainer.R
import sdt.tkm.at.steeldarttrainer.base.DataHolder
import sdt.tkm.at.steeldarttrainer.base.RankingsUser
import java.math.BigDecimal

class RankingsListAdapter(val context: Context, val users: MutableList<RankingsUser>): BaseAdapter() {

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater: LayoutInflater
                = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val item = inflater.inflate(R.layout.rankings_list_item, parent, false)
        val positionView = item.findViewById<TextView>(R.id.rankingsItemPostion)
        val pointsView = item.findViewById<TextView>(R.id.rankingsItemPoints)
        val nameView = item.findViewById<TextView>(R.id.rankingsItemName)
        val user = users[position]

        positionView.text = "${position + 1}"
        pointsView.text = String.format("%.2f", user.rankingPoints)

        nameView.text = "Player"

        if (user.uid == DataHolder(context).getUUID()) {
            item.setBackgroundColor(Color.GRAY)
            nameView.text = "You"
        }

        return item
    }

    override fun getItem(position: Int): Any {
        return users[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return users.size //To change body of created functions use File | Settings | File Templates.
    }
}