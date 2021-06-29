package com.smitcoderx.softageproject.UI.Saved

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.smitcoderx.softageproject.Adapters.ProjectAdapter
import com.smitcoderx.softageproject.R
import com.smitcoderx.softageproject.UI.MainActivity
import com.smitcoderx.softageproject.UI.ProjectViewModel
import com.smitcoderx.softageproject.databinding.FragmentSavedBinding

class SavedFragment : Fragment(R.layout.fragment_saved) {

    private lateinit var binding: FragmentSavedBinding
    lateinit var viewModel: ProjectViewModel
    lateinit var adapter: ProjectAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSavedBinding.bind(view)

        viewModel = (activity as MainActivity).viewModel
        setupRv()

        viewModel.allLocations().observe(viewLifecycleOwner, {
            adapter.differ.submitList(it)
        })
    }


    private fun setupRv() {
        adapter = ProjectAdapter()
        binding.rvSaved.adapter = adapter
    }

}