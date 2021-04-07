package screen.toolbar

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import parser.ParserPascal
import screen.CodeViewer
import screen.NAME_DIR
import screen.filetree.FileTree
import java.io.File

class Toolbar(val codeViewer: CodeViewer) {
    private val success = "Принадлежит к подмножеству"
    private val failed = "Не принадлежит к подмножеству"
    private lateinit var mParser: ParserPascal
    val active = codeViewer.editors.active
    var result = ""

    fun checkText(onSuccess: () -> Unit, onFailure: () -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val text = File(codeViewer.editors.active!!.filePath).readText()
                mParser = ParserPascal(text)
                val res = mParser.parse()
                result = if (res) {
                    success
                } else {
                    failed
                }
                GlobalScope.launch(Dispatchers.Main) {
                    onSuccess()
                }
            } catch (e: Exception) {
                GlobalScope.launch(Dispatchers.Main) {
                    onFailure()
                }
                return@launch
            }
        }
    }

    fun onEdit() {
        codeViewer.editors.update(active!!.filePath)
    }

    fun onAdd(path: String) {
        codeViewer.fileTree = FileTree(File(NAME_DIR), codeViewer.editors)
        codeViewer.editors.open(File(path))
    }
}