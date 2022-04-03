package com.android.cedecsi.ui.navigation

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.android.cedecsi.BuildConfig
import com.android.cedecsi.R
import com.android.cedecsi.databinding.FragmentShareBinding
import com.android.cedecsi.util.FileUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File


/**
 * A simple [Fragment] subclass.
 * Use the [ShareFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ShareFragment : Fragment() {

    private lateinit var binding: FragmentShareBinding
    private var path = ""
    private var latitude = 0.0
    private var longitude = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            path = it.getString(path_key, "")
            latitude = it.getDouble(latitude_key)
            longitude = it.getDouble(longitude_key)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShareBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        BitmapFactory.decodeFile(path)?.let { b->
            val bitmap = Bitmap.createScaledBitmap(b, b.width, b.height, true)
            binding.imgShare.setImageBitmap(bitmap)
        }
        binding.txtLatitude.text = getString(R.string.latitude, latitude.toString())
        binding.txtLongitude.text = getString(R.string.longitude, longitude.toString())

        binding.btnShare.setOnClickListener { printPdf() }
    }

    private fun printPdf() {
        binding.progress.isVisible = true
        val pdfDocument = FileUtil.createPdf(binding.layoutPdf)
        lifecycleScope.launch(Dispatchers.Default) {
            FileUtil.savePdfToStorage(
                requireContext(),
                pdfDocument,
                "Ubicación.pdf",
                "Cedecsi",
                BuildConfig.APPLICATION_ID + ".provider"
            )?.let {
                Log.i("Documento", it.path ?: "")
                launch(Dispatchers.Main) {
                    binding.progress.isVisible = false
                    val shareIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_STREAM, it)
                        type = "application/pdf"
                    }
                    startActivity(Intent.createChooser(shareIntent, null))
                }
            } ?: run {
                launch(Dispatchers.Main) {
                    binding.progress.isVisible = false
                    Toast.makeText(requireContext(), "No se pudo completar la operación", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        const val path_key = "path-key"
        const val latitude_key = "latitude-key"
        const val longitude_key = "longitude-key"
    }
}