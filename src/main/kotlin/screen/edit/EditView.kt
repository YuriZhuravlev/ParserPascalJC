package screen.edit

import androidx.compose.desktop.AppFrame
import androidx.compose.desktop.AppManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import screen.common.AppTheme

private var window: AppFrame? = null

@Composable
fun EditView(model: Edit) {
    var text by remember { mutableStateOf(model.getText()) }
    var close by remember { mutableStateOf(false) }

    if (close) {
        GlobalScope.launch(Dispatchers.Main) {
            window?.close()
        }
    }
    Box(
        Modifier.fillMaxSize()
            .background(AppTheme.colors.backgroundMedium)
            .padding(8.dp)
    ) {
        Column {
            Text(
                text = model.name,
                modifier = Modifier
                    .padding(vertical = 5.dp)
                    .requiredHeight(60.dp)
                    .fillMaxWidth(),
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.W700
            )
            TextField(
                value = text,
                onValueChange = { s: String -> text = s },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(Color.White)
            )
            Button(
                onClick = {
                    window = AppManager.focusedWindow!!
                    model.commit(text) {
                        close = true
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 4.dp)
                    .widthIn(120.dp, 200.dp)
                    .requiredHeight(40.dp),
            ) {
                Text("Подтвердить")
            }
        }
    }
}