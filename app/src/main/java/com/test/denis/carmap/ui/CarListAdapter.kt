package com.test.denis.carmap.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.Placeholder
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.denis.carmap.R
import com.test.denis.carmap.model.CarModel
import kotlinx.android.synthetic.main.item_car.view.*

class CarListAdapter(
    var callback: ((CarModel) -> Unit)
) : RecyclerView.Adapter<CarItemViewHolder>() {

    private val items: MutableList<CarModel> = mutableListOf()

    fun initData(listItems: List<CarModel>) {
        items.clear()
        items.addAll(listItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_car, parent, false)

        return CarItemViewHolder(itemView)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(viewHolder: CarItemViewHolder, position: Int) {
        val listItemModel = items[position]
        viewHolder.bind(listItemModel, callback)
    }
}

class CarItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(listItemModel: CarModel, callback: (CarModel) -> Unit) {
        with(listItemModel) {
            Glide.with(itemView)
                .load(imageUrl)
                .placeholder(R.drawable.ic_riding_car)
                .into(itemView.carImage)

            itemView.carName.text = "$modelName $name"
            itemView.license.text = licensePlate
            itemView.fuelType.text = fuelType
            itemView.fuelLevel.progress = (fuelLevel * 100).toInt()

            itemView.setOnClickListener {
                callback.invoke(listItemModel)
            }
        }
    }
}