package fr.isen.levreau.smartshirtapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_followup_evolution.view.*

class EvolutionAdapter(
    val post: ArrayList<String>,
    val mBarre: ArrayList<Int>,
    val hautDos: ArrayList<String>,
    val basDos: ArrayList<String>,
    val centreDos: ArrayList<String>,
    val epG: ArrayList<String>,
    val epD: ArrayList<String>
) : RecyclerView.Adapter<EvolutionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_followup_evolution, parent, false)
        )

    override fun getItemCount(): Int= post.size

    override fun onBindViewHolder(holder: EvolutionAdapter.ViewHolder, position: Int) {
        holder.part_corp.text = post[position]

        holder.image1.setImageResource(mBarre[selectBarre(position, 0)])
        holder.image2.setImageResource(mBarre[selectBarre(position, 1)])
        holder.image3.setImageResource(mBarre[selectBarre(position, 2)])
        holder.image4.setImageResource(mBarre[selectBarre(position, 3)])

        holder.nb_vibra1.text= afficheVibra(position, 0)
        holder.nb_vibra2.text= afficheVibra(position, 1)
        holder.nb_vibra3.text= afficheVibra(position, 2)
        holder.nb_vibra4.text= afficheVibra(position, 3)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val part_corp: TextView = itemView.txtView
        val image1: ImageView = itemView.image_barre1
        val image2: ImageView = itemView.image_barre2
        val image3: ImageView = itemView.image_barre3
        val image4: ImageView = itemView.image_barre4
        val nb_vibra1 : TextView = itemView.txt_barre1
        val nb_vibra2 : TextView = itemView.txt_barre2
        val nb_vibra3 : TextView = itemView.txt_barre3
        val nb_vibra4 : TextView = itemView.txt_barre4
    }

    private fun afficheVibra(position: Int,num_image : Int): String {
        if (post[position] == post[0]){
            return hautDos[num_image]
        }
        else if (post[position] == post[1]){
            return centreDos[num_image]
        }
        else if (post[position] == post[2]){
            return basDos[num_image]
        }
        else if (post[position] == post[3]){
            return epG[num_image]
        }
        else if (post[position] == post[4]){
            return epD[num_image]
        }

        return hautDos[num_image]
    }

    private fun selectBarre(position: Int, num_image : Int) : Int{
        var image_id : Int = 0
        if (post[position] == "Haut du dos"){
            when {
                hautDos[num_image].toInt() <= 3 -> image_id = 3
                hautDos[num_image].toInt() in 4..5 -> image_id = 2
                hautDos[num_image].toInt() in 6..7 -> image_id = 1
                hautDos[num_image].toInt() > 7 -> image_id = 0
            }
        }
        else if (post[position] == "Centre du dos"){
            when {
                centreDos[num_image].toInt() <= 3 -> image_id = 3
                centreDos[num_image].toInt() in 4..5 -> image_id = 2
                centreDos[num_image].toInt() in 6..7 -> image_id = 1
                centreDos[num_image].toInt() > 7 -> image_id = 0
            }
        }
        else if (post[position] == "Bas du dos"){
            when {
                basDos[num_image].toInt() <= 3 -> image_id = 3
                basDos[num_image].toInt() in 4..5 -> image_id = 2
                basDos[num_image].toInt() in 6..7 -> image_id = 1
                basDos[num_image].toInt() > 7 -> image_id = 0
            }
        }
        else if (post[position] == "Epaule droite"){
            when {
                epD[num_image].toInt() <= 3 -> image_id = 3
                epD[num_image].toInt() in 4..5 -> image_id = 2
                epD[num_image].toInt() in 6..7 -> image_id = 1
                epD[num_image].toInt() > 7 -> image_id = 0
            }
        }
        else if (post[position] == "Epaule gauche"){
            when {
                epG[num_image].toInt() <= 3 -> image_id = 3
                epG[num_image].toInt() in 4..5 -> image_id = 2
                epG[num_image].toInt() in 6..7 -> image_id = 1
                epG[num_image].toInt() > 7 -> image_id = 0
            }
        }

        var partie = post[position]
        var epaule = epD[num_image]
        println("nb faux epaule = $epaule")
        println("partie : $partie")
        println("image num : $image_id")
        return image_id

    }

}