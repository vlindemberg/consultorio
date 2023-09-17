package com.marcelo.piscologo.consultorio.utils

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.marcelo.piscologo.consultorio.R
import com.marcelo.piscologo.consultorio.databinding.ProfileButtonBinding

private const val DEFAULT_AVATAR_ICON = 0

class ProfileButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attributeSet, defStyleAttr) {

    private var binding: ProfileButtonBinding =
        ProfileButtonBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.ProfileButton, 0, 0)
            .use { typedArray ->
                setupAttrs(typedArray)
            }
    }

    private fun setupAttrs(typedArray: TypedArray) {
        typedArray.getResourceId(R.styleable.ProfileButton_icon, DEFAULT_AVATAR_ICON).apply {
            if (this != DEFAULT_AVATAR_ICON) setImageIcon(this)
        }
        typedArray.getString(R.styleable.ProfileButton_title)?.let { text ->
            setTitle(text)
        }
        typedArray.getString(R.styleable.ProfileButton_subtitle)?.let { text ->
            setSubtitle(text)
        }
    }

    private fun setImageIcon(icon: Int) {
        binding.ivIconBtn.setImageResource(icon)
    }

    private fun setTitle(title: String) {
        binding.tvTitle.text = title
    }

    private fun setSubtitle(subtitle: String) {
        binding.tvSubtitle.text = subtitle
        binding.tvSubtitle.visibility = View.VISIBLE
    }

}