package expresiones

import expresiones.ast.*
import expresiones.parser.ExpresionesAntlrParserFacade
import kotlin.math.ulp

class ExpresionesInterpreter {


    fun evaluate(code: String, symbolTable: Map<*, *>): Boolean {
        val expression = ExpresionesAntlrParserFacade.parse(code).root!!.toAst(considerPosition = true).expression
        val res = evaluate(expression, symbolTable)
        if (res is Boolean) {
            return res
        }
        throw IllegalStateException("Expression $code does not evaluate to true or false, but was instead $res")
    }


    fun evaluate(expression: Expression, symbolTable: Map<*, *>): Any? = when (expression) {
        is StringLiteral -> expression.value
        is IntegerLiteral -> expression.value
        is DecimalLiteral -> expression.value
        is BooleanLiteral -> expression.value
        is Identifier -> symbolTable[expression.name]
        is InExpression -> {
            val left = evaluate(expression.left, symbolTable)
            val right = evaluate(expression.right, symbolTable)
            when (right) {
                is Iterable<*> -> right.any { it == left }
                is Array<*> -> right.any { it == left }
                else -> equalsExpression(left, right, expression)
            }
        }
        is AnyMemberExpression -> {
            val left = evaluate(expression.left, symbolTable)
            when (left) {
                is List<*> -> {
                    left.map {
                        if (it is Map<*, *>) {
                            evaluate(expression.right, it)
                        } else {
                            evaluate(expression.right, symbolTable)
                        }
                    }.flattenAny()
                }
                is Array<*> -> {
                    left.map {
                        if (it is Map<*, *>) {
                            evaluate(expression.right, it)
                        } else {
                            evaluate(expression.right, symbolTable)
                        }
                    }.flattenAny()
                }
                else -> throw UnsupportedOperationException(expression.left.toString() + " from evaluating " + expression.right)
            }
        }
        is MemberExpression -> {
            val symbols = evaluate(expression.left, symbolTable)
            if (symbols is Map<*, *>) {
                evaluate(expression.right, symbols)
            } else {
                throw UnsupportedOperationException(expression.left.toString() + " from evaluating " + expression.right)
            }
        }
        is IndexExpression -> {
            val index = evaluate(expression.right, symbolTable)
            val indexValue = (index as? Long)?.toInt() ?: (index as? Int
                    ?: throw UnsupportedOperationException("Cannot use array index " + expression.right.toString() + " when evaluating " + expression.left))
            val value = evaluate(expression.left, symbolTable)
            if (value is Array<*>) {
                value[indexValue]
            } else {
                throw UnsupportedOperationException(expression.left.toString() + " from evaluating " + expression.right)
            }
        }
        is EqualsExpression -> {
            val l = evaluate(expression.left, symbolTable)
            val r = evaluate(expression.right, symbolTable)
            equalsExpression(l, r, expression.left)
        }
        is NotEqualsExpression -> {
            val l = evaluate(expression.left, symbolTable)
            val r = evaluate(expression.right, symbolTable)
            !equalsExpression(l, r, expression.left)

        }
        is LessThanOrEqualsExpression -> {
            val l = evaluate(expression.left, symbolTable)
            val r = evaluate(expression.right, symbolTable)
            val isLessThan = lessThanExpression(l, r)
            val isEquals = equalsExpression(l, r, expression.left)
            isLessThan || isEquals
        }
        is GreaterThanOrEqualsExpression -> {
            val l = evaluate(expression.left, symbolTable)
            val r = evaluate(expression.right, symbolTable)
            val isGreaterThan = greaterThanExpression(l, r)
            val isEquals = equalsExpression(l, r, expression.left)
            isGreaterThan || isEquals
        }
        is LessThanExpression -> {
            val l = evaluate(expression.left, symbolTable)
            val r = evaluate(expression.right, symbolTable)
            lessThanExpression(l, r)
        }
        is GreaterThanExpression -> {
            val l = evaluate(expression.left, symbolTable)
            val r = evaluate(expression.right, symbolTable)
            greaterThanExpression(l, r)
        }
        is BooleanAndExpression -> {
            val l = evaluate(expression.left, symbolTable)
            val r = evaluate(expression.right, symbolTable)
            if (r is Boolean && l is Boolean) {
                l && r
            } else throw UnsupportedOperationException(l.toString() + " from evaluating " + expression.left)

        }
        is LogicalNotExpression -> {
            val e = evaluate(expression.expression, symbolTable)
            if (e is Boolean) {
                !e
            } else throw UnsupportedOperationException(e.toString() + " from evaluating " + expression)

        }
        is BooleanOrExpression -> {
            val l = evaluate(expression.left, symbolTable)
            val r = evaluate(expression.right, symbolTable)
            if (r is Boolean && l is Boolean) {
                l || r
            } else throw UnsupportedOperationException(l.toString() + " from evaluating " + expression.left)

        }
        else -> throw UnsupportedOperationException(expression.javaClass.canonicalName)
    }

