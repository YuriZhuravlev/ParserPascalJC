package screen.add

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import screen.NAME_DIR
import java.io.File

class Add(val onAdd: () -> Unit) {
    fun commit(title: String, text: String, onSuccess: () -> Unit): Boolean {
        val file = File(NAME_DIR, title)
        if (file.exists()) {
            return false
        } else {
            GlobalScope.launch(Dispatchers.IO) {
                file.createNewFile()
                file.writeText(text)
                GlobalScope.launch(Dispatchers.Main) {
                    onAdd()
                    onSuccess()
                }
            }
        }
        return true
    }
}