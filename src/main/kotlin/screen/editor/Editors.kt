package screen.editor

import androidx.compose.runtime.mutableStateListOf
import util.SingleSelection
import java.io.File
import kotlin.math.min

class Editors {
    private val selection = SingleSelection()

    var editors = mutableStateListOf<Editor>()
        private set

    val active: Editor? get() = selection.selected as Editor?

    fun open(file: File) {
        var index: Int = -1
        editors.forEachIndexed { i, editor ->
            if (editor.filePath == file.path) {
                index = i
            }
        }
        if (index != -1) {
            selection.selected = editors[index]
        } else {
            val editor = createEditor(file)
            editor.selection = selection
            editor.close = {
                close(editor)
            }
            editors.add(editor)
            editor.activate()
        }
    }

    fun update(path: String) {
        var index: Int = -1
        editors.forEachIndexed { i, editor ->
            if (editor.filePath == path) {
                index = i
            }
        }
        if (index != -1) {
            editors.removeAt(index)
        }
        val editor = createEditor(File(path))
        editor.selection = selection
        editor.close = {
            close(editor)
        }
        editors.add(editor)
        editor.activate()
    }

    private fun close(editor: Editor) {
        val index = editors.indexOf(editor)
        editors.remove(editor)
        if (editor.isActive) {
            selection.selected = editors.getOrNull(min(index, editors.lastIndex))
        }
    }
}