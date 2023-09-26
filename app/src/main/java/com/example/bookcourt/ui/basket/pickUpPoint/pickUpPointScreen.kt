package com.example.bookcourt.ui.basket.pickUpPoint

import android.app.ActionBar.LayoutParams
import android.util.DisplayMetrics
import android.util.Log
import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bookcourt.R
import com.example.bookcourt.ui.basket.successPurchase.OrderWidget
import com.example.bookcourt.ui.theme.GrayBackground
import com.example.bookcourt.ui.theme.MainBgColor
import com.example.bookcourt.ui.theme.Roboto
import com.example.bookcourt.utils.CustomButton
import com.example.bookcourt.utils.ReturnTopBar
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView

@Composable
fun PickUpPointScreen(viewModel: pickUpPointViewModel = hiltViewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MainBgColor)
            .onGloballyPositioned {
                viewModel.mapSize.value = it.size
            }
    ){
        ReturnTopBar(curScreenName = "Адрес доставки") {}
        if (viewModel.mapSize.value.height>0&&viewModel.mapSize.value.width>0){
            AndroidView(
                modifier = Modifier
                    .fillMaxSize(),
                factory = { context->
                     MapView(context)

                },
                update = { mapView->

                    mapView.layoutParams.height = viewModel.mapSize.value.height
                    mapView.layoutParams.width = viewModel.mapSize.value.width
                    mapView.map.move(
                        CameraPosition(
                            Point(55.751574, 37.573856),
                            11.0f,
                            0.0f,
                            0.0f),
                        Animation(Animation.Type.SMOOTH, 5f),
                        null
                    )
                }
            )
        }

    }
}
