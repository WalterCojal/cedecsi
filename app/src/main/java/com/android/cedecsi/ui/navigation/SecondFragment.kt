package com.android.cedecsi.ui.navigation

import android.graphics.*
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.android.cedecsi.R
import com.android.cedecsi.databinding.FragmentFirstBinding
import com.android.cedecsi.databinding.FragmentSecondBinding
import com.android.cedecsi.ui.location.GPSProvider
import com.android.cedecsi.ui.location.GpsProviderType
import com.android.cedecsi.util.FileUtil
import com.android.cedecsi.util.hasGoogleServices
import java.io.File

/**
 * A simple [Fragment] subclass.
 * Use the [SecondFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SecondFragment : Fragment() {

    private var path = ""
    private var bitmap: Bitmap? = null

    // TODO. Uncomment binding initialization
    private lateinit var binding: FragmentSecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            path = it.getString(FirstFragment.key_path, "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // TODO Uncomment instantiation of binding
        binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
//        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupListeners()

        // TODO Uncomment to render image
        BitmapFactory.decodeFile(path)?.let { b->
            bitmap = b
            bitmap = Bitmap.createScaledBitmap(b, b.width, b.height, true)
            binding.imageView.setImageBitmap(bitmap)
        }
    }

    private fun setupViews() {
    }

    private fun setupListeners() {
        (requireActivity() as NavigationActivity).gpsProvider.onLocation = {
            addTextToImage("${it.latitude}, ${it.longitude}")
        }
        // TODO Uncomment to listen to btnDrawLocation actions
        binding.btnDrawLocation.setOnClickListener {
            (requireActivity() as NavigationActivity).gpsProvider.checkPermission()
        }
        binding.btnSave.setOnClickListener {
            binding.progress.isVisible = true
            bitmap?.let {
                FileUtil.saveImageToStorage(
                    context = requireContext(),
                    bitmap = it,
                    name = path.split("/").last(),
                    providerId = "${requireActivity().packageName}.provider",
                    appName = getString(R.string.app_name)
                ) { uri ->
                    binding.progress.isVisible = false
                    Log.i("Save photo", uri?.path ?: "")
                }
            }
        }
    }

    private fun addTextToImage(text: String) {
        bitmap = bitmap?.copy(bitmap?.config, true)?.let {
            val canvas = Canvas(it)

            val textHeight = it.height / 36

            Paint().apply {
                flags = Paint.ANTI_ALIAS_FLAG
                color = Color.YELLOW
                textSize = textHeight.toFloat()
                typeface = Typeface.MONOSPACE
                setShadowLayer(5f, 0f, 5f, Color.WHITE)
                canvas.drawText(text, (textHeight / 2).toFloat(), it.height - (textHeight / 2).toFloat(), this)
            }
            binding.imageView.setImageBitmap(it)
            it
        }
    }

}