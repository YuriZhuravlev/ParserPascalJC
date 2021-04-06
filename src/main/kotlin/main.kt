import androidx.compose.desktop.Window
import androidx.compose.ui.unit.IntSize
import screen.MainView

fun main() = Window(
    title = "Parser Pascal",
    icon = loadImageResource("ic_app.png"),
    size = IntSize(1024, 768)
) {
    MainView()
}