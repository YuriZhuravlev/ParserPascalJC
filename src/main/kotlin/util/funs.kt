import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import util.TextLines
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.RandomAccessFile
import java.nio.channels.FileChannel
import java.nio.charset.StandardCharsets
import javax.imageio.ImageIO


fun loadImageResource(path: String): BufferedImage {
    val resource = Thread.currentThread().contextClassLoader.getResource(path)
    requireNotNull(resource) { "Resource $path not found" }
    return resource.openStream().use(ImageIO::read)
}

fun File.readTextLines(scope: CoroutineScope): TextLines {
    val lines = this.readLines()

    return object : TextLines {
        override val size get() = lines.size

        override fun get(index: Int): String {
            return lines[index] //StandardCharsets.UTF_8.decode(slice).toString()
        }
    }
}

private fun File.readLinePositions(
    starts: IntList
) {
    require(length() <= Int.MAX_VALUE) {
        "Files with size over ${Int.MAX_VALUE} aren't supported"
    }

    val averageLineLength = 200
    starts.clear(length().toInt() / averageLineLength)

    try {
        FileInputStream(this@readLinePositions).use {
            val channel = it.channel
            val ib = channel.map(
                FileChannel.MapMode.READ_ONLY, 0, channel.size()
            )
            var isBeginOfLine = true
            var position = 0L
            while (ib.hasRemaining()) {
                val byte = ib.get()
                if (isBeginOfLine) {
                    starts.add(position.toInt())
                }
                isBeginOfLine = byte.toChar() == '\n'
                position++
            }
            channel.close()
        }
    } catch (e: IOException) {
        e.printStackTrace()
        starts.clear(1)
        starts.add(0)
    }

    starts.compact()
}

/**
 * Compact version of List<Int> (without unboxing Int and using IntArray under the hood)
 */
private class IntList(initialCapacity: Int = 16) {
    @Volatile
    private var array = IntArray(initialCapacity)

    @Volatile
    var size: Int = 0
        private set

    fun clear(capacity: Int) {
        array = IntArray(capacity)
        size = 0
    }

    fun add(value: Int) {
        if (size == array.size) {
            doubleCapacity()
        }
        array[size++] = value
    }

    operator fun get(index: Int) = array[index]

    private fun doubleCapacity() {
        val newArray = IntArray(array.size * 2 + 1)
        System.arraycopy(array, 0, newArray, 0, size)
        array = newArray
    }

    fun compact() {
        array = array.copyOfRange(0, size)
    }
}

val File.children: List<File>
    get() = this
        .listFiles { _, name -> !name.startsWith(".") }
        .orEmpty()
        .map { it }
val File.hasChildren: Boolean
    get() = isDirectory && listFiles()?.size ?: 0 > 0
