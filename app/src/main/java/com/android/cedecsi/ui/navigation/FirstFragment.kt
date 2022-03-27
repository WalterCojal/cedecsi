package com.android.cedecsi.ui.navigation

import android.Manifest
import android.content.DialogInterface
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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.android.cedecsi.R
import com.android.cedecsi.databinding.FragmentFirstBinding
import com.android.cedecsi.util.getFormat
import com.android.cedecsi.util.launcher.IntentLauncher
import com.android.cedecsi.util.launcher.LauncherResult
import pe.com.service.common.util.ImageUtils
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

    private lateinit var binding: FragmentFirstBinding

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
    ): View {
        binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
//        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupListeners()
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
                            ImageUtils.resizeImage(file.absolutePath, file) // Se escala la imagen a un tamaño determinado
                            var bitmap = BitmapFactory.decodeFile(file.absolutePath)
                            bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.width, bitmap.height, true)
                            binding.imageView.setImageBitmap(bitmap)
                            binding.btnConfirm.isVisible = true
                        }
                    }
                }
                is LauncherResult.Error->{
                    AlertDialog.Builder(requireContext())
                        .setTitle("Error al cargar imagen")
                        .setMessage("No se pudo cargar la imagen seleccionada, por favor intente de nuevo con otra imagen o vuelva más tarde")
                        .setPositiveButton("Aceptar") { dialog, _ -> dialog?.dismiss() }
                        .create().also { dialog ->
                            dialog.show()
                        }
                }
            }
        }
        binding.btnTakePicture.setOnClickListener { takePicture() }
        binding.btnConfirm.setOnClickListener { continuePhoto() }
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