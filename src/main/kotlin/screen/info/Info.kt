package screen.info

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import screen.common.AppTheme


@Composable
fun Info() {
    Column(
        Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundMedium)
            .padding(8.dp)
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            fontSize = 30.sp,
            fontWeight = FontWeight.W800,
            textAlign = TextAlign.Center,
            color = Color.White,
            text = "ParserPascalJC"
        )
        Text(
            modifier = Modifier.padding(4.dp),
            fontSize = 20.sp,
            textDecoration = TextDecoration.Underline,
            textAlign = TextAlign.Center,
            color = Color.LightGray.copy(0.8f),
            text = "https://github.com/YuriZhuravlev"
        )
        Text(
            modifier = Modifier.padding(2.dp),
            color = Color.LightGray,
            text = "Индивидуальный вариант задания (вариант 8)\n" +
                    "- program\n" +
                    "- var\n" +
                    "- const\n" +
                    "- begin\n" +
                    "- end\n" +
                    "- write\n" +
                    "- read\n" +
                    "- if\n" +
                    "- for\n" +
                    "- указатели\n" +
                    "- оператор присваивания\n" +
                    "- арифметические операции +|-|*|/\n" +
                    "- арифметические выражения\n" +
                    "- типы данных: Integer, Boolean\n"
        )
    }
}