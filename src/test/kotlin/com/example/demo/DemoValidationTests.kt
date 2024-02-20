package com.example.demo

import com.example.demo.data.demo.validation.DateValidator
import com.example.demo.data.demo.validation.ValidationState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.BeforeEach

@SpringBootTest
class DemoValidationTests {

    @BeforeEach
    fun setupMocks(){
        // Mock the companion object
        mockkObject(DateValidator.Companion)

        // Mock the static function
        every { DateValidator.getCurrentDateAsString("yyyy-MM-dd") } returns "2022-01-02"
    }

    fun validate(states: List<() -> ValidationState>): List<ValidationState.INVALID>{
        return states.map{ println("Validation something"); it() }.filterIsInstance<ValidationState.INVALID>()
    }

    @Test
    fun `Test Date Validation INVALID - NULL`(){
        val invalidDate = DateValidator.validateDateForTimespan("", 0)
        assertEquals(ValidationState.INVALID("Invalid Datetime"), invalidDate)

        val x = listOf({DateValidator.validateDateForTimespan("", 0)})

        val y = validate(x)
        assertEquals(listOf(ValidationState.INVALID("Invalid Datetime")), y)
    }

    @Test
    fun `Test Date Validation INVALID - Future`(){
        val invalidFutureDate = DateValidator.validateDateForTimespan("2022-01-19", 7)
        assertEquals(ValidationState.INVALID("Day in the Future"), invalidFutureDate)
    }

    @Test
    fun `Test Date Validation INVALID - Past`(){
        val invalidPastDate = DateValidator.validateDateForTimespan("2022-01-01", 7)
        assertEquals(ValidationState.INVALID("Day in the Past"), invalidPastDate)
    }

    @Test
    fun `Test Date Validation INVALID - INVALID`(){
        val invalidDate = DateValidator.validateDateForTimespan("01-01-202", 0)
        assertEquals(ValidationState.INVALID("Invalid Datetime"), invalidDate)
    }

    @Test
    fun `Test Date Validation INVALID - Boundary`(){
        val invalidDate = DateValidator.validateDateForTimespan("2022-01-10", 7)
        assertEquals(ValidationState.INVALID("Day in the Future"), invalidDate)
    }

    @Test
    fun `Test Date Validation VALID - Current`(){
        val validDate = DateValidator.validateDateForTimespan("2022-01-02", 7)
        assertEquals(ValidationState.VALID, validDate)
    }

    @Test
    fun `Test Date Validation VALID - Future`(){
        val validDate = DateValidator.validateDateForTimespan("2022-01-04", 7)
        assertEquals(ValidationState.VALID, validDate)
    }

    @Test
    fun `Test Date Validation VALID - Boundary`(){
        val validDate = DateValidator.validateDateForTimespan("2022-01-09", 7)
        assertEquals(ValidationState.VALID, validDate)
    }
}