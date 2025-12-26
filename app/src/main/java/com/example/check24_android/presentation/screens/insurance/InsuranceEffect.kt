package com.example.check24_android.presentation.screens.insurance


sealed class InsuranceEffect {
  data class ShowMessage(val message: String) : InsuranceEffect()

}
