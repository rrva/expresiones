package expresiones.ast

import me.tomassetti.kolasu.model.Point

data class Error(val message: String, val position: Point)