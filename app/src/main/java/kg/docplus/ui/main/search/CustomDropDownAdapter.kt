package kg.docplus.ui.main.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import kg.docplus.R
import java.time.DayOfWeek

class CustomDropDownAdapter(val context: Context, var listItemsTxt: ArrayList<String>, var dayOfWeek: ArrayList<String>) : BaseAdapter() {


    val mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val vh: ItemRowHolder
        if (convertView == null) {
            view = mInflater.inflate(R.layout.spinner_date, parent, false)
            vh = ItemRowHolder(view)
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemRowHolder
        }

    // setting adapter item height programatically.
     
        val params = view.layoutParams
        params.height = 60
        view.layoutParams = params

        vh.date.text = listItemsTxt[position]

        vh.day.text = dayOfWeek[position]
        return view
    }

    override fun getItem(position: Int): Any? {

        return null

    }

    override fun getItemId(position: Int): Long {

        return 0

    }

    override fun getCount(): Int {
        return listItemsTxt.size
    }

    private class ItemRowHolder(row: View?) {

        var date: TextView = row?.findViewById(R.id.date)!!
        var day: TextView = row!!.findViewById(R.id.day_of_week)

    }
}