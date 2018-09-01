package expresiones.ast

import expresiones.ExpresionesParser.*
import me.tomassetti.kolasu.mapping.toPosition

fun RootContext.toAst(considerPosition: Boolean = false): Root =
    Root(this.expression().toAst(considerPosition), toPosition(considerPosition))

fun ExpressionContext.toAst(considerPosition: Boolean = false): Expression = when (this) {
    is StringLiteralExpressionContext -> StringLiteral(this.text.removePrefix("'").removeSuffix("'"), toPosition(considerPosition))
    is StringLiteralQuotedExpressionContext -> StringLiteral(this.text.removePrefix("\"").removeSuffix("\""), toPosition(considerPosition))
    is IntegerLiteralExpressionContext -> IntegerLiteral(this.text.toLong(), toPosition(considerPosition))
    is BooleanLiteralExpressionContext -> BooleanLiteral(this.text!!.toBoolean(), toPosition(considerPosition))
    is DecimalLiteralExpressionContext -> DecimalLiteral(this.text.toDouble(), toPosition(considerPosition))
    is EqualsExpressionContext -> EqualsExpression(left.toAst(considerPosition), right.toAst(considerPosition), toPosition(considerPosition))
    is LessThanExpressionContext -> LessThanExpression(left.toAst(considerPosition), right.toAst(considerPosition), toPosition(considerPosition))
    is GreaterThanExpressionContext -> GreaterThanExpression(left.toAst(considerPosition), right.toAst(considerPosition), toPosition(considerPosition))
    is LessThanOrEqualsExpressionContext -> LessThanOrEqualsExpression(
        left.toAst(considerPosition),
        right.toAst(considerPosition),
        toPosition(considerPosition)
    )
    is GreaterThanOrEqualsExpressionContext -> GreaterThanOrEqualsExpression(
        left.toAst(considerPosition), right.toAst(considerPosition), toPosition(considerPosition)
    )
    is NotEqualsExpressionContext -> NotEqualsExpression(left.toAst(considerPosition), right.toAst(considerPosition), toPosition(considerPosition))
    is BooleanAndExpressionContext -> BooleanAndExpression(left.toAst(considerPosition), right.toAst(considerPosition), toPosition(considerPosition))
    is BooleanOrExpressionContext -> BooleanOrExpression(left.toAst(considerPosition), right.toAst(considerPosition), toPosition(considerPosition))
    is IdentifierExpressionContext -> Identifier(this.text)
    is MemberExpressionContext -> MemberExpression(left.toAst(considerPosition), right.toAst(considerPosition), toPosition(considerPosition))
    is AnyMemberExpressionContext -> AnyMemberExpression(left.toAst(considerPosition), right.toAst(considerPosition), toPosition(considerPosition))
    is InExpressionContext -> InExpression(left.toAst(considerPosition), right.toAst(considerPosition), toPosition(considerPosition))
    is IndexExpressionContext -> IndexExpression(left.toAst(considerPosition), right.toAst(considerPosition), toPosition(considerPosition))
    is ParenExpressionContext -> expression().toAst(considerPosition)
    is LogicalNotExpressionContext -> LogicalNotExpression(expression().toAst(considerPosition), toPosition(considerPosition))
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}