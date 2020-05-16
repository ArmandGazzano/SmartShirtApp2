package fr.isen.levreau.smartshirtapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.isen.levreau.smartshirtapp.R.layout.activity_home_cell
import kotlinx.android.synthetic.main.activity_home_cell.view.*
import kotlin.reflect.KFunction1

class MenuAdapter(
    val choix: ArrayList<String>,
    val mImage: List<Int>,
    val mItent: ArrayList<Intent>,
    private val imageClickListener: KFunction1<@ParameterName(name = "mItent") Intent, Unit>
): RecyclerView.Adapter<MenuAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(activity_home_cell, parent, false))

    override fun getItemCount(): Int = choix.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = choix[position]
        val image_id = mImage[position]
        holder.image.setImageResource(image_id)
        holder.menuCell.setOnClickListener {
            imageClickListener.invoke(mItent[position])
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.menu_button
        val image: ImageView = itemView.image_button
        var menuCell = itemView.menucell

    }

}