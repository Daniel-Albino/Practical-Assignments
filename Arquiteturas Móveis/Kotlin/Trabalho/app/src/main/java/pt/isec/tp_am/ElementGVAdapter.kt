package pt.isec.tp_am

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class ElementGVAdapter(private val elementList: List<ElementGRVModel>,
                       private val context: Context
) : BaseAdapter() {

    private var layoutInflater: LayoutInflater? = null
    private lateinit var elementTV: TextView

    override fun getCount(): Int {
        return elementList.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var convertView = convertView

        if (layoutInflater == null) {
            layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }

        if (convertView == null) {

            convertView = layoutInflater!!.inflate(R.layout.gridview_item, null)
        }

        elementTV = convertView!!.findViewById(R.id.idTVElement)


        val type = position % 2
        if(elementList.get(position).getElementGRV().equals(" "))
            convertView.setBackgroundColor(Color.parseColor("#00FFFFFF"))
        else {
            when (type) {
                0 -> convertView.setBackgroundColor(Color.parseColor("#FDDBBE"))
                else -> convertView.setBackgroundColor(Color.parseColor("#D4DCE5"))
            }
        }
        elementTV.text = elementList.get(position).getElementGRV()
        // at last we are returning our convert view.
        return convertView
    }

}