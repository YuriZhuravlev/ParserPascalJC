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
import androidx.compose.ui.graphics.imageFromResource
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
    var isChecked by remember { mutableStateOf(false) }

    clickableRunButton = model.active != null
    Box(
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Row(modifier = Modifier.fillMaxHeight().align(Alignment.CenterStart)) {
            Row(modifier = Modifier.weight(5f)) {
                Icon(
                    bitmap = imageFromResource("ic_add.png"),
                    contentDescription = "Add",
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(horizontal = 5.dp)
                        .clickable(true) {
                            Window(
                                title = "Создание документа",
                                icon = loadImageResource("ic_app.png")
                            ) {
                                MaterialTheme(colors = AppTheme.colors.material) {
                                    AddView(Add {
                                        model.onAdd()
                                    })
                                }
                            }
                        },
                    tint = Color(colorEnable)
                )

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
                    bitmap = imageFromResource("ic_edit.png"),
                    contentDescription = "Edit",
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(horizontal = 5.dp)
                        .clickable(clickableRunButton) {
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
                        },
                    tint = if (clickableRunButton) {
                        Color(colorEnable)
                    } else {
                        Color(colorDisable)
                    }
                )

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
            Row(modifier = Modifier.weight(1f)) {
                Icon(
                    bitmap = imageFromResource("ic_info.png"),
                    contentDescription = "Info",
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
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
                        },
                    tint = Color(colorEnable)
                )
            }
        }
    }
}
