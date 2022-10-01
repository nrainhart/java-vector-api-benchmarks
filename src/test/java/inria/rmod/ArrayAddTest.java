package inria.rmod;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class ArrayAddTest {
    @Test
    void testAddScalar() {
        double[] a = {1, 2, 3, 4, 5};
        double[] b = {10, 20, 30, 40, 50};
        double[] result = new double[a.length];

        ArrayAdd.addScalar(a, b, result);

        double[] expected = {11, 22, 33, 44, 55};

        assertArrayEquals(expected, result);
    }

    @Test
    void testAddVectorial() {
        double[] a = {1, 2, 3, 4, 5};
        double[] b = {10, 20, 30, 40, 50};
        double[] result = new double[a.length];

        ArrayAdd.addVectorial(a, b, result);

        double[] expected = {11, 22, 33, 44, 55};

        assertArrayEquals(expected, result);
    }

    @Test
    void testAddVectorialMasked() {
        double[] a = {1, 2, 3, 4, 5};
        double[] b = {10, 20, 30, 40, 50};
        double[] result = new double[a.length];

        ArrayAdd.addVectorialMasked(a, b, result);

        double[] expected = {11, 22, 33, 44, 55};

        assertArrayEquals(expected, result);
    }
}