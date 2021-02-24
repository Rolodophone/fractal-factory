import org.openrndr.Program
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.extra.olive.oliveProgram
import org.openrndr.math.Vector2

lateinit var pg: Program
var state = State.DRAW_SHAPE

var shape = mutableListOf<MutableList<Vector2>>()

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

            //draw shape
            drawer.fill = ColorRGBa.BLACK
            drawer.strokeWeight = 10.0

            for (lineStrip in shape) {
                drawer.lineStrip(lineStrip)
            }

            InfoText.draw()
        }

        mouse.buttonDown.listen {
            shape.add(mutableListOf(it.position))
        }

        mouse.dragged.listen {
            shape.last().add(it.position)
        }
    }
}