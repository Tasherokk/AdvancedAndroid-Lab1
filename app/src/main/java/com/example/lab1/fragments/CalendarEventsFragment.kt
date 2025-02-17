package com.example.lab1.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lab1.adapter.CalendarEvent
import com.example.lab1.adapter.EventsAdapter
import com.example.lab1.databinding.FragmentCalendarEventsBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CalendarEventsFragment : Fragment() {

    private var _binding: FragmentCalendarEventsBinding? = null
    private val binding get() = _binding!!

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                loadCalendarEvents()
            } else {
                // Permission denied; show empty or error
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        checkCalendarPermissionAndLoad()
    }

    private fun checkCalendarPermissionAndLoad() {
        val permission = Manifest.permission.READ_CALENDAR
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Permission already granted
                loadCalendarEvents()
            }
            else -> {
                // Request permission
                requestPermissionLauncher.launch(permission)
            }
        }
    }

    private fun loadCalendarEvents() {
        val eventsList = mutableListOf<CalendarEvent>()
        val projection = arrayOf(
            CalendarContract.Events._ID,
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.DTEND
        )
        val selection = "${CalendarContract.Events.DTSTART} >= ?"
        val currentTime = System.currentTimeMillis()
        val selectionArgs = arrayOf(currentTime.toString())
        val sortOrder = "${CalendarContract.Events.DTSTART} ASC"

        val cursor = requireContext().contentResolver.query(
            CalendarContract.Events.CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )
        cursor?.use {
            val titleIndex = it.getColumnIndex(CalendarContract.Events.TITLE)
            val dtStartIndex = it.getColumnIndex(CalendarContract.Events.DTSTART)
            val dtEndIndex = it.getColumnIndex(CalendarContract.Events.DTEND)

            while (it.moveToNext()) {
                val title = it.getString(titleIndex) ?: "No Title"
                val startTimeMillis = it.getLong(dtStartIndex)
                val endTimeMillis = it.getLong(dtEndIndex)

                val startTime = formatDateTime(startTimeMillis)
                val endTime = formatDateTime(endTimeMillis)

                eventsList.add(CalendarEvent(title, startTime, endTime))
            }
        }

        binding.recyclerView.adapter = EventsAdapter(eventsList)
    }

    private fun formatDateTime(timeInMillis: Long): String {
        val date = Date(timeInMillis)
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return format.format(date)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}