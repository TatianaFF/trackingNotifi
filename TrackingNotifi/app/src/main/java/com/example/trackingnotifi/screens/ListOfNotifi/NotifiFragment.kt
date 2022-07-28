package com.example.trackingnotifi.screens.ListOfNotifi

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.trackingnotifi.R

class NotifiFragment : Fragment() {

    companion object {
        fun newInstance() = NotifiFragment()
    }

    private lateinit var viewModel: NotifiViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.notifi_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NotifiViewModel::class.java)
        // TODO: Use the ViewModel
    }

}