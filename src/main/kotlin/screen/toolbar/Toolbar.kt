package screen.toolbar

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import parser.ParserPascal
import screen.CodeViewer
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
                val text = File("build/test.pas").readText()
                //val text = File(codeViewer.editors.active!!.fileName).readText()
                mParser = ParserPascal(text)
                val res = mParser.parse()
                if (res) {
                    result = success
                } else {
                    result = failed
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
}