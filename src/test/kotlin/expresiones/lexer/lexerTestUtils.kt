package expresiones.lexer

import expresiones.ExpresionesLexer
import org.antlr.v4.runtime.CharStreams
import java.io.StringReader
import java.util.*

fun lexerForCode(code: String) = ExpresionesLexer(CharStreams.fromReader(StringReader(code)))

fun tokensNames(lexer: ExpresionesLexer): List<String> {
    val tokens = LinkedList<String>()
    do {
        val t = lexer.nextToken()
        when (t.type) {
            -1 -> tokens.add("EOF")
            else -> if (t.type != ExpresionesLexer.Whitespace) tokens.add(lexer.vocabulary.getSymbolicName(t.type))
        }
    } while (t.type != -1)
    return tokens
}