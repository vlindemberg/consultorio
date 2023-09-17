package com.marcelo.piscologo.consultorio.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

fun EditText.insertMask(maskType: Mask) {
    var isUpdating = false
    val mask = maskType.pattern
    var oldText = ""

    this.addTextChangedListener(
        onTextChanged = { _, count ->
            val cleanText = text.toString().unmaskOnlyNumbers()
            var newText = ""
            if (count == 0) isUpdating = true

            if (isUpdating) {
                oldText = cleanText
                isUpdating = false

                return@addTextChangedListener
            }

            newText = applyMask(mask, cleanText, oldText, newText)

            isUpdating = true
            this.setText(newText)
            this.setSelection(newText.length)
        }
    )
}

fun applyMask(mask: String, cleanText: String, oldText: String, newText: String): String {
    var textWithMask = newText
    var i = 0
    for (m: Char in mask.toCharArray()) {
        if (m != '#' && cleanText.length > oldText.length) {
            textWithMask += m
            continue
        }

        try {
            textWithMask += cleanText[i]
        } catch (exception: StringIndexOutOfBoundsException) {
            break
        }
        i++
    }
    return textWithMask
}

private inline fun EditText.addTextChangedListener(
    crossinline afterTextChanged: (Editable?) -> Unit = {},
    crossinline beforeTextChanged: (CharSequence?) -> Unit = {},
    crossinline onTextChanged: (CharSequence?, Int) -> Unit = { _, _ -> },
): TextWatcher {
    return object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            beforeTextChanged.invoke(s)
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChanged.invoke(s, count)
        }

        override fun afterTextChanged(s: Editable?) {
            afterTextChanged.invoke(s)
        }

    }.apply { this@addTextChangedListener.addTextChangedListener(this) }
}
