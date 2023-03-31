package com.example.bookcourt.ui.recomendation

import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeableDefaults
import androidx.compose.material.ThresholdConfig
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
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
import kotlinx.coroutines.async
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
    val visibility_first = Animatable(1f)


    val likeIconSize = Animatable(20.0f)
    val dislikeIconSize = Animatable(20.0f)
    val wantToReadIconSize = Animatable(20.0f)
    val skipBookIconSize = Animatable(20.0f)


    var baseIconColor = Color(222, 210, 169)

    var likeIconAlpha = Animatable(0f)
    var currentLikeIconColor = Color(red = 252, 87, 5)

    var dislikeIconAlpha = Animatable(0f)
    var currentDislikeIconColor = Color(red = 252, 87, 5)

    var wantToReadIconAlpha = Animatable(0f)
    var currentWantToReadIconColor = Color(red = 243, 183, 29)

    var skipIconAlpha = Animatable(0f)
    var currentSkipIconColor = Color(red = 134, 134, 134)


    var onSwipeLeft: () -> Unit = {}
    var onSwipeRight: () -> Unit = {}
    var onSwipeUp: () -> Unit = {}
    var onSwipeDown: () -> Unit = {}

    fun swipeLeft() {
        scope.apply {
            launch {

                offsetX.animateTo(-screenWidth, animationSpec)
                visibility_first.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(durationMillis = 200, easing = LinearEasing)
                )
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
                    visibility_first.snapTo(1f)
                }
                launch {
                    dislikeIconSize.snapTo(20.0f)
                }
                launch {
                    dislikeIconAlpha.snapTo(0f)
                }

            }
        }
    }

    fun swipeRight() {
        scope.apply {
            launch {

                offsetX.animateTo(screenWidth, animationSpec)
                visibility_first.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(durationMillis = 200, easing = LinearEasing)
                )
                onSwipeRight()
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
                    visibility_first.snapTo(1f)
                }
                launch {
                    likeIconSize.animateTo(
                        targetValue = 20f,
                        animationSpec = tween(durationMillis = 200, easing = LinearEasing)
                    )
                }
                launch {
                    likeIconAlpha.snapTo(0f)
                }
            }
        }
    }

    fun swipeUp() {
        scope.apply {
            launch {

                offsetY.animateTo(-screenHeight, animationSpec)
                visibility_first.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(durationMillis = 200, easing = LinearEasing)
                )
                onSwipeUp()
                launch {
                    offsetX.snapTo(0f)
                }
                launch {
                    offsetY.snapTo(center.y)
                }
                launch {
                    rotation.snapTo(0f)
                }
                launch {
                    visibility_first.snapTo(1f)
                }
                launch {
                    wantToReadIconSize.snapTo(20.0f)
                }
                launch {
                    wantToReadIconAlpha.snapTo(0f)
                }

            }
        }
    }

    fun swipeDown() {
        scope.apply {
            launch {
                offsetY.animateTo(screenHeight, animationSpec)
                visibility_first.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(durationMillis = 200, easing = LinearEasing)
                )
                onSwipeDown()
                launch {
                    offsetX.snapTo(0f)
                }
                launch {
                    offsetY.snapTo(center.y)
                }
                launch {
                    rotation.snapTo(0f)
                }
                launch {
                    visibility_first.snapTo(1f)
                }
                launch {
                    skipBookIconSize.snapTo(20.0f)
                }
                launch {
                    skipIconAlpha.snapTo(
                        0f
                    )
                }

            }
        }
    }

    fun returnCenter() {
        scope.apply {
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
                visibility_first.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing)
                )
            }

            launch {
                likeIconSize.snapTo(20.0f)
                wantToReadIconSize.snapTo(20.0f)
                skipBookIconSize.snapTo(20.0f)
                dislikeIconSize.snapTo(20.0f)
                likeIconAlpha.snapTo(0f)
                dislikeIconAlpha.snapTo(0f)
                wantToReadIconAlpha.snapTo(0f)
                skipIconAlpha.snapTo(0f)
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

                if (isTopShift(controller)) {
                    controller.scope.apply {
                        launch(Dispatchers.Default) {
                            iconPulseAnimation(
                                controller.wantToReadIconAlpha,
                                controller.wantToReadIconSize
                            )
                        }
                    }
                    controller.swipeUp()

                } else if (isBottomShift(controller)) {
                    controller.scope.apply {
                        launch(Dispatchers.Default) {
                            iconPulseAnimation(
                                controller.skipIconAlpha,
                                controller.skipBookIconSize
                            )
                        }
                    }
                    controller.swipeDown()

                } else if (isLeftShift(controller)) {
                    controller.scope.apply {
                        launch(Dispatchers.Default) {
                            iconPulseAnimation(
                                controller.dislikeIconAlpha,
                                controller.dislikeIconSize
                            )
                        }
                    }
                    controller.swipeLeft()

                } else if (isRightShift(controller)) {
                    controller.scope.apply {
                        launch(Dispatchers.Default) {
                            iconPulseAnimation(controller.likeIconAlpha, controller.likeIconSize)
                        }
                    }
                    controller.swipeRight()

                } else {
                    controller.returnCenter()
                }

            },
            onDrag = { change, dragAmount ->
                controller.scope.apply {
                    launch(Dispatchers.Default) {
                        var percentShiftY =
                            controller.offsetY.value / (controller.top.y / 100)
                        var percentShiftX =
                            controller.offsetX.value / (controller.right.x / 100)
                        if (isShiftByX(controller, dragAmount)) {
                            val targetChange = normalize(
                                controller.center.x,
                                controller.right.x,
                                abs(controller.offsetX.value),
                                0f,
                                10f
                            )
                            drawIconByShiftX(percentShiftX,controller)
                            controller.rotation.snapTo(targetChange * controller.offsetX.value.sign)
                            controller.visibility_first.snapTo(1 - (abs(percentShiftX) / 200))
                            controller.offsetX.snapTo(controller.offsetX.value + dragAmount.x)
                        } else {
                            drawIconByShiftY(percentShiftY,controller)
                            controller.visibility_first.snapTo(1 - (abs(percentShiftY) / 200))
                            controller.offsetY.snapTo(controller.offsetY.value + dragAmount.y)
                        }

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

fun isTopShift(controller: BookCardController): Boolean {
    return controller.offsetY.value <= -controller.thresholdY
}

fun isBottomShift(controller: BookCardController): Boolean {
    return controller.offsetY.value >= controller.thresholdY
}

fun isLeftShift(controller: BookCardController): Boolean {
    return controller.offsetX.value <= -controller.thresholdX
}

fun isRightShift(controller: BookCardController): Boolean {
    return controller.offsetX.value >= controller.thresholdX
}

fun isShiftByX(controller: BookCardController, dragAmount: Offset): Boolean {
    var percentShiftX = abs(controller.offsetX.value) / (controller.right.x / 100)
    var percentShiftY = abs(controller.offsetY.value) / (controller.top.y / 100)
    return (percentShiftX >= percentShiftY) && abs(dragAmount.x) > abs(dragAmount.y)
            || ((percentShiftX > percentShiftY) && abs(dragAmount.x) < abs(dragAmount.y))
}




private fun normalize(
    min: Float,
    max: Float,
    v: Float,
    startRange: Float = 0f,
    endRange: Float = 1f
): Float {
    val value = v.coerceIn(min, max)
    return (value - min) / (max - min) * (endRange + startRange) + startRange
}

suspend private fun iconPulseAnimation(
    alpha: Animatable<Float, AnimationVector1D>,
    size: Animatable<Float, AnimationVector1D>
) {
    alpha.animateTo(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 50, easing = LinearEasing)
    )
    size.animateTo(
        targetValue = 55f,
        animationSpec = tween(durationMillis = 50, easing = LinearEasing)
    )
}

suspend private fun iconChanges(
    alpha: Animatable<Float, AnimationVector1D>,
    size: Animatable<Float, AnimationVector1D>,
    sizeTo: Float,
    alphaTo: Float
) {
    size.snapTo(sizeTo)
    alpha.snapTo(alphaTo)
}
suspend fun drawIconByShiftX(value:Float,controller: BookCardController){
    if(value < 0){
        iconChanges(
            controller.dislikeIconAlpha,
            controller.dislikeIconSize,
            (20.0 + 0.55 * abs(value)).toFloat(),
            abs(value) / 50
        )
    }else{
        iconChanges(
            controller.likeIconAlpha,
            controller.likeIconSize,
            (20.0 + (55 * value)/100).toFloat(),
            value / 50
        )
    }
}
suspend fun drawIconByShiftY(value:Float,controller: BookCardController){
    if(value < 0){
        iconChanges(
            controller.wantToReadIconAlpha,
            controller.wantToReadIconSize,
            (20.0 + 0.55 * abs(value)).toFloat(),
            abs(value) / 50
        )
    }else{
        iconChanges(
            controller.skipIconAlpha,
            controller.skipBookIconSize,
            (20.0 + 0.55 * value).toFloat(),
            value / 50
        )
    }
}