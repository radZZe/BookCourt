package com.example.bookcourt.ui.verification

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookcourt.data.repositories.DataStoreRepository
import com.example.bookcourt.data.repositories.MetricsRepository
import com.example.bookcourt.data.room.user.UserRepositoryI
import com.example.bookcourt.models.metrics.DataClickMetric
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerificationCodeViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val userRepositoryI: UserRepositoryI,
    private val metricRep: MetricsRepository,
) : ViewModel() {
    var userEmail = mutableStateOf("")

    val textList = listOf(
        mutableStateOf(
            TextFieldValue(
                text = "",
                selection = TextRange(0)
            )
        ),
        mutableStateOf(
            TextFieldValue(
                text = "",
                selection = TextRange(0)
            )
        ),
        mutableStateOf(
            TextFieldValue(
                text = "",
                selection = TextRange(0)
            )
        ),
        mutableStateOf(
            TextFieldValue(
                text = "",
                selection = TextRange(0)
            )
        )
    )

    val requestList = listOf(
        FocusRequester(),
        FocusRequester(),
        FocusRequester(),
        FocusRequester()
    )

    private val _isOver = MutableStateFlow(false)
    val isOver = _isOver.asStateFlow()

    private val _isValid: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val isValid = _isValid.asStateFlow()

    private val _timer = MutableStateFlow(59)
    val timer = _timer.asStateFlow()

    private var isCodeVerified by mutableStateOf(false)

    init {
        viewModelScope.launch {
            userEmail.value = getUserEmail()
            countDownTimer()
        }
    }

    private suspend fun editPrefs() {
        dataStoreRepository.setPref(!isCodeVerified, DataStoreRepository.isCodeVerificated)
    }

    private suspend fun getUserEmail(): String {
        val userId = dataStoreRepository.getPref(DataStoreRepository.uuid)
        return userRepositoryI.loadData(userId.first())!!.email
    }

    private suspend fun countDownTimer() {
        while (true) {
            delay(1000)
            _timer.value--
            if (_timer.value == 0) {
                _isOver.update { true }
            }
        }
    }

    fun resendCode() {
        _timer.value = 59
        _isOver.update { false }
    }

    fun nextFocus(
        textList: List<MutableState<TextFieldValue>>,
        requestList: List<FocusRequester>
    ) {
        for (i in textList.indices) {
            if (textList[i].value.text == "") {
                if (i < textList.size) {
                    requestList[i].requestFocus()
                    break
                }
            }
        }
    }

    fun connectInputtedCode(
        textList: List<MutableState<TextFieldValue>>,
        onVerifyCode: ((success: Boolean) -> Unit)? = null
    ) {
        var code = ""
        for (text in textList) {
            code += text.value.text
        }
        if (code.length == 4) {
            verifyCode(
                code,
                onSuccess = {
                    _isValid.update { true }
                    onVerifyCode?.let {
                        it(true)
                    }
                },
                onError = {
                    _isValid.update { false }
                    onVerifyCode?.let {
                        it(false)
                    }
                }
            )
        }
    }

    private fun verifyCode(
        code: String,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        if (code == "1234") { // Test code
            onSuccess()
        } else {
            onError()
        }
    }

    fun metricClick(clickMetric: DataClickMetric) {
        viewModelScope.launch(Dispatchers.IO) {
            editPrefs()
            metricRep.onClick(clickMetric)
        }
    }
}