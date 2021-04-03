package screen.toolbar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ToolbarView(model: Toolbar) {
    var textButton by remember { mutableStateOf("Проверить") }
    Box(
        modifier = Modifier
            .height(36.dp)
            .fillMaxWidth()
            .padding(2.dp)
    ) {
        Row(modifier = Modifier.fillMaxHeight().align(Alignment.CenterStart)) {
            if (model.active != null) {

                Text(
                    text = textButton,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(horizontal = 10.dp)
                        .clickable(true) {
                            textButton = "Проверка"
                        },
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
        }
    }
}