package com.example.camerax.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.camerax.R
import com.example.camerax.model.GalleryModel
import com.timelysoft.tsjdomcom.common.GenericRecyclerAdapter
import com.timelysoft.tsjdomcom.common.ViewHolder
import kotlinx.android.synthetic.main.item_gallery.view.*
import android.view.MotionEvent
import android.annotation.SuppressLint
import android.os.CountDownTimer
import android.view.View.OnTouchListener

class GalleryAdapter( var listener: GalleryListener, var list: ArrayList<GalleryModel> = arrayListOf()): GenericRecyclerAdapter<GalleryModel>(list) {
//    private lateinit var timer: CountDownTimer

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return super.onCreateViewHolder(parent, R.layout.item_gallery)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun bind(item: GalleryModel, holder: ViewHolder) {
        holder.itemView.textDateTime.text = item.current
        Glide
            .with(holder.itemView.context)
            .load(item.image)
            .into(holder.itemView.galleryImageView)

        holder.itemView.imageClear.setOnClickListener {
            listener.onClickListener(item.id, item.image, item.current, holder.adapterPosition)
        }

//        holder.itemView.galleryImageView.setOnTouchListener(OnTouchListener { v, event ->
//            if(event.action == MotionEvent.ACTION_DOWN){
//                timer = object : CountDownTimer(5000, 1000) {
//                    override fun onTick(millisUntilFinished: Long) {}
//                    override fun onFinish() {
//
//                    }
//                }
//                timer.start()
//            }else if(event.action == MotionEvent.ACTION_UP) {
//                timer.cancel()
//            }
//            false
//        })
    }
}