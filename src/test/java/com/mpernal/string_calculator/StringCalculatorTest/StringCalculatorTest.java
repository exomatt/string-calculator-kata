package com.mpernal.string_calculator.StringCalculatorTest;

import com.mpernal.string_calculator.StringCalculator;
import com.mpernal.string_calculator.exception.NegativesNotAllowedException;
import com.mpernal.string_calculator.exception.ParseDelimiterException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class StringCalculatorTest {

    @Test
    void shouldReturnZeroForEmptyString() throws NegativesNotAllowedException, ParseDelimiterException {
        assertEquals(0, StringCalculator.add(""));
    }

    @Test
    void shouldAddOneNumber() throws NegativesNotAllowedException, ParseDelimiterException {
        assertEquals(2, StringCalculator.add("2"));
    }

    @Test
    void shouldAddTwoNumbers() throws NegativesNotAllowedException, ParseDelimiterException {
        assertEquals(3, StringCalculator.add("1,2"));
    }

    @Test
    void shouldWorkForUnknownAmountOfNumbers() throws NegativesNotAllowedException, ParseDelimiterException {
        List<Integer> range = IntStream.rangeClosed(3, 40)
                .boxed()
                .toList();
        String textFromRange = range.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        Integer expected = range.stream()
                .mapToInt(Integer::intValue)
                .sum();
        assertEquals(expected, StringCalculator.add(textFromRange));
    }

    @Test
    void shouldAddWhenNewLineCharacter() throws NegativesNotAllowedException, ParseDelimiterException {
        assertEquals(6, StringCalculator.add("1\n2,3"));
    }

    @Test
    void shouldAddWhenTwoDefaultDelimiters() throws NegativesNotAllowedException, ParseDelimiterException {
        assertEquals(3, StringCalculator.add("1,\n2"));
    }

    @Test
    void shouldAddSingleDelimiter() throws NegativesNotAllowedException, ParseDelimiterException {
        assertEquals(6, StringCalculator.add("//;\n1;2;3"));
    }

    @Test
    void shouldAddMultiLengthDelimiter() throws NegativesNotAllowedException, ParseDelimiterException {
        assertEquals(6, StringCalculator.add("//[*****]\n1*****2*****3"));
    }

    @Test
    void shouldAddMoreThanOneDelimiter() throws NegativesNotAllowedException, ParseDelimiterException {
        assertEquals(6, StringCalculator.add("//[*****][&&&&&]\n1*****2&&&&&3"));
    }

    @Test
    void shouldIgnoreBiggerThan1000() throws NegativesNotAllowedException, ParseDelimiterException {
        assertEquals(1004, StringCalculator.add("//[*****][&&&&&]\n1000*****21000&&&&&3*****1*****1001"));
    }


    @Test
    void shouldThrowExceptionForNegative() {
        NegativesNotAllowedException exception = assertThrows(NegativesNotAllowedException.class, () -> StringCalculator.add("5,-2"));
        assertTrue(exception.getMessage().startsWith("negatives not allowed:"));
    }

    @Test
    void shouldThrowParseException() {
        ParseDelimiterException exception = assertThrows(ParseDelimiterException.class, () -> StringCalculator.add("//[*****"));
        assertEquals("Problem with parsing delimiter", exception.getMessage());
    }

    @Test
    void shouldThrowParseExceptionForWrongDelimiter() {
        ParseDelimiterException exception = assertThrows(ParseDelimiterException.class, () -> StringCalculator.add("//[*****4*****"));
        assertEquals("Problem with parsing delimiter", exception.getMessage());
    }

    @Test
    void shouldThrowParseExceptionForWrongDelimiterSecond() {
        ParseDelimiterException exception = assertThrows(ParseDelimiterException.class, () -> StringCalculator.add("//[4]"));
        assertEquals("Problem with parsing delimiter", exception.getMessage());
    }

    @Test
    void shouldThrowParseExceptionSecond() {
        ParseDelimiterException exception = assertThrows(ParseDelimiterException.class, () -> StringCalculator.add("//"));
        assertEquals("Problem with parsing delimiter", exception.getMessage());
    }
}
