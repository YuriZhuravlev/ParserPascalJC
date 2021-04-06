import androidx.compose.desktop.Window
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import screen.MainView

fun main() = Window(
    title = "Parser Pascal",
    icon = loadImageResource("ic_app.png"),
) {
    MainView()
}