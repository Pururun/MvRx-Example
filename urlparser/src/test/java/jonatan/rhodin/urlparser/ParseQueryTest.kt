package jonatan.rhodin.urlparser

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 *
 * Test that UrlParser.parseQuery works correct
 *
 */
class ParseQueryTest {

    @Test
    fun `single pair query`() {
        val expected = mapOf("key" to "value")
        val result = parseQuery("key=value")
        assertEquals(expected, result)
    }

    @Test
    fun `multiple pairs query`() {
        val expected = mapOf("a" to "b", "c" to "d")
        val result = parseQuery("a=b&c=d")
        assertEquals(expected, result)
    }

    @Test
    fun `query is empty string`() {
        val expected = emptyMap<String, String>()
        val result = parseQuery("")
        assertEquals(expected, result)
    }

    @Test
    fun `query has empty value`() {
        val expected = mapOf("key" to "")
        val result = parseQuery("key=")
        assertEquals(expected, result)
    }

    @Test
    fun `query has no pair`() {
        val expected = emptyMap<String, String>()
        val result = parseQuery("something")
        assertEquals(expected, result)
    }
}