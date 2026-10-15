
import encoding.LineEncoder;
import encoding.Manchester;
import encoding.DifferentialManchester;

import decoding.LineDecoder;
import decoding.ManchesterDecoder;
import decoding.DifferentialManchesterDecoder;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {

        String bits = "101100101";

        test(
                bits,
                new Manchester(),
                new ManchesterDecoder(),
                "Manchester");

        test(
                bits,
                new DifferentialManchester(),
                new DifferentialManchesterDecoder(),
                "Differential Manchester");
    }

    private static void test(
            String bits,
            LineEncoder encoder,
            LineDecoder decoder,
            String scheme) {

        int[] signal = encoder.encode(bits);
        String decoded = decoder.decode(signal);

        System.out.println("\nScheme: " + scheme);
        System.out.println("Original: " + bits);
        System.out.println(
                "Signal:   " + Arrays.toString(signal));
        System.out.println("Decoded:  " + decoded);
        System.out.println(
                "Correct:  " + bits.equals(decoded));
    }
}
