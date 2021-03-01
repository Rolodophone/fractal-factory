import org.openrndr.KEY_ENTER
import org.openrndr.KEY_ESCAPE
import org.openrndr.Program
import org.openrndr.application
import org.openrndr.color.ColorHSVa
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.LineCap
import org.openrndr.draw.isolated
import org.openrndr.math.Matrix44
import org.openrndr.math.Vector2
import org.openrndr.math.transforms.transform
import kotlin.math.pow

lateinit var pg: Program
var state = State.DRAW_SHAPE

var shape = mutableListOf<MutableList<Vector2>>()
var transformations = mutableListOf<Matrix44>()
var shapeOrigin = Vector2.ZERO

var currentTransform = Matrix44.IDENTITY

val tempTransforms = mutableListOf<Matrix44>()

enum class State {
    DRAW_SHAPE, DEFINE_NEXT_GEN, DISPLAY_FRACTAL
}

fun main() = application {
    configure {
        width = 1280
        height = 720
    }
    program {
        pg = this

        extend {
            //drawing settings
            drawer.lineCap = LineCap.ROUND

            //draw background
            drawer.clear(ColorRGBa(0.9, 0.9, 0.9))

            //draw base shape
            if (state == State.DRAW_SHAPE) {
                drawer.stroke = ColorRGBa.BLACK
                drawer.lineStrips(shape)
            }

            if (state == State.DEFINE_NEXT_GEN) {
                //draw first gen
                for (transform in transformations) {
                    drawer.isolated {
                        drawer.model = transform
                        drawer.lineStrips(shape)
                    }
                }

                //draw ghost of next transform
                drawer.stroke = ColorRGBa.GRAY
                drawer.isolated {
                    drawer.model = currentTransform
                    drawer.lineStrips(shape)
                }
            }

            //---FOR TESTING-----
            var hue = 0.0
            for (transform in tempTransforms) {
                drawer.isolated {
                    drawer.stroke = ColorHSVa(hue, 1.0, 0.5).toRGBa()
                    drawer.model = transform
                    drawer.lineStrips(shape)
                }
                hue += 40.0
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
            when (state) {
                State.DRAW_SHAPE -> shape.last().add(it.position)
                else -> {}
            }
        }

        mouse.scrolled.listen {
            currentTransform = transform(currentTransform) {
                val scaleFactor = 1.2.pow(it.rotation.y)

                translate(shapeOrigin)
                scale(scaleFactor)
                translate(-shapeOrigin)
            }
        }

        keyboard.keyDown.listen { event ->
            when (event.key) {
                KEY_ENTER -> {
                    when (state) {
                        State.DRAW_SHAPE -> {
                            shapeOrigin = meanVector(shape.flatten())
                            state = State.DEFINE_NEXT_GEN
                        }
                        State.DEFINE_NEXT_GEN -> state = State.DISPLAY_FRACTAL
                        else -> {}
                    }
                }

                KEY_ESCAPE -> {
                    when (state) {
                        State.DISPLAY_FRACTAL -> {
                            shape = mutableListOf()
                            transformations = mutableListOf()
                            currentTransform = Matrix44.IDENTITY
                            state = State.DRAW_SHAPE
                        }
                        else -> {}
                    }
                }
            }
        }
    }
}