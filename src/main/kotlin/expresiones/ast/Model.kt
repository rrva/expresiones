package expresiones.ast

import me.tomassetti.kolasu.model.Node
import me.tomassetti.kolasu.model.Position

data class Root(val expression: Expression, override val position: Position? = null) : Node(position)
open class Expression(position: Position?) : Node(position)
data class StringLiteral(val value: String, override val position: Position? = null) : Expression(position)
data class IntegerLiteral(val value: Long, override val position: Position? = null) : Expression(position)
data class DecimalLiteral(val value: Double, override val position: Position? = null) : Expression(position)
data class BooleanLiteral(val value: Boolean, override val position: Position? = null) : Expression(position)
data class AnyMemberExpression(val left: Expression, val right: Expression, override val position: Position? = null) : Expression(position)
data class InExpression(val left: Expression, val right: Expression, override val position: Position? = null) : Expression(position)
data class MemberExpression(val left: Expression, val right: Expression, override val position: Position? = null) : Expression(position)
data class IndexExpression(val left: Expression, val right: Expression, override val position: Position? = null) : Expression(position)
data class LogicalNotExpression(val expression: Expression, override val position: Position? = null) : Expression(position)
data class EqualsExpression(val left: Expression, val right: Expression, override val position: Position? = null) : Expression(position)
data class LessThanOrEqualsExpression(val left: Expression, val right: Expression, override val position: Position? = null) : Expression(position)
data class GreaterThanOrEqualsExpression(val left: Expression, val right: Expression, override val position: Position? = null) : Expression(position)
data class LessThanExpression(val left: Expression, val right: Expression, override val position: Position? = null) : Expression(position)
data class GreaterThanExpression(val left: Expression, val right: Expression, override val position: Position? = null) : Expression(position)
data class NotEqualsExpression(val left: Expression, val right: Expression, override val position: Position? = null) : Expression(position)
data class BooleanAndExpression(val left: Expression, val right: Expression, override val position: Position? = null) : Expression(position)
data class BooleanOrExpression(val left: Expression, val right: Expression, override val position: Position? = null) : Expression(position)
data class Identifier(val name: String, override val position: Position? = null) : Expression(position)