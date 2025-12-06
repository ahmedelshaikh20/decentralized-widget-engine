package com.example.check24_android.presentation.ui.common

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.check24_android.presentation.ui.theme.AppButtonDefaults
import com.example.check24_android.presentation.ui.theme.BrandWhite
import com.example.check24_android.presentation.ui.theme.ButtonSecondary
import com.example.check24_android.presentation.ui.theme.Check24androidTheme

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(AppButtonDefaults.height),
        shape = RoundedCornerShape(AppButtonDefaults.cornerRadius),
        colors = ButtonDefaults.buttonColors(
            containerColor = ButtonSecondary,
            contentColor = BrandWhite
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge
        )
    }
}




@Preview(showBackground = true)
@Composable
fun SecondaryButtonPreview() {
  Check24androidTheme {
    SecondaryButton (text = "Button", onClick = {})
  }
}
