package com.example.trackingnotifi.screens.Settings

import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.trackingnotifi.R
import com.example.trackingnotifi.databinding.FragmentSettingsBinding
import com.example.trackingnotifi.databinding.NotifiFragmentBinding
import com.example.trackingnotifi.screens.ListOfNotifi.NotifiViewModel

class SettingsFragment : Fragment() {
    private val TAG = this.javaClass.simpleName
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var tvSettings: TextView
    private lateinit var imgMail: ImageView
    private lateinit var btnSendMail: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)

        tvSettings = binding.tvSettings
        imgMail = binding.imgSettMail
        btnSendMail = binding.btnSendMail


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        val viewModel = ViewModelProvider(this)[SettingsViewModel::class.java]

        btnSendMail.setOnClickListener{
            val mIntent = Intent(Intent.ACTION_SENDTO)
            mIntent.data = Uri.parse("mailto:")
            mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("tsff2018@gmail.com"))
//            mIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("content://path/to/email/attachment"))

            try {
                //start email intent
                context?.startActivity(mIntent)
            }
            catch (e: Exception){
                //if any thing goes wrong for example no email client application or any exception
                //get and show exception message
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }

    }

}