package com.example.bookcourt.utils

import android.util.LayoutDirection
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density

class TriangleEdgeShape(val offset: Int) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: androidx.compose.ui.unit.LayoutDirection,
        density: Density
    ): Outline {
        val trianglePath = Path().apply {
            moveTo(x = 0f, y = size.height-offset)
            lineTo(x = 0f, y = size.height)
            lineTo(x = 0f + offset, y = size.height)
        }
        return Outline.Generic(path = trianglePath)
    }
}