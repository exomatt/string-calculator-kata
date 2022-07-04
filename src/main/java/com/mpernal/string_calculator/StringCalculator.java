package com.mpernal.string_calculator;

import com.mpernal.string_calculator.exception.NegativesNotAllowedException;
import com.mpernal.string_calculator.exception.ParseDelimiterException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StringCalculator {
    private static final String DEFAULT_DELIMITER = "[,|\n]";
    private static final String REGEX_TO_EXTRACT_DELIMITER = "(?<=\\[)([^\\d\\]]+)(?=])";
    public static final int THE_BIGGEST_ALLOWED_NUMBER = 1000;

    public static int add(String text) throws NegativesNotAllowedException, ParseDelimiterException {
        if (text.isEmpty()) {
            return 0;
        }

        List<Integer> numbers = parseStringToNumbers(text);

        checkIfNegativeNumbers(numbers);

        return numbers.stream().filter(num -> num <= THE_BIGGEST_ALLOWED_NUMBER).mapToInt(Integer::intValue).sum();
    }

    private static void checkIfNegativeNumbers(List<Integer> numbers) throws NegativesNotAllowedException {
        List<Integer> negatives = numbers.stream().filter(num -> num < 0).toList();
        if (!negatives.isEmpty()) {
            String message = "negatives not allowed: " + negatives.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
            throw new NegativesNotAllowedException(message);
        }
    }

    private static List<Integer> parseStringToNumbers(String text) throws ParseDelimiterException {
        String delimiter = getDelimiter(text);
        if (!delimiter.equals(DEFAULT_DELIMITER))
            text = removeDelimiterDefinitionIfExists(text);
        return Arrays.stream(text.split(delimiter)).filter(s -> !s.isEmpty()).map(Integer::parseInt).collect(Collectors.toList());
    }

    private static String getDelimiter(String text) throws ParseDelimiterException {
        if (text.startsWith("//[")) {
            List<String> delimiters = new ArrayList<>();
            Pattern pattern = Pattern.compile(REGEX_TO_EXTRACT_DELIMITER);
            Matcher ma = pattern.matcher(text);
            while (ma.find()) {
                delimiters.add(ma.group());
            }
            if (delimiters.isEmpty()) {
                throw new ParseDelimiterException("Problem with parsing delimiter");
            }
            return "[" + String.join("|", delimiters) + "]";
        } else if (text.startsWith("//")) {
            if (text.length() < 3) {
                throw new ParseDelimiterException("Problem with parsing delimiter");
            }
            String delimiter = text.substring(2, 3);
            return "[" + delimiter + "]";
        } else {
            return DEFAULT_DELIMITER;
        }
    }

    private static String removeDelimiterDefinitionIfExists(String text) {
        if (text.startsWith("//")) {
            return text.split("\n")[1];
        } else {
            return text;
        }
    }
}
