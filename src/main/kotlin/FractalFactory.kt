import org.openrndr.Program
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.extra.olive.oliveProgram

lateinit var pg: Program
var state = State.DRAW_SHAPE

enum class State {
    DRAW_SHAPE, DEFINE_NEXT_GEN, DISPLAY_FRACTAL
}

fun main() = application {
    configure {
        width = 1280
        height = 720
    }
    oliveProgram {
        pg = this

        extend {
            drawer.clear(ColorRGBa.GRAY)

            InfoText.draw()
        }
    }
}