package hku.hk.cs.a3330gp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import hku.hk.cs.a3330gp.data.Patient

class PatientsAdapter(private val context: Context, private val patientsList: List<Patient>) :
    ArrayAdapter<Patient>(context, R.layout.patient_profile_card, patientsList) {

    private class ViewHolder(row: View?) {
        val name: TextView = row?.findViewById(R.id.name) as TextView
        val sex: TextView = row?.findViewById(R.id.sex) as TextView
        val age: TextView = row?.findViewById(R.id.age) as TextView
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View?
        val vh: ViewHolder

        if (convertView == null) {
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.patient_profile_card, parent, false)
            vh = ViewHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as ViewHolder
        }

        val patient = patientsList[position]
        vh.name.text = patient.name
        vh.sex.text = patient.sex
        vh.age.text = patient.age.toString()

        return view as View
    }
}