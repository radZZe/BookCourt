package com.example.bookcourt.ui.categorySelection

import androidx.compose.animation.*
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bookcourt.models.metrics.DataClickMetric
import com.example.bookcourt.ui.theme.LightYellowBtn
import com.example.bookcourt.ui.theme.MainBgColor
import com.example.bookcourt.utils.*
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun CategorySelectionScreen(
    onNavigateToBottomNav:()->Unit,
    viewModel: CategorySelectionViewModel = hiltViewModel(),
) {
    val windowInfo = rememberWindowSizeClass()
    Box(modifier = Modifier
        .fillMaxSize()
        .background(MainBgColor),
    contentAlignment = Alignment.Center) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            HeaderCategoryScreen(windowInfo.screenHeightInfo)
            Spacer(modifier = Modifier.height(24.dp))
            CategoriesGrid(viewModel, windowInfo.screenHeightInfo)
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = if (windowInfo.screenHeightInfo == WindowInfo.WindowType.Compact) 10.dp else 20.dp)
        ) {
            NextButton(viewModel, windowInfo.screenHeightInfo,onNavigateToBottomNav)
        }

    }
}

@Composable
fun CategoriesGrid(
    viewModel: CategorySelectionViewModel,
    windowType: WindowInfo.WindowType
) {
    var data = viewModel.categories
    FlowRow(
        modifier = Modifier
            .fillMaxWidth(),
        mainAxisSpacing = 8.dp,
        crossAxisSpacing = 10.dp,
    ) {
        data.forEachIndexed { index, item ->
            CategoryItem(
                text = item.value.title,
                windowType = windowType,
                isChecked = item.value.state.value
            ) {
                viewModel.changeStateCategory(index)
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CategoryItem(
    text: String,
    windowType: WindowInfo.WindowType,
    isChecked: Boolean = false,
    onClick: () -> Unit?
) {
    AnimatedContent(targetState = isChecked,
        transitionSpec = {
            fadeIn(animationSpec = tween(durationMillis = 150)) with
                    fadeOut(animationSpec = tween(durationMillis = 150)) using
                    SizeTransform { initialSize, targetSize ->
                        if (targetState) {
                            keyframes {
                                IntSize(initialSize.width, initialSize.height) at 150
                                durationMillis = 300
                            }
                        } else {
                            keyframes {
                                IntSize(targetSize.width, targetSize.height) at 150
                                durationMillis = 300
                            }
                        }
                    }
        },
        modifier = Modifier.clickable {
            onClick()
        }) { state ->

        val fontSize = if (windowType == WindowInfo.WindowType.Compact) 14 else 15
        val rowPadding = mutableMapOf<String, Int>()
        if (windowType == WindowInfo.WindowType.Compact) {
            rowPadding.put("start", 14)
            rowPadding.put("end", 14)
            rowPadding.put("top", 3)
            rowPadding.put("bottom", 3)
        } else {
            rowPadding.put("start", 16)
            rowPadding.put("end", 16)
            rowPadding.put("top", 5)
            rowPadding.put("bottom", 5)
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(25.dp))
                .background(if (state) Color(57, 57, 57) else Color(134, 134, 134)),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .padding(
                        start = rowPadding["start"]!!.dp,
                        end = rowPadding["end"]!!.dp,
                        top = rowPadding["top"]!!.dp,
                        bottom = rowPadding["bottom"]!!.dp
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row() {
                    Text(
                        text = text,
                        color = Color.White,
                        fontFamily = FontFamily(
                            Font(
                                com.example.bookcourt.R.font.roboto_regular,
                            )
                        ),
                        fontSize = fontSize.sp
                    )
                }
            }
        }
    }
}

@Composable
fun NextButton(viewModel: CategorySelectionViewModel, windowType: WindowInfo.WindowType,onNavigateToBottomNav:()->Unit) {
    if (viewModel.selectedCategories.size > 0) {

        CustomButton(
            text = "Продолжить",
            color = LightYellowBtn,
            textColor = Color.Black,
            modifier = Modifier.padding(horizontal = 20.dp),
            onCLick = {
                viewModel.metricClick(
                    DataClickMetric(Buttons.CATEGORIES_SELECTION, Screens.CategorySelection.route)
                )
                onNavigateToBottomNav()
            }
        )

    } else {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {

        }
    }

}

@Composable
fun HeaderCategoryScreen(windowType: WindowInfo.WindowType) {
    Column(Modifier.fillMaxWidth()) {
        Text(text = "Какие книги Вам нравятся?",
            fontFamily = FontFamily(
                Font(
                    com.example.bookcourt.R.font.roboto_bold,
                )
            ),
            fontSize = 22.sp)
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Выберите от одного до пяти жанров, чтобы мы могли подобрать книги именно для вас.",
            fontFamily = FontFamily(
                Font(
                    com.example.bookcourt.R.font.roboto_regular,
                )
            ),
            fontSize = 16.sp)
    }
}

//@Preview(showBackground = true)
//@Composable
//fun CategorySelectionScreenPreview(){
//    CategorySelectionScreen()
//}