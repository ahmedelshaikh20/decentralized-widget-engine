package com.example.check24_android.presentation.screens.insurance


sealed class InsuranceScreenEvents {

  data object NewQuoteClicked : InsuranceScreenEvents()
  data object SignClicked : InsuranceScreenEvents()
}