    private fun greaterThanExpression(l: Any?, r: Any?): Boolean {
        if (l is Comparable<*> && r is Comparable<*>) {
            @Suppress("UNCHECKED_CAST")
            return l as Comparable<Any?> > r as Comparable<Any?>
        } else {
            throw UnsupportedOperationException("Cannot compare $l to $r")
        }
    }

    private fun lessThanExpression(l: Any?, r: Any?): Boolean {
        if (l is Comparable<*> && r is Comparable<*>) {
            @Suppress("UNCHECKED_CAST")
            return l as Comparable<Any?> < r as Comparable<Any?>
        } else {
            throw UnsupportedOperationException("Cannot compare $l to $r")
        }
    }

    private fun equalsExpression(
            l: Any?, r: Any?, left: Expression
    ): Boolean {
        return when {
            l is String && r is String -> l == r
            l is Long && r is Long -> l == r
            l is Int && r is Int -> l == r
            l is Long && r is Int -> l.compareTo(r) == 0
            l is Int && r is Long -> l.compareTo(r) == 0
            l is Boolean && r is Boolean -> l == r
            l is Double && r is Double -> almostEqual(l, r)
            l is Long && r is Double -> almostEqual(l, r)
            l is Int && r is Double -> almostEqual(l, r)
            l is Double && r is Long -> almostEqual(l, r)
            l is Double && r is Int -> almostEqual(l, r)
            l is Double && r is Float -> almostEqual(l, r)
            l is Float && r is Double -> almostEqual(l, r)
            l is Int && r is Float -> almostEqual(l, r)
            l is Float && r is Int -> almostEqual(l, r)
            else -> throw UnsupportedOperationException(l.toString() + "==" + r.toString() + " from evaluating " + left)
        }
    }


    private fun almostEqual(a: Double, b: Double): Boolean {
        return Math.abs(a - b) < Math.max(a.ulp, b.ulp)
    }

    private fun almostEqual(a: Long, b: Double): Boolean {
        return almostEqual(a.toDouble(), b)
    }

    private fun almostEqual(a: Double, b: Long): Boolean {
        return almostEqual(a, b.toDouble())
    }

    private fun almostEqual(a: Double, b: Int): Boolean {
        return almostEqual(a, b.toDouble())
    }

    private fun almostEqual(a: Int, b: Double): Boolean {
        return almostEqual(a.toDouble(), b)
    }

    private fun almostEqual(a: Double, b: Float): Boolean {
        return almostEqual(a, b.toDouble())
    }

    private fun almostEqual(a: Float, b: Double): Boolean {
        return almostEqual(a.toDouble(), b)
    }


    private fun almostEqual(a: Int, b: Float): Boolean {
        return almostEqual(a, b.toDouble())
    }

    private fun almostEqual(a: Float, b: Int): Boolean {
        return almostEqual(a.toDouble(), b)
    }

    private fun List<Any?>.flattenAny(): List<Any?> {
        val list = mutableListOf<Any?>()
        flattenList(this, list)
        return list.toList()
    }

    private fun flattenList(nestedList: List<Any?>, flatList: MutableList<Any?>) {
        nestedList.forEach { e ->
            when (e) {
                !is List<Any?> -> flatList.add(e)
                else -> flattenList(e, flatList)
            }
        }
    }

}



fun main(args: Array<String>) {
    val code = """'bijäb'=="bijäb""""
    val expression = ExpresionesAntlrParserFacade.parse(code).root!!.toAst(considerPosition = true).expression

    val variables = mapOf(
            "a" to arrayOf(mapOf("names" to mapOf("first" to "pelle")), mapOf("names" to mapOf("first" to "kalle"))),
            "b" to true,
            "c" to 3,
            "foo" to mapOf("name" to mapOf("first" to "pelle"))
    )
    val interpreter = ExpresionesInterpreter()

    println(expression.toString() + "\n\nevaluates to\n\n" + interpreter.evaluate(expression, variables))

}
