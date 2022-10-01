package inria.rmod;

import jdk.incubator.vector.DoubleVector;
import jdk.incubator.vector.VectorSpecies;

public class ArrayAdd {
    private static final VectorSpecies<Double> f64x2 = DoubleVector.SPECIES_128;

    public static void addScalar(double[] a, double[] b, double[] result) {
        assert (a.length == b.length);
        assert (a.length == result.length);

        for (int i = 0; i < a.length; i++) {
            result[i] = a[i] + b[i];
        }
    }

    public static void addVectorial(double[] a, double[] b, double[] result) {
        assert (a.length == b.length);
        assert (a.length == result.length);
        int len = a.length;
        int i = 0;

        // vectorized loop
        for (; i < f64x2.loopBound(len); i += f64x2.length()) {
            var v1 = DoubleVector.fromArray(f64x2, a, i);
            var v2 = DoubleVector.fromArray(f64x2, b, i);
            v1.add(v2).intoArray(result, i);
        }

        // scalar loop for the remaining elements
        for (; i < len; i++) {
            result[i] = a[i] + b[i];
        }
    }

    public static void addVectorialMasked(double[] a, double[] b, double[] result) {
        assert (a.length == b.length);
        assert (a.length == result.length);
        int len = a.length;
        for (int i = 0; i < len; i += f64x2.length()) {
            // builds a VectorMask that will ignore the out of bounds elements when loading/storing
            var mask = f64x2.indexInRange(i, len);
            var v1 = DoubleVector.fromArray(f64x2, a, i, mask);
            var v2 = DoubleVector.fromArray(f64x2, b, i, mask);

            v1.add(v2).intoArray(result, i, mask);
        }
    }
}