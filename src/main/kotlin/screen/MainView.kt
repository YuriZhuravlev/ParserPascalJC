package screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import children
import screen.common.AppTheme
import screen.common.Settings
import screen.editor.Editors
import screen.filetree.FileTree
import screen.toolbar.Toolbar
import screen.toolbar.ToolbarView
import java.io.File

const val NAME_DIR = "PascalTests"

@Composable
fun MainView() {
    val codeViewer = remember {
        val editors = Editors()

        val file = File(NAME_DIR)
        if (!file.exists()) {
            file.mkdir()
        }

        CodeViewer(
            editors = editors,
            fileTree = FileTree(file, editors),
            settings = Settings()
        )
    }

    // DisableSelection {
    MaterialTheme(
        colors = AppTheme.colors.material
    ) {
        Column(Modifier.fillMaxSize().background(AppTheme.colors.backgroundMedium)) {
            ToolbarView(Toolbar(codeViewer))
            CodeViewerView(codeViewer)
        }
    }
}