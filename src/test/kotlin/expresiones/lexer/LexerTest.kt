package expresiones.lexer

import org.junit.Assert.assertEquals
import org.junit.Test

class LexerTest {

    @Test
    fun `lexing equals expression gives expected tokens`() {
        assertEquals(
            listOf("Identifier", "Equals_", "Identifier", "EOF"), tokensNames(lexerForCode("a==b"))
        )
    }

    @Test
    fun `lexing string literal gives expected tokens`() {
        assertEquals(
            listOf("StringLiteral", "EOF"), tokensNames(lexerForCode("'a'"))
        )
    }

    @Test
    fun `lexing paren expression gives expected tokens`() {
        assertEquals(
            listOf("LParen_", "Identifier", "And_", "Identifier", "RParen_", "Or_", "Identifier", "EOF"), tokensNames(lexerForCode("(a && b) || c"))
        )
    }

    @Test
    fun `lexing identifier with underscore gives a single token`() {
        assertEquals(
            listOf("Identifier", "EOF"), tokensNames(lexerForCode("a_b"))
        )
    }
}