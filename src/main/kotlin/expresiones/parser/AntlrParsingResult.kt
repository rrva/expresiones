package expresiones.parser

import expresiones.ExpresionesLexer
import expresiones.ExpresionesParser
import expresiones.ExpresionesParser.RootContext
import me.tomassetti.kolasu.model.Point
import org.antlr.v4.runtime.*
import org.antlr.v4.runtime.atn.ATNConfigSet
import org.antlr.v4.runtime.dfa.DFA
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.nio.charset.Charset
import java.util.*

data class AntlrParsingResult(val root: RootContext?, val errors: List<expresiones.ast.Error>)

fun String.toStream(charset: Charset = Charsets.UTF_8) = ByteArrayInputStream(toByteArray(charset))

object ExpresionesAntlrParserFacade {

    fun parse(code: String): AntlrParsingResult = parse(code.toStream())

    fun parse(inputStream: InputStream): AntlrParsingResult {
        val lexicalAndSyntacticErrors = LinkedList<expresiones.ast.Error>()
        val errorListener = object : ANTLRErrorListener {
            override fun reportAmbiguity(p0: Parser?, p1: DFA?, p2: Int, p3: Int, p4: Boolean, p5: BitSet?, p6: ATNConfigSet?) {
                // Ignored for now
            }

            override fun reportAttemptingFullContext(p0: Parser?, p1: DFA?, p2: Int, p3: Int, p4: BitSet?, p5: ATNConfigSet?) {
                // Ignored for now
            }

            override fun syntaxError(
                recognizer: Recognizer<*, *>?, offendingSymbol: Any?, line: Int, charPositionInline: Int, msg: String, ex: RecognitionException?
            ) {
                lexicalAndSyntacticErrors.add(expresiones.ast.Error(msg, Point(line, charPositionInline)))
            }

            override fun reportContextSensitivity(p0: Parser?, p1: DFA?, p2: Int, p3: Int, p4: Int, p5: ATNConfigSet?) {
                // Ignored for now
            }
        }

        val lexer = ExpresionesLexer(CharStreams.fromStream(inputStream))
        lexer.removeErrorListeners()
        lexer.addErrorListener(errorListener)
        val parser = ExpresionesParser(CommonTokenStream(lexer))
        parser.removeErrorListeners()
        parser.addErrorListener(errorListener)
        val root = parser.root()
        return AntlrParsingResult(root, lexicalAndSyntacticErrors)
    }

}