package com.example.bookcourt.ui.basket.orderingScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun  PickUpDialog(
    onDismiss:()->Unit,
    onConfirm:()->Unit,
   viewModel: OrderingScreenViewModel = hiltViewModel()
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
            ,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Введите адрес")
            TextField(
                value = viewModel.regionName.value,
                onValueChange = {
                    viewModel.regionName.value = it
                },
                placeholder = {
                    Text(text = "Регион")
                },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = viewModel.cityName.value,
                onValueChange = {
                    viewModel.cityName.value = it
                },
                placeholder = {
                    Text(text = "Город")
                },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = viewModel.address.value,
                onValueChange = {
                    viewModel.address.value = it
                },
                placeholder = {
                    Text(text = "Адрес")
                },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = viewModel.postOfficeIndex.value,
                onValueChange = {
                    viewModel.postOfficeIndex.value = it
                },
                placeholder = {
                    Text(text = "Индекс почтового отделения")
                },
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = onConfirm) {
                    Text(text = "confirm")
                }
                Button(onClick = onDismiss) {
                    Text(text = "cancel")
                }
            }
        }
    }
}