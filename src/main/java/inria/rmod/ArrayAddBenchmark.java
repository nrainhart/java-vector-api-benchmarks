package inria.rmod;

import java.util.concurrent.ThreadLocalRandom;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.profile.DTraceAsmProfiler;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@BenchmarkMode(Mode.Throughput)
public class ArrayAddBenchmark {

    @State(Scope.Benchmark)
    public static class Input {

        @Param({ "6", "48" })
        int size;
        double[] left;
        double[] right;
        double[] result;

        @Setup
        public void setup() {
            var random = ThreadLocalRandom.current();

            left = new double[size];
            right = new double[size];
            result = new double[size];

            for (int i = 0; i < size; i++) {
                left[i] = random.nextInt(10);
                right[i] = random.nextInt(10);
                result[i] = 0.0;
            }
        }
    }

    @Benchmark
    public void benchmarkAddScalar(Input input, Blackhole blackhole) {
        ArrayAdd.addScalar(input.left, input.right, input.result);
        blackhole.consume(input.result);
    }

    @Benchmark
    public void benchmarkAddVectorialWithScalarRemainder(Input input, Blackhole blackhole) {
        ArrayAdd.addVectorial(input.left, input.right, input.result);
        blackhole.consume(input.result);
    }

    @Benchmark
    public void benchmarkAddVectorialWithRangeMask(Input input, Blackhole blackhole) {
        ArrayAdd.addVectorialMasked(input.left, input.right, input.result);
        blackhole.consume(input.result);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .jvmArgs("--add-modules=jdk.incubator.vector",
                        "-XX:MaxInlineLevel=32",
                        "-XX:+UnlockExperimentalVMOptions",
                        "-XX:+UnlockDiagnosticVMOptions",
                        "-XX:+TrustFinalNonStaticFields",
                        // disable autovectorization (in pure scalar code)
                        "-XX:-UseSuperWord",
                        // disable loop unrolling
                        "-XX:LoopUnrollLimit=0",
                        // print assembly code for bytecoded and native methods
                        //"-XX:+PrintAssembly",
                        //"-XX:PrintAssemblyOptions=arm"
                        // print assembly for individual methods
                        "-XX:CompileCommand=print ArrayAdd.*"
                        // print names of methods being compiled
                        //"-XX:+PrintCompilation"
                        // for additional debug info, see https://wiki.openjdk.org/display/HotSpot/PrintAssembly
                )
                .include(ArrayAddBenchmark.class.getSimpleName())
                .threads(1)
                .forks(1)
                //.addProfiler(DTraceAsmProfiler.class)
                .build();

        new Runner(opt).run();
    }

}