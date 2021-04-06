package screen.add

import androidx.compose.desktop.AppFrame
import androidx.compose.desktop.AppManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private var window: AppFrame? = null

@Composable
fun AddView(model: Add) {
    var title by remember { mutableStateOf("") }
    var text by remember { mutableStateOf("") }
    var close by remember { mutableStateOf(false) }
    var incorrectName by remember { mutableStateOf(false) }

    if (close) {
        GlobalScope.launch(Dispatchers.Main) {
            window?.close()
        }
    }

    Box(Modifier.fillMaxSize()) {
        Column {
            TextField(
                value = title,
                onValueChange = { s: String -> title = s },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = incorrectName,
                label = { Text("Имя файла") }
            )
            TextField(
                value = text,
                onValueChange = { s: String -> text = s },
                modifier = Modifier.fillMaxWidth()
            )
            Button(onClick = {
                window = AppManager.focusedWindow!!
                incorrectName = !model.commit(title, text) { close = true }
            }) {
                Text("Commit")
            }
        }
    }
}