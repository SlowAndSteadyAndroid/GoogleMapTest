package com.example.googlemaptest.ext

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.googlemaptest.R
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout


fun AppCompatActivity.showToast(context: Context = this, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.showToast(context: Context = this.requireContext(), message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}


fun View.hideKeyboard(context: Context = this.context) {
    val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(this.windowToken, 0)
}

fun TextInputLayout.showError(message: String) {
    error = message
    isErrorEnabled = true
}

fun TextInputLayout.hideError() {
    isErrorEnabled = false
}

fun ConstraintLayout.showPOIInfoContainer(context: Context) {
    bringToFront()
    isVisible = true
    startAnimation(
        AnimationUtils.loadAnimation(
            context,
            R.anim.slide_up
        )
    )
}

fun ConstraintLayout.hidePOIInfoContainer(context: Context) {
    if (isVisible) {
        isVisible = false
        startAnimation(
            AnimationUtils.loadAnimation(
                context,
                R.anim.slide_down
            )
        )
    }
}

fun showSnackBar(attachLayout: CoordinatorLayout, message: String) {
    attachLayout.bringToFront()
    val snackbar = Snackbar.make(
        attachLayout, message, Snackbar.LENGTH_SHORT
    )
    val layoutParams = CoordinatorLayout.LayoutParams(snackbar.view.layoutParams)
    layoutParams.gravity = Gravity.TOP
    layoutParams.topMargin = 10
    snackbar.view.layoutParams = layoutParams
    snackbar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
    snackbar.show()
}
