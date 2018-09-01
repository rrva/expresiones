package expresiones.parser

import expresiones.ExpresionesParser
import expresiones.lexer.lexerForCode
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.Vocabulary
import org.antlr.v4.runtime.tree.TerminalNode
import java.util.*

fun parseCode(code: String): ExpresionesParser.RootContext = ExpresionesParser(CommonTokenStream(lexerForCode(code))).root()

fun parseTree(code: String) = toParseTree(parseCode(code), ExpresionesParser.VOCABULARY).multiLineString()

abstract class ParseTreeElement {
    abstract fun multiLineString(indentation: String = ""): String
}

class ParseTreeLeaf(private val type: String, val text: String) : ParseTreeElement() {
    override fun toString(): String {
        return "T:$type[$text]"
    }

    override fun multiLineString(indentation: String): String = "${indentation}T:$type[$text]\n"
}

class ParseTreeNode(private val name: String) : ParseTreeElement() {
    private val children = LinkedList<ParseTreeElement>()
    fun child(c: ParseTreeElement): ParseTreeNode {
        children.add(c)
        return this
    }

    override fun toString(): String {
        return "Node($name) $children"
    }

    override fun multiLineString(indentation: String): String {
        val sb = StringBuilder()
        sb.append("$indentation$name\n")
        children.forEach { c ->
            sb.append(c.multiLineString("$indentation  "))
        }
        return sb.toString()
    }
}

fun toParseTree(
    node: ParserRuleContext, vocabulary: Vocabulary
): ParseTreeNode {
    val res = ParseTreeNode(node.javaClass.simpleName.removeSuffix("Context"))
    node.children.forEach { c ->

        when (c) {
            is ParserRuleContext -> res.child(toParseTree(c, vocabulary))
            is TerminalNode -> res.child(
                ParseTreeLeaf(
                    vocabulary.getSymbolicName(
                        c.symbol.type
                    ), c.text
                )
            )
        }
    }
    return res
}