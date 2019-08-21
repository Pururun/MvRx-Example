package jonatan.rhodin.urlparser

import org.junit.Assert.assertEquals
import org.junit.Test
import java.net.MalformedURLException

/**
 * Test that UrlParser.parseUrlString works correctly
 */
class ParseUrlTest {

    @Test
    fun `full url is parsed correctly`() {
        val expected = mapOf(
            "scheme" to "http",
            "user" to "username",
            "pass" to "password",
            "host" to "hostname",
            "path" to "/path",
            "query" to "arg=value",
            "fragment" to "anchor"
        )
        val result = parseUrlString("http://username:password@hostname/path?arg=value#anchor")
        assertEquals(expected, result)
    }

    @Test
    fun `simple webpage is parsed correctly`() {
        val expected = mapOf("scheme" to "https", "host" to "google.com")
        val result = parseUrlString("https://google.com")
        assertEquals(expected, result)
    }

    @Test(expected = MalformedURLException::class)
    fun `no scheme throws exception`() {
        parseUrlString("google.com")
    }

    @Test
    fun `username and password with no host`() {
        val expected = mapOf("scheme" to "https", "user" to "username", "pass" to "password")
        val result = parseUrlString("https://username:password@")
        assertEquals(expected, result)
    }

    @Test
    fun `parse scheme, username, password and host`() {
        val expected = mapOf("scheme" to "https", "user" to "username", "pass" to "password", "host" to "hostname")
        val result = parseUrlString("https://username:password@hostname")
        assertEquals(expected, result)
    }

    @Test
    fun `complex path`() {
        val expected = mapOf("scheme" to "https", "host" to "hostname", "path" to "/path/to/here")
        val result = parseUrlString("https://hostname/path/to/here")
        assertEquals(expected, result)
    }

    @Test
    fun `multiple queries`() {
        val expected = mapOf("scheme" to "https", "host" to "hostname", "path" to "/path", "query" to "a=b&c=d")
        val result = parseUrlString("https://hostname/path?a=b&c=d")
        assertEquals(expected, result)
    }

    @Test
    fun `queries without path`() {
        val expected = mapOf("scheme" to "https", "host" to "hostname", "query" to "a=b&c=d")
        val result = parseUrlString("https://hostname?a=b&c=d")
        assertEquals(expected, result)
    }

    @Test
    fun `hostname with fragment`() {
        val expected = mapOf("scheme" to "https", "host" to "hostname", "fragment" to "fragment")
        val result = parseUrlString("https://hostname#fragment")
        assertEquals(expected, result)
    }

    @Test
    fun `query with no host`() {
        val expected = mapOf("scheme" to "https", "query" to "hello=go")
        val result = parseUrlString("https://?hello=go")
        assertEquals(expected, result)
    }
}