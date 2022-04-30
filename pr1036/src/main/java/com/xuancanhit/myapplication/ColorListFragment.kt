package com.xuancanhit.myapplication


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment


class ColorListFragment : Fragment() {

    private lateinit var lvColor: ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_color_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val context = context as MainActivity
        val colorNamesList = resources.getStringArray(R.array.colorNames)
        val colorValuesList = resources.getIntArray(R.array.colorValues)

        lvColor = context.findViewById(R.id.lv_color)
        val colorList = resources.getStringArray(R.array.colorNames)
        val adapter =
            object : ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, colorList) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val myView = super.getView(position, convertView, parent)
//
//                    for (color in colorValuesList) {
//                        val colorString = "#" + Integer.toHexString(color)
//                        Log.d("LOG_TAG", "Hex Color: $colorString")
//                    }



                    Log.d("LOG_TAG", "Size List Name" + colorNamesList.size.toString())
                    Log.d("LOG_TAG", "Size List Value" + colorValuesList.size.toString())

//                    for (i in colorNamesList.indices) {
//                        if (position == i) {
//                            myView.setBackgroundColor(
//                                colorValuesList[i]
//                            )
//                        }
//                    }





                    return myView
                }
            }





        lvColor.adapter = adapter
        //lvColor.setSelection(0)



//        lvColor.onItemClickListener = object : AdapterView.OnItemClickListener {
//            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//
//            }
//        }

    }

}