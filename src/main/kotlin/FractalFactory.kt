import org.openrndr.KEY_ENTER
import org.openrndr.KEY_ESCAPE
import org.openrndr.Program
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.LineCap
import org.openrndr.draw.isolated
import org.openrndr.extra.olive.oliveProgram
import org.openrndr.math.Matrix44
import org.openrndr.math.Vector2
import org.openrndr.math.transforms.transform
import kotlin.math.pow

lateinit var pg: Program
var state = State.DRAW_SHAPE

var shape = mutableListOf<MutableList<Vector2>>()
var transformations = mutableListOf<Matrix44>()

var currentTransform = transform { scale(0.5) }

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
            //drawing settings
            drawer.lineCap = LineCap.ROUND

            //draw background
            drawer.clear(ColorRGBa.GRAY)

            //draw base shape
            drawer.fill = ColorRGBa.BLACK
            drawer.lineStrips(shape)

            if (state == State.DEFINE_NEXT_GEN) {
                //draw first gen
                for (transform in transformations) {
                    drawer.isolated {
                        drawer.model = transform
                        drawer.lineStrips(shape)
                    }
                }

                //draw ghost of next transform
                drawer.fill = ColorRGBa.GRAY
                drawer.isolated {
                    drawer.model = currentTransform
                    drawer.lineStrips(shape)
                }
            }



            //info text
            InfoText.draw()
        }

        mouse.buttonDown.listen {
            when (state) {
                State.DRAW_SHAPE -> shape.add(mutableListOf(it.position))
                State.DEFINE_NEXT_GEN -> {
                    transformations.add(currentTransform)
                }
                else -> {}
            }

        }

        mouse.dragged.listen {
            shape.last().add(it.position)
        }

        mouse.scrolled.listen {
            currentTransform = transform(currentTransform) {
                scale(2.0.pow(it.rotation.y))
            }
        }

        keyboard.keyDown.listen { event ->
            when (event.key) {
                KEY_ENTER -> {
                    when (state) {
                        State.DRAW_SHAPE -> state = State.DEFINE_NEXT_GEN
                        State.DEFINE_NEXT_GEN -> state = State.DISPLAY_FRACTAL
                        else -> {}
                    }
                }

                KEY_ESCAPE -> {
                    when (state) {
                        State.DISPLAY_FRACTAL -> {
                            shape = mutableListOf()
                            state = State.DRAW_SHAPE
                        }
                        else -> {}
                    }
                }
            }
        }
    }
}