package screen.toolbar

import androidx.compose.desktop.Window
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.svgResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import loadImageResource
import screen.add.Add
import screen.add.AddView
import screen.common.AppTheme
import screen.edit.Edit
import screen.edit.EditView
import screen.info.Info

const val colorRun = 0xFF007F0E
const val colorEnable = 0xFFE5E5E5
const val colorDisable = 0xFF808080

@Composable
fun ToolbarView(model: Toolbar) {
    var clickableRunButton by remember { mutableStateOf(true) }
    var activeNotNull by remember { mutableStateOf(false) }
    var isChecked by remember { mutableStateOf(false) }

    activeNotNull = model.active != null
    Box(
        modifier = Modifier
            .height(44.dp)
            .fillMaxWidth()
            .padding(2.dp)
    ) {
        Row(modifier = Modifier.fillMaxHeight().align(Alignment.CenterStart)) {
            Column(modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 5.dp)
                .clickable(true) {
                    Window(
                        title = "Создание документа",
                        icon = loadImageResource("ic_app.png")
                    ) {
                        MaterialTheme(colors = AppTheme.colors.material) {
                            AddView(Add { path ->
                                model.onAdd(path)
                            })
                        }
                    }
                }) {
                Icon(
                    painter = svgResource("ic_add.svg"),
                    contentDescription = "Add",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    tint = Color(colorEnable))
                Text(text = "Создать",
                    color = Color(colorEnable),
                    fontSize = 10.sp
                )
            }

            Column(modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 5.dp)
                .clickable(activeNotNull && clickableRunButton) {
                    clickableRunButton = false
                    model.checkText(
                        {
                            isChecked = true
                            clickableRunButton = true
                        }, {
                            isChecked = true
                            clickableRunButton = true
                        }
                    )
                }) {
                Icon(
                    painter = svgResource("ic_run.svg"),
                    contentDescription = "Run",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    tint = if (activeNotNull && clickableRunButton) {
                        Color(colorRun)
                    } else {
                        Color(colorDisable)
                    })
                Text(text = "Проверить",
                    color = if (activeNotNull && clickableRunButton) {
                        Color(colorEnable)
                    } else {
                        Color(colorDisable)
                    },
                    fontSize = 10.sp
                )
            }

            Column(modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 5.dp)
                .clickable(activeNotNull) {
                    Window(
                        title = "Изменение документа",
                        icon = loadImageResource("ic_app.png")
                    ) {
                        MaterialTheme(colors = AppTheme.colors.material) {
                            EditView(Edit(model.active!!.filePath) {
                                model.onEdit()
                            })
                        }
                    }
                }) {
                Icon(
                    painter = svgResource("ic_edit.svg"),
                    contentDescription = "Edit",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    tint = if (activeNotNull) {
                        Color(colorEnable)
                    } else {
                        Color(colorDisable)
                    })
                Text(text = "Изменить",
                    color = if (activeNotNull) {
                        Color(colorEnable)
                    } else {
                        Color(colorDisable)
                    },
                    fontSize = 10.sp
                )
            }

            Text(
                text = if (isChecked) {
                    model.result
                } else {
                    ""
                },
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = 5.dp)
                    .fillMaxWidth(0.8f),
                color = Color.LightGray,
                fontSize = 14.sp,
                fontWeight = FontWeight.W700
            )
        }
        Row(modifier = Modifier.align(Alignment.CenterEnd)) {
            Column(modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 5.dp)
                .clickable(true) {
                    Window(
                        title = "Информация",
                        icon = loadImageResource("ic_app.png"),
                        size = IntSize(500, 500),
                        resizable = false
                    ) {
                        MaterialTheme(colors = AppTheme.colors.material) {
                            Info()
                        }
                    }
                }) {
                Icon(
                    painter = svgResource("ic_info.svg"),
                    contentDescription = "Info",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    tint = Color(colorEnable))
                Text(text = "О программе",
                    color = Color(colorEnable),
                    fontSize = 10.sp
                )
            }
        }
    }
}
