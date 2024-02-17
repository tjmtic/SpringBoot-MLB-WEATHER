package com.example.demo.data.demo.validation

import java.text.DateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class DateValidator {

    companion object {

        fun validateDateForTimespan(date: String, timespan: Int, datePattern: String = "yyyy-MM-dd"): ValidationState {
            return validateDate(getCurrentDateAsString(datePattern), date, timespan, datePattern)
        }

        internal fun validateDate(startDate: String, comparisonDate: String, comparator: Int, datePattern: String): ValidationState {
            return when(compareDates(startDate, comparisonDate, comparator, datePattern)) {
                DateComparisonState.VALID -> ValidationState.VALID
                is DateComparisonState.INVALID -> ValidationState.INVALID("Invalid Datetime")
                DateComparisonState.FUTURE -> ValidationState.INVALID("Day in the Future")
                DateComparisonState.PAST -> ValidationState.INVALID("Day in the Past")
            }
        }

        private fun compareDates(benchmarkDate: String, comparisonDate: String, comparator: Int, datePattern: String): DateComparisonState {
            try {
                //Parse Date Strings
                val dateFormat = DateTimeFormatter.ofPattern(datePattern)
                val x = LocalDate.parse(benchmarkDate, dateFormat)
                val y = LocalDate.parse(comparisonDate, dateFormat)

                // Calculate difference in days
                val differenceInDays = ChronoUnit.DAYS.between(x, y)

                //Date is In the Past, return Comparison Value
                if(differenceInDays < 0) return DateComparisonState.PAST

                return when {
                    differenceInDays > comparator -> DateComparisonState.FUTURE
                    differenceInDays <= comparator -> DateComparisonState.VALID
                    else -> DateComparisonState.INVALID("Invalid Date Comparison")
                }
            } catch (e: Exception){
                return DateComparisonState.INVALID(e.message)
            }
        }

        internal fun getCurrentDateAsString(pattern: String): String {
            return LocalDate.now().format(DateTimeFormatter.ofPattern(pattern))
        }
    }
}

sealed class DateComparisonState {
    data object FUTURE : DateComparisonState()
    data object PAST : DateComparisonState()
    data object VALID : DateComparisonState()
    data class INVALID(val message: String?) : DateComparisonState()
}