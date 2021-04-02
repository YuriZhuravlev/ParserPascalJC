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

private fun loadImageResource(path: String): BufferedImage {
    val resource = Thread.currentThread().contextClassLoader.getResource(path)
    requireNotNull(resource) { "Resource $path not found" }
    return resource.openStream().use(ImageIO::read)
}