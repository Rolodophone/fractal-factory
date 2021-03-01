import org.openrndr.math.Vector2

fun meanVector(vectors: List<Vector2>): Vector2 {
	val sum = vectors.fold(Vector2.ZERO) { acc, elem -> acc + elem }
	return sum / vectors.size.toDouble()
}