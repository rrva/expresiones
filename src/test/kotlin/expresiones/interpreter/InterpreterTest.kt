package expresiones.interpreter

import expresiones.ExpresionesInterpreter
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class InterpreterTest {

    val noSymbols = emptyMap<String, Any>()
    val interpreter = ExpresionesInterpreter()

    @Test
    fun `equal integer literals should be equal`() {
        assertTrue("1==1", noSymbols)
        assertTrue("2==2", noSymbols)
    }

    @Test
    fun `not equal integer literals should not be equal`() {
        assertFalse("1==2", noSymbols)
        assertFalse("1000==2000", noSymbols)
    }

    @Test
    fun `equal string literals should be equal`() {
        assertTrue("'hello'=='hello'", noSymbols)
        assertFalse("'hello'!='hello'", noSymbols)
    }

    @Test
    fun `not equal string literals should not be equal`() {
        assertFalse("'hello'=='goodbye'", noSymbols)
        assertTrue("'hello'!='goodbye'", noSymbols)
    }

    @Test
    fun `equal decimal literals should be equal`() {
        assertTrue("10.12==10.12", noSymbols)
        assertFalse("10.12!=10.12", noSymbols)
    }

    @Test
    fun `not equal decimal literals should not be equal`() {
        assertTrue("10.12!=10.13", noSymbols)
        assertFalse("10.12==10.13", noSymbols)
    }


    @Test
    fun `less than or equals on integer literals which are both less than or equals should be less than or equals`() {
        assertTrue("1<=1", noSymbols)
        assertTrue("1<=2", noSymbols)
        assertFalse("2<=1", noSymbols)
        assertFalse("2<=1", noSymbols)
    }


    @Test
    fun `greater than or equals on integer literals which are both greater than or equals should be greater than or equals`() {
        assertTrue("1>=1", noSymbols)
        assertFalse("1>=2", noSymbols)
        assertTrue("2>=1", noSymbols)
        assertTrue("2>=1", noSymbols)
    }

    @Test
    fun `equal integer literals should not be not equal`() {
        assertFalse("1!=1", noSymbols)
    }

    @Test
    fun `identifier should be equal to its value`() {
        assertTrue("a==1", mapOf("a" to 1))
        assertTrue("a=='hello'", mapOf("a" to "hello"))
        assertTrue("a==10.1", mapOf("a" to 10.1))
    }

    @Test
    fun `boolean literals true and false are true and false`() {
        assertTrue("a==true", mapOf("a" to true))
        assertTrue("a==false", mapOf("a" to false))
        assertTrue("true==true", noSymbols)
        assertTrue("false==false", noSymbols)
        assertFalse("true==false", noSymbols)
    }

    @Test
    fun `greater than with integer literals, 5 is greater than 3`() {
        assertTrue("5>3", noSymbols)
        assertFalse("3>5", noSymbols)
    }

    @Test
    fun `less than with integer literals, 3 is less than 5`() {
        assertTrue("3<5", noSymbols)
        assertFalse("5<3", noSymbols)
    }

    @Test
    fun `greater than with decimal literals - 10,1 is greater than 10,0`() {
        assertTrue("10.1>10.0", noSymbols)
        assertFalse("10.0>10.1", noSymbols)
    }

    @Test
    fun `greater than - 10,1 is greater than 10,0 in variable`() {
        assertTrue("10.1>b", mapOf("b" to 10.0))
        assertFalse("b>10.1", mapOf("b" to 10.0))
    }

    @Test
    fun `greater than - 10,1 in variable is greater than 10,0 in variable`() {
        assertTrue("a>b", mapOf("a" to 10.1, "b" to 10.0))
        assertFalse("b>a", mapOf("a" to 10.1, "b" to 10.0))
    }

    @Test
    fun `less than with decimal literals - 10,0 is less than 10,1`() {
        assertTrue("10.0<10.1", noSymbols)
        assertFalse("10.1<10.0", noSymbols)
    }

    @Test
    fun `less than - 10,0 is less than 10,0 in variable`() {
        assertTrue("a<10.1", mapOf("a" to 10.0))
        assertFalse("10.1<a", mapOf("a" to 10.0))
    }

    @Test
    fun `less than - 10,0 in variable is less than 10,0 in variable`() {
        assertTrue("b<a", mapOf("a" to 10.1, "b" to 10.0))
        assertFalse("a<b", mapOf("a" to 10.1, "b" to 10.0))
    }

    @Test
    fun `decimal and integer literal with same value are equal`() {
        assertTrue("10==10.0", noSymbols)
        assertTrue("10.0==10", noSymbols)
        assertTrue("a==b", mapOf("a" to 10.0, "b" to 10))
        assertTrue("a==b", mapOf("a" to 10.0, "b" to 10.0))
        assertTrue("a==b", mapOf("a" to 10.0f, "b" to 10))
        assertTrue("a==b", mapOf("a" to 10.0f, "b" to 10.0))
        assertTrue("b==a", mapOf("a" to 10.0, "b" to 10))
        assertTrue("b==a", mapOf("a" to 10.0, "b" to 10.0))
        assertTrue("b==a", mapOf("a" to 10.0f, "b" to 10))
        assertTrue("b==a", mapOf("a" to 10.0f, "b" to 10.0))
    }


    @Test
    fun `boolean and with boolean values, full truth table and associative as expected`() {
        assertTrue("a && b", mapOf("a" to true, "b" to true))
        assertFalse("a && b", mapOf("a" to true, "b" to false))
        assertFalse("a && b", mapOf("a" to false, "b" to true))
        assertFalse("a && b", mapOf("a" to false, "b" to false))
        assertTrue("b && a", mapOf("a" to true, "b" to true))
        assertFalse("b && a", mapOf("a" to true, "b" to false))
        assertFalse("b && a", mapOf("a" to false, "b" to true))
        assertFalse("b && a", mapOf("a" to false, "b" to false))
    }

    @Test
    fun `boolean or with boolean values, full truth table and associative as expected`() {
        assertTrue("a || b", mapOf("a" to true, "b" to true))
        assertTrue("a || b", mapOf("a" to true, "b" to false))
        assertTrue("a || b", mapOf("a" to false, "b" to true))
        assertFalse("a || b", mapOf("a" to false, "b" to false))
        assertTrue("b || a", mapOf("a" to true, "b" to true))
        assertTrue("b || a", mapOf("a" to true, "b" to false))
        assertTrue("b || a", mapOf("a" to false, "b" to true))
        assertFalse("b || a", mapOf("a" to false, "b" to false))
    }

    @Test
    fun `not expression inverts boolean values`() {
        assertFalse("!true", noSymbols)
        assertTrue("!false", noSymbols)
    }

    @Test
    fun `boolean expression with parenthesis`() {
        val code = "(a || !b) && c"
        assertFalse(code, mapOf("a" to false, "b" to false, "c" to false))
        assertTrue(code, mapOf("a" to false, "b" to false, "c" to true))
        assertFalse(code, mapOf("a" to false, "b" to true, "c" to false))
        assertFalse(code, mapOf("a" to false, "b" to true, "c" to true))
        assertFalse(code, mapOf("a" to true, "b" to false, "c" to false))
        assertTrue(code, mapOf("a" to true, "b" to false, "c" to true))
        assertFalse(code, mapOf("a" to true, "b" to true, "c" to false))
        assertTrue(code, mapOf("a" to true, "b" to true, "c" to true))
    }

    @Test
    fun `expression to test if array contains value finds existing value`() {
        assertTrue("'joe' in a[].name", mapOf("a" to arrayOf(mapOf("name" to "joe"))))
        assertFalse("'alice' in a[].name", mapOf("a" to arrayOf(mapOf("name" to "joe"))))
    }

    @Test
    fun `expression to test if array contains nested value finds existing value`() {
        assertTrue("'john' in a[].names[].first", mapOf("a" to listOf(mapOf("names" to listOf(mapOf("first" to "joe"),
                mapOf("first" to "john"))))))
        assertTrue("'jane' in a[].names[].first", mapOf("a" to listOf(mapOf("names" to listOf(mapOf("first" to "jane"),
                mapOf("first" to "john"))))))
        assertFalse("'bob' in a[].names[].first", mapOf("a" to listOf(mapOf("names" to listOf(mapOf("first" to "joe"),
                mapOf("first" to "john"))))))
    }


    @Test
    fun `expression to test if array contains deeply nested value finds existing value`() {
        assertTrue("'joe' in a[].names[].first[].favorite", mapOf("a" to listOf(mapOf("names" to listOf(mapOf("first" to listOf(mapOf("favorite" to "joe"))),
                mapOf("first" to listOf(mapOf("favorite" to "alice"))))))))
    }

    fun assertTrue(code: String, symbolTable: Map<String, Any>) {
        assertTrue(interpreter.evaluate(code, symbolTable))
    }

    fun assertFalse(code: String, symbolTable: Map<String, Any>) {
        assertFalse(interpreter.evaluate(code, symbolTable))
    }
}