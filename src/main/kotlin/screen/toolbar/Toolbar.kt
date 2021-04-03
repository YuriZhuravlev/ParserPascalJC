package screen.toolbar

import androidx.compose.runtime.mutableStateOf
import screen.CodeViewer

class Toolbar(codeViewer: CodeViewer) {
    val active = codeViewer.editors.active
}