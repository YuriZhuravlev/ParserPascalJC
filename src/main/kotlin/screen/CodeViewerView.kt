package screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp

@Composable
fun CodeViewerView(model: CodeViewer) {
    val panelState = remember { PanelState() }

//    val animatedSize = if (panelState.splitter)
    //VerticalSplittable(
}

private class PanelState {
    val collapsedSize = 24.dp
    var expandedSize by mutableStateOf(300.dp)
    val expandedSizeMin = 90.dp
    var isExpanded by mutableStateOf(true)
    // TODO
    // val splitter = SplitterState()
}