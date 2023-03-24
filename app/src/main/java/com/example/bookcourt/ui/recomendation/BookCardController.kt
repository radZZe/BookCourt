package com.example.bookcourt.ui.recomendation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bookcourt.utils.DIRECTION_BOTTOM
import com.example.bookcourt.utils.DIRECTION_LEFT
import com.example.bookcourt.utils.DIRECTION_RIGHT
import com.example.bookcourt.utils.DIRECTION_TOP
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.sign

open class BookCardController(
    val scope: CoroutineScope,
    private val screenWidth: Float,
    private val screenHeight: Float,
    internal val animationSpec: AnimationSpec<Float> = SwipeableDefaults.AnimationSpec,
    val viewModel: CardStackViewModel
) {

    val right = Offset(screenWidth, 0f)
    val center = Offset(0f, 0f)
    val top = Offset(0f, screenHeight)

    var thresholdX = 0.0f
    var thresholdY = 0.0f

    val offsetX = Animatable(0f)
    val offsetY = Animatable(0f)
    val rotation = Animatable(0f)
    val scale = Animatable(0.8f)

    val limitValueY = 200
    val limitValueX = 100


    var onSwipeLeft: () -> Unit = {}
    var onSwipeRight: () -> Unit = {}
    var onSwipeUp: () -> Unit = {}
    var onSwipeDown: () -> Unit = {}

    fun swipeLeft() {
        scope.apply {
            launch {
                offsetX.animateTo(-screenWidth, animationSpec)
                onSwipeLeft()
                launch {
                    offsetX.snapTo(center.x)
                }
                launch {
                    offsetY.snapTo(0f)
                }
                launch {
                    rotation.snapTo(0f)
                }
                launch {
                    scale.snapTo(0.8f)
                }
            }
            launch {
                scale.animateTo(1f, animationSpec)
            }
        }
    }

    fun swipeRight() {
        scope.apply {
            launch {
                offsetX.animateTo(screenWidth, animationSpec)
                onSwipeRight()
                launch {
                    offsetX.snapTo(center.x)
                }
                launch {
                    offsetY.snapTo(0f)
                }
                launch {
                    scale.snapTo(0.8f)
                }
                launch {
                    rotation.snapTo(0f)
                }
            }
            launch {
                scale.animateTo(1f, animationSpec)
            }
        }
    }

    fun swipeUp() {
        scope.apply {
            launch {
                offsetY.animateTo(-screenHeight, animationSpec)
                onSwipeUp()
                launch {
                    offsetX.snapTo(0f)
                }
                launch {
                    offsetY.snapTo(center.y)
                }
                launch {
                    scale.snapTo(0.8f)
                }
                launch {
                    rotation.snapTo(0f)
                }
            }
            launch {
                scale.animateTo(1f, animationSpec)
            }
        }
    }

    fun swipeDown() {
        scope.apply {
            launch {
                offsetY.animateTo(screenHeight, animationSpec)
                onSwipeDown()
                launch {
                    offsetX.snapTo(0f)
                }
                launch {
                    offsetY.snapTo(center.y)
                }
                launch {
                    scale.snapTo(0.8f)
                }
                launch {
                    rotation.snapTo(0f)
                }
            }
            launch {
                scale.animateTo(1f, animationSpec)
            }
        }
    }

    fun returnCenter() {
        scope.apply {
            viewModel.changeDirection(null, viewModel.currentItem)
            launch {
                offsetX.animateTo(center.x, animationSpec)
            }
            launch {
                offsetY.animateTo(center.y, animationSpec)
            }
            launch {
                rotation.animateTo(0f, animationSpec)
            }
            launch {
                scale.animateTo(0.8f, animationSpec)
            }
        }
    }
}

@Composable
fun rememberBookCardController(
    animationSpec: AnimationSpec<Float> = SwipeableDefaults.AnimationSpec,
    viewModel: CardStackViewModel = hiltViewModel()
): BookCardController {
    val scope = rememberCoroutineScope()
    val screenWidth = with(LocalDensity.current) {
        LocalConfiguration.current.screenWidthDp.dp.toPx()
    }
    val screenHeight = with(LocalDensity.current) {
        LocalConfiguration.current.screenHeightDp.dp.toPx()
    }

    return remember {
        BookCardController(
            scope = scope,
            screenWidth = screenWidth,
            screenHeight = screenHeight,
            animationSpec = animationSpec,
            viewModel
        )
    }
}



