import org.openrndr.math.Matrix44
import org.openrndr.math.Vector2

fun meanVector(vectors: List<Vector2>): Vector2 {
	val sum = vectors.fold(Vector2.ZERO) { acc, elem -> acc + elem }
	return sum / vectors.size.toDouble()
}

fun middleVector(vectors: List<Vector2>): Vector2 {
	var minX = Double.POSITIVE_INFINITY
	var maxX = Double.NEGATIVE_INFINITY
	var minY = Double.POSITIVE_INFINITY
	var maxY = Double.NEGATIVE_INFINITY

	for (vector in vectors) {
		if (vector.x < minX) minX = vector.x
		if (vector.x > maxX) maxX = vector.x
		if (vector.y < minY) minY = vector.y
		if (vector.y > maxY) maxY = vector.y
	}

	return Vector2((minX + maxX) / 2.0, (minY + maxY) / 2.0)
}

val Matrix44.translationXY
	get() = Vector2(c3r0, c3r1)