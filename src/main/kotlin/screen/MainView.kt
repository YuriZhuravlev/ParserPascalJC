package screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import screen.common.AppTheme
import screen.common.Settings
import screen.editor.Editors
import screen.filetree.FileTree
import screen.toolbar.Toolbar
import screen.toolbar.ToolbarView

@Composable
fun MainView() {
    val codeViewer = remember {
        val editors = Editors()

        CodeViewer(
            editors = editors,
            fileTree = FileTree(java.io.File(System.getProperty("user.dir")), editors),
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