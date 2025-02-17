package com.example.lab1.fragments

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.lab1.databinding.FragmentAirplaneModeBinding
import com.example.lab1.receiver.AirplaneModeReceiver

class AirplaneModeFragment : Fragment() {

    private var _binding: FragmentAirplaneModeBinding? = null
    private val binding get() = _binding!!

    private val airplaneModeReceiver = AirplaneModeReceiver()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAirplaneModeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        // Register the receiver
        val filter = IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        requireActivity().registerReceiver(airplaneModeReceiver, filter)
    }

    override fun onPause() {
        super.onPause()
        // Unregister the receiver
        requireActivity().unregisterReceiver(airplaneModeReceiver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
