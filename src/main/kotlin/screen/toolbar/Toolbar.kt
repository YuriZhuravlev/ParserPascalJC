package screen.toolbar

import screen.CodeViewer

class Toolbar(val codeViewer: CodeViewer) {
    private val success = "Ах да, я ж нихуя не сделал анализатор"
    private val failed = "Я нихуя не сделал, но походу ты пытаешься наебать меня (файл не .pas)"
    val active = codeViewer.editors.active
    var result = ""

    fun checkText(onSuccess: () -> Unit, onFailure: () -> Unit) {
        //TODO
        if (codeViewer.editors.active?.fileName?.endsWith(".pas") == true) {
            result = success
            onSuccess()
        } else {
            result = failed
            onSuccess()
        }
    }
}