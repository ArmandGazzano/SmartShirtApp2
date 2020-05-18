package fr.isen.levreau.smartshirtapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import fr.isen.levreau.smartshirtapp.R.layout.activity_followup_seance
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_followup_seance.view.*

class SeanceAdapter(
    val nseance: ArrayList<String>,
    val hautDos: ArrayList<String>,
    val basDos: ArrayList<String>,
    val centreDos: ArrayList<String>,
    val epG: ArrayList<String>,
    val epD: ArrayList<String>
): RecyclerView.Adapter<SeanceAdapter.ViewHolder>() {
    var maxDanger: String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(activity_followup_seance, parent, false))


    override fun getItemCount() = nseance.size

    override fun onBindViewHolder(holder: SeanceAdapter.ViewHolder, position: Int) {
        holder.nbSeance.text = nseance[position]
        holder.fleche.setOnClickListener {
            if (holder.expendableLayout.visibility == View.GONE)
                holder.expendableLayout.visibility=View.VISIBLE
            else holder.expendableLayout.visibility=View.GONE

            compareDonnees(position)

            holder.tendance.text = maxDanger
            holder.nbHdos.text = hautDos[position]
            holder.nbBdos.text = basDos[position]
            holder.nbCdos.text = centreDos[position]
            holder.nbEpG.text = epG[position]
            holder.nbEpD.text = epD[position]

        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nbSeance: TextView = itemView.num_seance
        val expendableLayout: ConstraintLayout = itemView.expandableLayout
        val fleche: ImageView = itemView.fleche_button
        val nbHdos: TextView = itemView.hautDos_nb
        val nbBdos: TextView = itemView.basDos_nb
        val nbCdos: TextView = itemView.centreDos_nb
        val nbEpG: TextView = itemView.epG_nb
        val nbEpD: TextView = itemView.epD_nb
        val tendance: TextView = itemView.tendance_txt
    }
    private fun compareDonnees(position: Int){
        val listmax: ArrayList<String> = ArrayList()

        listmax.add(hautDos[position])
        listmax.add(basDos[position])
        listmax.add(centreDos[position])
        listmax.add(epD[position])
        listmax.add(epG[position])

        val stringlist: String? = listmax.max()

        if (stringlist != null) {
            if (stringlist == hautDos[position])
                maxDanger = "Haut du dos"
            if (stringlist == basDos[position])
                maxDanger = "Bas du dos"
            if (stringlist == centreDos[position])
                maxDanger = "Centre du dos"
            if (stringlist == epD[position])
                maxDanger = "Epaule droite"
            if (stringlist == epG[position])
                maxDanger = "Epaule gauche"
        }
    }
}