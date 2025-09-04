package com.studyh2g.composeoptimization.service

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

class LambdaParameterViewModel : ViewModel() {
    var color by mutableStateOf(Color.Red)
        private set

    var colorLambda by mutableStateOf(Color.Red)
        private set

    var colorRemember by mutableStateOf(Color.Red)
        private set

    fun changeColor(color: Color) {
        this.color = color
    }

    fun changeColorLambda(color: Color) {
        this.colorLambda = color
    }

    fun changeColorRemember(color: Color) {
        this.colorRemember = color
    }
}
