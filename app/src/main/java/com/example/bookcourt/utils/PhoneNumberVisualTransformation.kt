package com.example.bookcourt.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

const val mask = "+7 (xxx) xxx-xx-xx"
class PhoneNumberVisualTransformation:VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed =
            if (text.text.length >= 12) text.text.substring(0..11) else text.text

        val annotatedString = AnnotatedString.Builder().run {
            for (i in trimmed.indices) {
                append(trimmed[i])
                if (i == 1) {
                    append(" (")
                }
                if (i==4){
                    append(") ")
                }
                if (i==7||i==9){
                    append("-")
                }
            }
            pushStyle(SpanStyle(color = Color.LightGray))
            append(mask.takeLast(mask.length - length))
            toAnnotatedString()
        }

        val phoneNumberOffsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 1) return offset
                if (offset <= 4) return offset + 2
                if (offset <= 7) return offset + 4
                if (offset <= 9) return offset + 5
                if (offset <= 12) return offset + 6
                return 18
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 1) return offset
                if (offset <= 4) return offset - 2
                if (offset <= 7) return offset - 4
                if (offset <= 9) return offset - 5
                if (offset <= 12) return offset - 6
                return 12
            }
        }
        return TransformedText(annotatedString, phoneNumberOffsetTranslator)
    }
}