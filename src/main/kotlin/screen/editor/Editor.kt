package screen.editor

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import readTextLines
import util.EmptyTextLines
import util.SingleSelection
import java.io.File

class Editor(
    val fileName: String,
    val filePath: String,
    val lines: (backgroundScope: CoroutineScope) -> Lines
) {
    var close: (() -> Unit)? = null
    lateinit var selection: SingleSelection

    val isActive: Boolean
        get() = selection.selected === this

    fun activate() {
        selection.selected = this
    }

    class Line(val number: Int, val content: Content)

    interface Lines {
        val lineNumberDigitCount: Int get() = size.toString().length
        val size: Int
        operator fun get(index: Int): Line
    }

    class Content(val value: State<String>, val isCode: Boolean)
}

fun createEditor(file: File) = Editor(fileName = file.name, filePath = file.path) { backgroundScope ->
    val textLines = try {
        file.readTextLines(backgroundScope)
    } catch (e: Throwable) {
        e.printStackTrace()
        EmptyTextLines
    }
    val isCode = file.name.endsWith(".pas", ignoreCase = true)

    fun content(index: Int): Editor.Content {
        val text = textLines.get(index)
            .trim('\n') // fix for native crash in Skia.

        // Workaround for another Skia problem with empty line layout.
        val state = mutableStateOf(if (text.isEmpty()) " " else text)
        return Editor.Content(state, isCode)
    }

    object : Editor.Lines {
        override val size get() = textLines.size

        override fun get(index: Int) = Editor.Line(
            number = index + 1,
            content = content(index)
        )
    }
}

