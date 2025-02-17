package com.example.lab1.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lab1.databinding.ItemEventBinding

data class CalendarEvent(
    val title: String,
    val startTime: String,
    val endTime: String
)

class EventsAdapter(private val events: List<CalendarEvent>) :
    RecyclerView.Adapter<EventsAdapter.EventViewHolder>() {

    inner class EventViewHolder(val binding: ItemEventBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemEventBinding.inflate(inflater, parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.binding.tvTitle.text = event.title
        holder.binding.tvStart.text = event.startTime
        holder.binding.tvEnd.text = event.endTime
    }

    override fun getItemCount(): Int = events.size
}