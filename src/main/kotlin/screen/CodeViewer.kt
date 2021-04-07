package screen

import screen.common.Settings
import screen.editor.Editors
import screen.filetree.FileTree

class CodeViewer (
    val editors: Editors,
    var fileTree: FileTree,
    val settings: Settings
)