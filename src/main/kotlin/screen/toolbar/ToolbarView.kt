package screen.toolbar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.imageFromResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import loadImageResource

const val colorRun = 0xFF007F0E
const val colorEnable = 0xFFE5E5E5
const val colorDisable = 0xFF808080

@Composable
fun ToolbarView(model: Toolbar) {
    var clickableRunButton by remember { mutableStateOf(true) }

    var isChecked by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Row(modifier = Modifier.fillMaxHeight().align(Alignment.CenterStart)) {
            if (model.active != null) {
                Icon(
                    bitmap = imageFromResource("ic_run.png"),
                    contentDescription = "Run",
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(horizontal = 10.dp)
                        .clickable(clickableRunButton) {
                            clickableRunButton = false
                            model.checkText(
                                {
                                    isChecked = true
                                    clickableRunButton = true
                                }, {
                                    clickableRunButton = true
                                }
                            )
                        },
                    tint = if (clickableRunButton) {
                        Color(colorRun)
                    } else {
                        Color(colorDisable)
                    }
                )

                Icon(
                    bitmap = imageFromResource("ic_save.png"),
                    contentDescription = "Save",
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(horizontal = 5.dp)
                        .clickable(true) {
                            //model.save()
                        },
                    tint = Color(colorEnable)
                )

                Icon(
                    bitmap = imageFromResource("ic_edit.png"),
                    contentDescription = "Edit",
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(horizontal = 5.dp)
                        .clickable(true) {
                            //model.edit()
                        },
                    tint = Color(colorEnable)
                )

                Text(
                    text = if (isChecked) {model.result } else {""},
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(horizontal = 5.dp)
                        .fillMaxWidth(0.8f),
                    color = Color.LightGray,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W700
                )

                Icon(
                    bitmap = imageFromResource("ic_info.png"),
                    contentDescription = "Info",
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(horizontal = 5.dp)
                        .clickable(true) {
                        },
                    tint = Color(colorEnable)
                )
            }
        }
    }
}