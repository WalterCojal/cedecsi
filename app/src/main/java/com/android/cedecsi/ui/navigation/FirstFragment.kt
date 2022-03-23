package com.android.cedecsi.ui.navigation

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.navigation.fragment.findNavController
import com.android.cedecsi.R
import com.android.cedecsi.util.getFormat
import com.android.cedecsi.util.launcher.IntentLauncher
import com.android.cedecsi.util.launcher.LauncherResult
import java.io.File
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [FirstFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FirstFragment : Fragment() {

    companion object {
        const val key_path = "key-path"
        const val WRITE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE
        const val CAMERA_PERMISSION = Manifest.permission.CAMERA
    }

    private lateinit var cameraLauncher: IntentLauncher<Uri, Boolean>
    private var file: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraLauncher = IntentLauncher(
            requireActivity() as AppCompatActivity,
            arrayOf(WRITE_PERMISSION, CAMERA_PERMISSION),
            ActivityResultContracts.TakePicture()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    private fun setupViews() {

    }

    private fun setupListeners() {
        cameraLauncher.result = {
            when (it) {
                is LauncherResult.Success->{
                    file?.let { file ->
                        if (it.result) {
                            Log.i("CommerceActivity", file.absolutePath)
                            var bitmap = BitmapFactory.decodeFile(file.absolutePath)
                            bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.width, bitmap.height, true)
                            // TODO Fill image with photo
                        }
                    }
                }
                is LauncherResult.Error->{

                }
            }
        }
    }

    private fun takePicture() {
        val name = "Image_${getFormat("ddMMyyyy_hhmmss", Calendar.getInstance().time)}"
        file = File.createTempFile(name, ".png", requireActivity().cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }
        val uri = FileProvider.getUriForFile(
            requireContext(),
            "${requireActivity().packageName}.provider",
            file!!
        )
        cameraLauncher.executeLauncher(uri)
    }

    private fun continuePhoto() {
        val bundle = Bundle().apply {
            putString(key_path, file?.absolutePath)
        }
        findNavController().navigate(R.id.action_firstFragment_to_secondFragment2, bundle)
    }

}