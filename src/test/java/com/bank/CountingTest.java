package com.bank;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Character.valueOf;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;
import static org.assertj.core.api.Assertions.assertThat;

public class CountingTest {

    @Test
    void shouldTestCorrectOutput() {
        // given
        Map<Character, Integer> expectedOutput = Map.of('A', 3, 'B', 3, 'C', 3, 'D', 1, 'E', 1, 'F', 1);
        final var intput = "ABCABCABCDEF";
        // when
        final var result = count(intput);
        // then
        System.out.println("result = " + result);
        assertThat(result).isEqualTo(expectedOutput);
    }

    @Test
    void shouldTestStreamCorrectOutput() {
        final var input = "ABRACADABRA";
        Map<Character, Integer> frequency = count(input.chars().mapToObj(character -> (char) character));
        System.out.println("frequency = " + frequency);
        System.out.println("frequency2 = " + count2(input.chars().mapToObj(value -> (char) value)));
        System.out.println("frequency3 = " + countLong(input.chars().mapToObj(value -> (char) value)));
    }

    private static Map<Character, Integer> count(final String input) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("Should not be empty");
        }

        Map<Character, Integer> count = new HashMap<>();
        input
                .chars()
                .forEach(character -> {
                    count.merge(valueOf((char) character), 1, Integer::sum);
                });
        return count;
    }

    private static Map<Character, Integer> count(final Stream<Character> stream) {
        final Map<Character, Integer> count = new ConcurrentHashMap<>();
        stream
                .parallel()
                .forEach(character -> count.merge(character, 1, Integer::sum));
        return count;
    }

    private static Map<Character, Integer> count2(final Stream<Character> stream) {
        return stream
                .parallel()
                .collect(toMap(Character::charValue, character -> 1, Integer::sum));
    }

    private static Map<Character, Long> countLong(final Stream<Character> stream) {
        return stream
                .parallel()
                .collect(groupingBy(Character::charValue, Collectors.counting()));
    }


}
