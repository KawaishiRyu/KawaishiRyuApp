package app.kawaishiryu.jiujitsu.util

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import app.kawaishiryu.jiujitsu.R
import app.kawaishiryu.jiujitsu.databinding.CustomSnackbar2LayoutBinding
import app.kawaishiryu.jiujitsu.databinding.CustomSnackbar3LayoutBinding
import app.kawaishiryu.jiujitsu.databinding.CustomSnackbarLayoutBinding
import com.google.android.material.snackbar.Snackbar

class SnackbarUtils {
    companion object {
        // Option 1 (Message successful)
        // Option 2 (Error)
        // Option 3 (Precaution)

        fun showCustomSnackbar(
            view: View,
            layoutInflater: LayoutInflater,
            option: Int,
            message: String // Nuevo parámetro para el mensaje
        ) {
            showCustomSnackbarInternal(view, layoutInflater, option, message)
        }

        private fun showCustomSnackbarInternal(
            view: View,
            layoutInflater: LayoutInflater,
            option: Int,
            message: String
        ) {
            val snackbar = Snackbar.make(view, "", Snackbar.LENGTH_LONG)

            val customView = when (option) {
                1 -> {
                    val binding = CustomSnackbarLayoutBinding.inflate(layoutInflater)
                    binding.message.text = message
                    binding.root
                }
                2 -> {
                    val binding = CustomSnackbar2LayoutBinding.inflate(layoutInflater)
                    binding.message.text = message
                    binding.root
                }
                3 -> {
                    val binding = CustomSnackbar3LayoutBinding.inflate(layoutInflater)
                    binding.message.text = message
                    binding.root
                }
                else -> {
                    val defaultBinding = CustomSnackbarLayoutBinding.inflate(layoutInflater)
                    defaultBinding.message.text = message
                    defaultBinding.root
                }
            }

            snackbar.view.setBackgroundColor(Color.TRANSPARENT)

            val snackBarView = snackbar.view as Snackbar.SnackbarLayout
            snackBarView.setPadding(0, 0, 0, 0)
            snackBarView.addView(customView, 0)
            snackbar.show()
        }
    }
}
