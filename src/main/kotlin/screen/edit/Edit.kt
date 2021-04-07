package screen.edit

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import screen.NAME_DIR
import java.io.File

class Edit(val path: String, val onEdit: () -> Unit) {
    val name = path.replace(NAME_DIR + '\\', "")

    fun getText(): String {
        val file = File(path)
        return if (file.exists()) {
            file.readText()
        } else {
            ""
        }
    }

    fun commit(text: String, onSuccess: () -> Unit) {
        val file = File(path)
        GlobalScope.launch(Dispatchers.IO) {
            try {
                file.createNewFile()
                file.writeText(text)
                GlobalScope.launch(Dispatchers.Main) {
                    onEdit()
                    onSuccess()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}