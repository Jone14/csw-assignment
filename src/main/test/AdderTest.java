/*
package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.csw.main.Adder;

public class AdderTest {

    private Adder adder;

    @BeforeEach
    public void setUp() {
        adder = new Adder();
    }

    @Test
    public void whenAddTwoZeros_ThenSumIsZero() {
        Assertions.assertEquals(0, adder.add(0, 0));
    }

    @Test
    public void whenAddFirstZeroSecondNegative_ThenSumIsEqualToSecond() {
        Assertions.assertEquals(-1, adder.add(0, -1));
    }

    @Test
    public void whenAddFirstNegativeSecondZero_ThenSumIsEqualToFirst() {
        Assertions.assertEquals(-1, adder.add(-1, 0));
    }

    @Test
    public void whenTwoNegatives_ThenSumIsCorrect() {
        Assertions.assertEquals(-3, adder.add(-1, -2));
    }

    @Test
    public void whenAddFirstZeroSecondPositive_ThenSumIsEqualToSecond() {
        Assertions.assertEquals(1, adder.add(0, 1));
    }

    @Test
    public void whenAddFirstPositiveSecondZero_ThenSumIsEqualToFirst() {
        Assertions.assertEquals(1, adder.add(1, 0));
    }

    @Test
    public void whenTwoPositives_ThenSumIsCorrect() {
        Assertions.assertEquals(3, adder.add(1, 2));
    }

    @Test
    public void whenAddFirstPositiveSecondNegative_ThenSumIsCorrect() {
        Assertions.assertEquals(0, adder.add(1, -1));
    }

    @Test
    public void whenAddFirstNegativeSecondPositive_ThenSumIsCorrect() {
        Assertions.assertEquals(0, adder.add(-1, 1));
    }
}*/
