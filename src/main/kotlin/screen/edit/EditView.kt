package screen.edit

import androidx.compose.desktop.AppFrame
import androidx.compose.desktop.AppManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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

    Box(Modifier.fillMaxSize()) {
        Column {
            TextField(
                value = text,
                onValueChange = { s: String -> text = s },
                modifier = Modifier.fillMaxWidth()
            )
            Button(onClick = {
                window = AppManager.focusedWindow!!
                model.commit(text) {
                    close = true
                }
            }) {
                Text("Commit")
            }
        }
    }
}