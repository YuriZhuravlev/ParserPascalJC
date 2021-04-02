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
        val editor = createEditor(file)
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