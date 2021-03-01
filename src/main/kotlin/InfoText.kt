import org.openrndr.color.ColorRGBa

object InfoText {
	fun draw() {
		val text = when (state) {
			State.DRAW_SHAPE -> "Click and drag to draw a shape. Press ENTER when done."
			State.DEFINE_NEXT_GEN -> "Define the next gen. Right click to rotate. Scroll to scale. Press ENTER when " +
				"done"
			State.DISPLAY_FRACTAL -> "Press SPACE to display the next generation. Press ESC to start again."
		}

		//draw background
		pg.drawer.fill = ColorRGBa.WHITE
		pg.drawer.stroke = null
		pg.drawer.rectangle(0.0, 0.0, 7.5 * text.length + 3.0, 22.0)

		//draw text
		pg.drawer.fill = ColorRGBa.BLACK
		pg.drawer.text(text, 5.0, 15.0)
	}
}