@OptIn(ExperimentalMaterialApi::class)
fun Modifier.draggableStack(
    controller: BookCardController,
    thresholdConfig: (Float, Float) -> ThresholdConfig,
): Modifier = composed {

    val density = LocalDensity.current
    val thresholds = { a: Float, b: Float ->
        with(thresholdConfig(a, b)) {
            density.computeThreshold(a, b)
        }
    }

    controller.thresholdX = thresholds(controller.center.x, controller.right.x)
    controller.thresholdY = thresholds(controller.center.y, controller.top.y)

    Modifier.pointerInput(Unit) {
        detectDragGestures(
            onDragEnd = {
                if(controller.offsetY.value == controller.center.y && controller.offsetX.value == controller.center.x){
                    controller.viewModel.changeDirection(
                        null,
                        controller.viewModel.currentItem
                    )
                }

                if (isTopShift(controller)) {
                    controller.swipeUp()

                } else if (isBottomShift(controller)) {
                    controller.swipeDown()

                } else if (isLeftShift(controller)) {
                    controller.swipeLeft()

                } else if (isRightShift(controller)) {
                    controller.swipeRight()

                } else {
                    controller.returnCenter()
                }

            },
            onDrag = { change, dragAmount ->
                controller.scope.apply {
                    launch(Dispatchers.Default) {
                        if(isShiftByX(controller,dragAmount)){
                            controller.offsetX.snapTo(controller.offsetX.value + dragAmount.x)
                        }else{
                            controller.offsetY.snapTo(controller.offsetY.value + dragAmount.y)
                        }

                        if (isTopShift(controller)) {
                            drawShift(controller, DIRECTION_TOP)
                        } else if (isBottomShift(controller)) {
                            drawShift(controller, DIRECTION_BOTTOM)
                        } else if (isLeftShift(controller)) {
                            drawShift(controller, DIRECTION_LEFT)
                        } else if (isRightShift(controller)) {
                            drawShift(controller, DIRECTION_RIGHT)
                        } else  {
                            drawShift(controller, null)
                        }

                        val targetRotation = normalize(
                            controller.center.x,
                            controller.right.x,
                            abs(controller.offsetX.value),
                            0f,
                            10f
                        )


                        controller.rotation.snapTo(targetRotation * -controller.offsetX.value.sign)

                        controller.scale.snapTo(
                            normalize(
                                controller.center.x,
                                controller.right.x / 3,
                                abs(controller.offsetX.value),
                                0.8f
                            )
                        )
                    }
                }
                change.consume()
            },
            onDragCancel = {
                controller.returnCenter()
            },
        )
    }
}

fun isTopShift(controller: BookCardController):Boolean{
    return controller.offsetY.value <= -controller.thresholdY+controller.limitValueY
}

fun isBottomShift(controller: BookCardController):Boolean{
    return controller.offsetY.value >= controller.thresholdY-controller.limitValueY
}

fun isLeftShift(controller: BookCardController):Boolean{
 return controller.offsetX.value <= -controller.thresholdX+controller.limitValueX
}

fun isRightShift(controller: BookCardController):Boolean{
    return controller.offsetX.value >= controller.thresholdX-controller.limitValueX
}
fun isShiftByX(controller: BookCardController, dragAmount:Offset):Boolean{
    var percentShiftX = abs(controller.offsetX.value)/(controller.right.x/100)
    var percentShiftY = abs(controller.offsetY.value)/(controller.top.y/100)
    return (percentShiftX>=percentShiftY) && abs(dragAmount.x) > abs(dragAmount.y)
            ||((percentShiftX>percentShiftY) && abs(dragAmount.x) < abs(dragAmount.y))
}

fun drawShift(controller: BookCardController, direction:String?){
    controller.viewModel.changeDirection(
        direction,
        controller.viewModel.currentItem
    )
}



private fun normalize(
    min: Float,
    max: Float,
    v: Float,
    startRange: Float = 0f,
    endRange: Float = 1f
): Float {
    require(startRange < endRange) {
        "Start range is greater than end range"
    }

    val value = v.coerceIn(min, max)

    return (value - min) / (max - min) * (endRange + startRange) + startRange
}