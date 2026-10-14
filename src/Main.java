
import encoding.LineEncoder;
import encoding.NRZL;
import encoding.NRZI;

import decoding.LineDecoder;
import decoding.NRZLDecoder;
import decoding.NRZIDecoder;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {

        String originalBits = "101100101";

        testDecoder(
                originalBits,
                new NRZL(),
                new NRZLDecoder(),
                "NRZ-L");

        testDecoder(
                originalBits,
                new NRZI(),
                new NRZIDecoder(),
                "NRZ-I");
    }

    private static void testDecoder(
            String bits,
            LineEncoder encoder,
            LineDecoder decoder,
            String scheme) {

        int[] signal = encoder.encode(bits);

        String decodedBits = decoder.decode(signal);

        System.out.println("\nScheme: " + scheme);
        System.out.println("Original: " + bits);
        System.out.println(
                "Signal:   " + Arrays.toString(signal));
        System.out.println("Decoded:  " + decodedBits);
        System.out.println(
                "Correct:  " + bits.equals(decodedBits));
    }
}
