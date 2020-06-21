package com.fidea.letter

import android.R
import android.content.Context
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    private val FAKE_STRING = "HELLO WORLD"

    @Mock
    var mockContext: Context? = null

    @Test
    fun readStringFromContext_LocalizedString() { // Given a mocked Context injected into the object under test...
//        when(mockContext?.getString(R.string.ok)).thenReturn(FAKE_STRING)
//        val myObjectUnderTest = ClassUnderTest(mockContext)
//        // ...when the string is returned from the object under test...
//        val result: String = myObjectUnderTest.getHelloWorldString()
//        // ...then the result should be the expected one.
//        assertThat(result, is(FAKE_STRING))
    }
}
