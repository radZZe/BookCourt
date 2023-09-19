package com.example.bookcourt.ui.recommendation

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeableDefaults
import androidx.compose.material.ThresholdConfig
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import android.content.Context
import android.util.Log
import android.view.WindowManager
import androidx.compose.animation.core.*
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.material.LocalAbsoluteElevation
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.*
import androidx.compose.ui.unit.Dp

open class BottomSheetController(
    val scope: CoroutineScope,
    private val screenWidth: Float,
    val screenHeight: Float,
) {
    var thresholdX = 0.0f
    var thresholdY = 0.0f

    var sheetHeight = Animatable(150f)
    var sheetPadding = Animatable(200f)

    var isExpanded = mutableStateOf(false)

    val center = Offset(0f, 0f)
    val top = Offset(0f, screenHeight)
    val startPosition = (screenHeight / 1.3).toFloat()
    val endPosition =screenHeight / 2000


    val endStartDifference = endPosition - startPosition
    val percentDifference = endStartDifference / 100


    val lastDragAmount = Animatable(0f)
    val offsetY = Animatable((screenHeight / 1.3).toFloat())
    val topBarPercentSize = Animatable(0f)
    val logoVisibility = Animatable(0f)
    val logoPercentSize = Animatable(0f)

    fun hide(){
        scope.apply {
            launch {
                isExpanded.value = false
                sheetHeight.animateTo(
                    targetValue = (150f),
                    animationSpec = tween(durationMillis = 200, easing = LinearEasing)
                )
            }
        }

    }

    fun expand(){
        scope.apply {
            launch{
                sheetHeight.animateTo(
                    targetValue = (screenHeight - 500f),
                    animationSpec = tween(durationMillis = 200, easing = LinearEasing)
                )
                isExpanded.value = true
            }
        }


    }




}

@Composable
fun rememberBottomSheetController(
    screenHeight: Float,
    screenWidth: Float
): BottomSheetController {
    val scope = rememberCoroutineScope()
    return remember {
        BottomSheetController(
            scope = scope,
            screenWidth = screenWidth,
            screenHeight = screenHeight,
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
fun Modifier.draggableBottomSheet(
    onExpanding:()->Unit,
    onCollapsing:()->Unit,
    controller: BottomSheetController,
    thresholdConfig: (Float, Float) -> ThresholdConfig,
): Modifier = composed {
    val density = LocalDensity.current
    val thresholds = { a: Float, b: Float ->
        with(thresholdConfig(a, b)) {
            density.computeThreshold(a, b)
        }
    }

    controller.thresholdY = thresholds(controller.center.y, 0f)
    Modifier.pointerInput(Unit) {

        detectDragGestures(
            onDrag = { change, dragAmount ->
                controller.scope.apply {
                    launch(Dispatchers.Default) {
                        if(dragAmount.y < 0){
                            controller.expand()
                            onExpanding()
                        }else if (dragAmount.y > 0){
                            onCollapsing()
                            controller.hide()
                        }
                    }
                }
                change.consume()
            },
            onDragEnd = {
                controller.scope.apply {
                    launch(Dispatchers.Default) {
                        if(controller.lastDragAmount.value < 0){
//                            controller.offsetY.animateTo(
//                                    targetValue = (controller.endPosition),
//                                    animationSpec = tween(durationMillis = 150, easing = LinearEasing)
//                                )
                        }else if (controller.lastDragAmount.value > 0){
//                            controller.offsetY.animateTo(
//                                targetValue = controller.startPosition,
//                                animationSpec = tween(durationMillis = 150, easing = LinearEasing)
//                            )
                        }
                    }
                }


            },
        )
    }
}

