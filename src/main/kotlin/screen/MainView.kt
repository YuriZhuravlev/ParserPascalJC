package screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import screen.common.AppTheme
import screen.common.Settings
import screen.editor.Editors
import screen.filetree.FileTree

/*@Composable
fun MainView() {
    val codeViewer = remember {
        val editors = Editors()

        CodeViewer(
            editors = editors,
            fileTree = FileTree(HomeFolder, editors),
            settings = Settings()
        )
    }
}*/

@Composable
fun MainView() {
    val codeViewer = remember {
        val editors = Editors()

        CodeViewer(
            editors = editors,
//            fileTree = FileTree(HomeFolder, editors),
            settings = Settings()
        )
    }

    // DisableSelection {
    MaterialTheme(
        colors = AppTheme.colors.material
    ) {
        CodeViewerView(codeViewer)
    }
}