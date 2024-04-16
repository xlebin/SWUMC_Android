package com.example.umc01

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.umc01.databinding.FragmentSavedsongBinding

class SavedSongFragment : Fragment() {
    lateinit var binding: FragmentSavedsongBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedsongBinding.inflate(inflater, container, false)
        return binding.root
    }
}