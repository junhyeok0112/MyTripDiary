package org.techtown.mtd.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.techtown.mtd.R
import org.techtown.mtd.databinding.FragmentBoardBinding

class BoardFragment : Fragment() {

    lateinit var binding : FragmentBoardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBoardBinding.inflate(layoutInflater , container , false)
        return binding.root

    }

}