package expresiones.ast

import expresiones.parser.ExpresionesAntlrParserFacade
import org.junit.Assert.assertEquals
import org.junit.Test

class AstMappingTest {

    @Test
    fun `mapping equals should give expected AST`() {
        val ast = ExpresionesAntlrParserFacade.parse("a==b").root!!.toAst()
        val expectedAst = Root(
            EqualsExpression(
                Identifier("a"), Identifier("b")
            )
        )
        assertEquals(expectedAst, ast)
    }

    @Test
    fun `mapping greater than should have expected precedence over or`() {
        val ast = ExpresionesAntlrParserFacade.parse("a>0 || b>0").root!!.toAst()
        val expectedAst = Root(
            BooleanOrExpression(
                GreaterThanExpression(
                    Identifier("a"), IntegerLiteral(0)
                ), GreaterThanExpression(
                    Identifier("b"), IntegerLiteral(0)
                )
            )
        )

        assertEquals(expectedAst, ast)
    }

    @Test
    fun `mapping more complex expression should give expected AST`() {
        val ast = ExpresionesAntlrParserFacade.parse("(a=='hello' || a=='hej') && b>0").root!!.toAst()
        val expectedAst = Root(
            BooleanAndExpression(
                BooleanOrExpression(
                    EqualsExpression(
                        Identifier("a"), StringLiteral("hello")
                    ), EqualsExpression(
                        Identifier("a"), StringLiteral("hej")
                    )
                ), GreaterThanExpression(
                    Identifier("b"), IntegerLiteral(0)
                )
            )
        )

        assertEquals(expectedAst, ast)
    }

}