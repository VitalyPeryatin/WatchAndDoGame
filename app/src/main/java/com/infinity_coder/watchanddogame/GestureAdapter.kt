package com.infinity_coder.watchanddogame

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_gesture.*

class GestureAdapter(
    private val itemClickListener: OnItemClickListener?
) : RecyclerView.Adapter<GestureAdapter.GestureViewHolder>() {

    private var gestureList = mutableListOf<GestureEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GestureViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_gesture, parent, false)
        return GestureViewHolder(view, itemClickListener)
    }

    override fun getItemCount(): Int {
        return gestureList.size
    }

    override fun onBindViewHolder(holder: GestureViewHolder, position: Int) {
        holder.bind(gestureList[position])
    }

    fun setGestures(gestures: List<GestureEntity>) {
        gestureList = gestures.toMutableList()
        notifyDataSetChanged()
    }

    class GestureViewHolder(
        override val containerView: View,
        private val itemClickListener: OnItemClickListener?
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(gestureEntity: GestureEntity) {
            nameTextView.text = gestureEntity.gestureName
            val gestureBitmap = gestureEntity.gesture.toBitmap(30, 30, 3, Color.YELLOW)
            gestureImageView.setImageBitmap(gestureBitmap)

            deleteImageView.setOnClickListener {
                itemClickListener?.onDelete(gestureEntity)
            }
        }
    }

    interface OnItemClickListener {
        fun onDelete(gestureEntity: GestureEntity)
    }
}