package pl.ozodbek.pdfviewerpractice.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import pl.ozodbek.pdfviewerpractice.R
import pl.ozodbek.pdfviewerpractice.util.changeFragmentTo
import pl.ozodbek.pdfviewerpractice.util.doWhileRequestPermission
import pl.ozodbek.pdfviewerpractice.util.doWhileSelfCheckPermission
import pl.ozodbek.pdfviewerpractice.util.openAppSettings
import pl.ozodbek.pdfviewerpractice.util.showDialog

@SuppressLint("CustomSplashScreen")
class SplashScreenFragment : Fragment(R.layout.fragment_splash_screen) {


    private val dynamicReceiverPermissionStorage by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE
            )
        } else {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        callStoragePermission()
    }

    private val requestStoragePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true &&
                permissions[Manifest.permission.MANAGE_EXTERNAL_STORAGE] == true) {
                // Both permissions granted
                changeFragmentTo(SplashScreenFragmentDirections.actionSplashScreenFragmentToMainFragment())
            } else {
                // Handle permission denial
                requestStoragePermission()
            }
        }



    private fun callStoragePermission() {
        doWhileSelfCheckPermission(
            dynamicReceiverPermissionStorage,
            onPermissionGranted = {
                changeFragmentTo(SplashScreenFragmentDirections.actionSplashScreenFragmentToMainFragment())
            }
        ) {
            requestStoragePermissionLauncher.launch(dynamicReceiverPermissionStorage)
        }
    }

    private fun requestStoragePermission() {
        doWhileRequestPermission(
            dynamicReceiverPermissionStorage,
            onPermissionGranted = {
                changeFragmentTo(SplashScreenFragmentDirections.actionSplashScreenFragmentToMainFragment())
            },
            onPermissionDenied = {
                showDialog(
                    "Sizdan ruxsat talab qilinadi.",
                    "Siz ruxsat berish orqali, foydalanuvchiga SMS yuborishingiz mumkin. Iltimos, ruhsat bering.",
                    "OK",
                    actionPositive = {
                        requestStoragePermissionLauncher.launch(dynamicReceiverPermissionStorage)
                    }
                )
            },
            onPermissionFullyDenied = {
                showDialog(
                    "Sizdan ruxsat talab qilinadi",
                    "Bu funsiydan foydalanish uchun sozlamalardan qayta yoqishingiz mumkin.",
                    "Sozlamalar",
                    actionPositive = {
                        openAppSettings()
                    }
                )
            }
        )
    }
}