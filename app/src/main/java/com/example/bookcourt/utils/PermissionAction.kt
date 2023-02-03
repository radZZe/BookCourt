package com.example.bookcourt.utils

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState


@OptIn(ExperimentalPermissionsApi::class)
fun PermissionState.isPermanentlyDenied() : Boolean {
    return !hasPermission  && !shouldShowRationale
}