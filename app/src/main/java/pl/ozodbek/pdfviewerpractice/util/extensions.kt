package pl.ozodbek.pdfviewerpractice.util

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun Fragment.changeFragmentTo(destination: Any) {
    val navController = this.findNavController()

    when (destination) {
        is Int -> {
            navController.safeNavigate(destination)
        }

        is NavDirections -> {
            navController.safeNavigate(destination)
        }
    }
}

fun Fragment.changeFragmentTo(destination: Any, navController: NavController?) {

    when (destination) {
        is Int -> {
            navController?.safeNavigate(destination)
        }

        is NavDirections -> {
            navController?.safeNavigate(destination)
        }
    }
}

fun NavController.safeNavigate(direction: Any) {
    when (direction) {
        is Int -> {
            if (currentDestination?.id == direction) {
                return
            }
            navigate(direction)
        }

        is NavDirections -> {
            var isNavigationEnabled = true
            CoroutineScope(Dispatchers.Main).launch {
                if (isNavigationEnabled) {
                    isNavigationEnabled = false
                    currentDestination?.getAction(direction.actionId)?.run {
                        navigate(direction)
                    }
                    delay(1000)
                    isNavigationEnabled = true
                }
            }
        }

        else -> {
        }
    }
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.hide() {
    this.visibility = View.INVISIBLE
}

fun View.onClick(clickListener: (View) -> Unit) {
    setOnClickListener(clickListener)
}


fun Fragment.onBackPressed(onBackPressed: OnBackPressedCallback.() -> Unit) {
    requireActivity().onBackPressedDispatcher.addCallback(this) {
        onBackPressed()
    }
}

fun Fragment.popBackStack() {
    findNavController().popBackStack()
}


fun View.showSoftKeyboard() {
    if (this.requestFocus()) {
        val imm = this.context.getSystemService(InputMethodManager::class.java)
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun Fragment.hideSoftKeyboard() {
    val inputMethodManager = requireActivity().getSystemService(
        Activity.INPUT_METHOD_SERVICE
    ) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(requireView().windowToken, 0)
}


val EditText.fullText: String
    get() = this.text.toString().trim()

fun <T : ViewBinding> ViewGroup.viewBinding(viewBindingFactory: (LayoutInflater, ViewGroup, Boolean) -> T) =
    viewBindingFactory.invoke(LayoutInflater.from(this.context), this, false)



fun Fragment.doWhileSelfCheckPermission(
    permissions: Array<String>,
    onPermissionGranted: () -> Unit,
    inElseCase: () -> Unit,
) {
    val hasAllPermissions = permissions.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    if (hasAllPermissions) {
        onPermissionGranted()
    } else {
        inElseCase()
    }
}

fun Fragment.doWhileRequestPermission(
    permissions: Array<String>,
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit,
    onPermissionFullyDenied: () -> Unit,
) {
    val hasAllPermissions = permissions.all {
        ActivityCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    when {
        hasAllPermissions -> {
            // Permission is already granted, execute the action
            onPermissionGranted()
        }
        permissions.any {
            ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), it)
        } -> {
            // Permission denied but rationale can be shown
            onPermissionDenied()
        }
        else -> {
            onPermissionFullyDenied()
        }
    }
}


fun Fragment.openAppSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", requireContext().packageName, null)
    intent.data = uri
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(intent)
}

fun Fragment.showDialog(
    title: String,
    message: String,
    textPositive: String,
    actionPositive: () -> Unit,
) {
    val rationaleDialog = AlertDialog.Builder(requireContext())
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(textPositive) { _, _ ->
            actionPositive()
        }
        .setNegativeButton("Yopish") { _, _ ->
        }
        .create()

    rationaleDialog.show()
}



