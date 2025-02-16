package com.valify.registration.presentation.captureImage

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.google.common.util.concurrent.ListenableFuture
import com.valify.registration.R
import com.valify.registration.databinding.FragmentCaptureImageBinding
import com.valify.registration.domain.utils.AppError
import com.valify.registration.presentation.captureImage.viewmodel.CaptureIntent
import com.valify.registration.presentation.captureImage.viewmodel.CaptureViewModel
import com.valify.registration.utils.FaceDetectSmile
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.Executor

@AndroidEntryPoint
class CaptureImageFragment : Fragment(R.layout.fragment_capture_image) {

    private var _binding: FragmentCaptureImageBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CaptureViewModel by viewModels()
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private val imageCapture = ImageCapture.Builder().build()

    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            binding.tryToStartCamera()
        } else {
            val showRationale = shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)
            if (!showRationale) {
                Snackbar.make(binding.root, getString(R.string.please_enable_camera_permission_msg), Snackbar.LENGTH_LONG)
                    .setAction("Settings") {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", requireContext().packageName, null)
                        }
                        startActivity(intent)
                    }
                    .show()
            } else {
                Toast.makeText(requireContext(), getString(R.string.camera_permission_is_required_msg), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        FragmentCaptureImageBinding.inflate(inflater, container, false).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bindClicks()
        binding.tryToStartCamera()
        binding.collectCaptureImageState()
    }

    private fun trySaveImageCaptured(bitmap: Bitmap) {
        viewModel.sendIntent(CaptureIntent.SaveCaptureImageIntent(bitmap))
    }

    private fun FragmentCaptureImageBinding.cameraInit() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun FragmentCaptureImageBinding.bindPreview(cameraProvider: ProcessCameraProvider) {
        cameraProvider.unbindAll()
        val executor = ContextCompat.getMainExecutor(requireContext())
        val preview: Preview = Preview.Builder()
            .build()

        val cameraSelector: CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
            .build()

        preview.surfaceProvider = cameraContainer.getSurfaceProvider()
        cameraProvider.bindToLifecycle(viewLifecycleOwner, cameraSelector, preview, imageCapture, smileDetection(executor) {
            captureImage(executor, imageCapture) {
                trySaveImageCaptured(it)
            }
        })
    }

    private fun FragmentCaptureImageBinding.bindClicks() {
        permissionCameraButton.setOnClickListener {
            if (!isGrantedCameraPermission()) {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun FragmentCaptureImageBinding.tryToStartCamera() {
        if (isGrantedCameraPermission()) {
            permissionMsg.visibility = View.GONE
            permissionCameraButton.visibility = View.GONE
            cameraContainer.visibility = View.VISIBLE
            cameraInit()
        } else {
            cameraContainer.visibility = View.GONE
            permissionMsg.visibility = View.VISIBLE
            permissionCameraButton.visibility = View.VISIBLE

        }
    }

    private fun FragmentCaptureImageBinding.collectCaptureImageState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { state ->
                    with(state) {
                        appFailure?.apply {
                            if (this !is AppError.ValidateError) {
                                val message = when (this) {
                                    is AppError.DatabaseError -> getString(R.string.database_error_msg)
                                    else -> getString(R.string.something_went_wrong_msg)
                                }
                                Snackbar.make(root, message, Snackbar.LENGTH_SHORT).setBackgroundTint(Color.RED).setTextColor(Color.WHITE).show()
                            }
                        }
                        if (success) {
                            requireActivity().finish()
                        }
                    }
                }
            }
        }
    }

    private fun captureImage(executor: Executor, imageCapture: ImageCapture, onImageCaptured: (Bitmap) -> Unit) {
        imageCapture.takePicture(executor, object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                onImageCaptured(image.toBitmap())
                image.close()
            }
        })
    }

    private fun smileDetection(executor: Executor, onSmileDetect: () -> Unit): ImageAnalysis {
        return ImageAnalysis.Builder().setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build().also {
            it.setAnalyzer(executor, FaceDetectSmile(onSmileDetect))
        }
    }

    private fun isGrantedCameraPermission() =
        ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}