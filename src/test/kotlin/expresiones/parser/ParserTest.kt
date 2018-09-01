package expresiones.parser

import org.junit.Assert.assertEquals
import org.junit.Test

class ParserTest {

    @Test
    fun `equals expression should give expected parse tree`() {
        assertEquals(
            """Root
  EqualsExpression
    IdentifierExpression
      T:Identifier[a]
    T:Equals_[==]
    IdentifierExpression
      T:Identifier[b]
  T:EOF[<EOF>]
""", parseTree("a == b")
        )
    }

    @Test
    fun `parenthesis expression should give expected parse tree`() {
        assertEquals(
            """Root
  BooleanOrExpression
    ParenExpression
      T:LParen_[(]
      BooleanAndExpression
        IdentifierExpression
          T:Identifier[a]
        T:And_[&&]
        IdentifierExpression
          T:Identifier[b]
      T:RParen_[)]
    T:Or_[||]
    IdentifierExpression
      T:Identifier[c]
  T:EOF[<EOF>]
""", parseTree("(a && b) || c")
        )
    }

    @Test
    fun `whitespace outside strings should be ignored`() {
        assertEquals(
            parseTree("a==b"), parseTree("a == b")
        )
    }

    @Test
    fun `string literals with spaces inside should be preserved`() {
        assertEquals(
            """Root
  EqualsExpression
    StringLiteralExpression
      T:StringLiteral['hello ']
    T:Equals_[==]
    StringLiteralExpression
      T:StringLiteral[' hello']
  T:EOF[<EOF>]
""", parseTree("'hello ' == ' hello'")
        )
    }

    @Test
    fun `string literals with spaces inside gives the expected parse tree`() {
        assertEquals(
            """Root
  StringLiteralExpression
    T:StringLiteral['hello world']
  T:EOF[<EOF>]
""", parseTree("'hello world'")
        )
    }

}