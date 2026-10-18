
import java.util.Arrays;
import java.util.Scanner;

import data.DataType;
import data.DigitalDataGenerator;
import data.PalindromeFinder;

import encoding.LineEncoder;
import encoding.NRZL;
import encoding.NRZI;
import encoding.Manchester;
import encoding.DifferentialManchester;
import encoding.AMI;

import decoding.LineDecoder;
import decoding.NRZLDecoder;
import decoding.NRZIDecoder;
import decoding.ManchesterDecoder;
import decoding.DifferentialManchesterDecoder;
import decoding.AMIDecoder;

import modulation.PCMEncoder;
import modulation.PCMDecoder;
import visualization.SignalPlotter;

public class Main {

    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(System.in)) {

            System.out.println("==================================");
            System.out.println(" DIGITAL SIGNAL ENCODER/DECODER");
            System.out.println("==================================");

            // 1. Choose data pattern.
            System.out.println("\nSelect data pattern:");
            System.out.println("1. Completely random");
            System.out.println("2. Random with four consecutive zeros");
            System.out.println("3. Random with eight consecutive zeros");
            System.out.print("Choice: ");

            int dataChoice = scanner.nextInt();

            DataType dataType;

            switch (dataChoice) {
                case 1:
                    dataType = DataType.RANDOM;
                    break;
                case 2:
                    dataType = DataType.FOUR_ZEROS;
                    break;
                case 3:
                    dataType = DataType.EIGHT_ZEROS;
                    break;
                default:
                    System.out.println("Invalid data choice.");
                    return;
            }

            System.out.print("Enter bit sequence length (minimum 8): ");
            int length = scanner.nextInt();

            if (length < 8) {
                System.out.println("Length must be at least 8.");
                return;
            }

            // 2. Generate digital data.
            DigitalDataGenerator generator = new DigitalDataGenerator();

            String bits = generator.generate(length, dataType);

            System.out.println("\nGenerated digital data:");
            System.out.println(bits);
            System.out.println("Length: " + bits.length());

            // 3. Find the longest palindrome.
            String palindrome = PalindromeFinder.findLongestPalindrome(bits);

            System.out.println("\nLongest palindromic substring:");
            System.out.println(palindrome);
            System.out.println(
                    "Palindrome length: " + palindrome.length());

            // 4. Choose a line-coding scheme.
            System.out.println("\nSelect line-coding scheme:");
            System.out.println("1. NRZ-L");
            System.out.println("2. NRZ-I");
            System.out.println("3. Manchester");
            System.out.println("4. Differential Manchester");
            System.out.println("5. AMI");
            System.out.print("Choice: ");

            int schemeChoice = scanner.nextInt();

            LineEncoder encoder;
            LineDecoder decoder;
            String schemeName;

            switch (schemeChoice) {
                case 1:
                    encoder = new NRZL();
                    decoder = new NRZLDecoder();
                    schemeName = "NRZ-L";
                    break;
                case 2:
                    encoder = new NRZI();
                    decoder = new NRZIDecoder();
                    schemeName = "NRZ-I";
                    break;
                case 3:
                    encoder = new Manchester();
                    decoder = new ManchesterDecoder();
                    schemeName = "Manchester";
                    break;
                case 4:
                    encoder = new DifferentialManchester();
                    decoder = new DifferentialManchesterDecoder();
                    schemeName = "Differential Manchester";
                    break;
                case 5:
                    encoder = new AMI();
                    decoder = new AMIDecoder();
                    schemeName = "AMI";
                    break;
                default:
                    System.out.println("Invalid scheme choice.");
                    return;
            }

            // 5. Encode and decode the generated data.
            int[] signal = encoder.encode(bits);
            String decodedBits = decoder.decode(signal);

            System.out.println("\nSelected scheme: " + schemeName);
            System.out.println("Encoded signal:");
            System.out.println(Arrays.toString(signal));

            System.out.println("\nDecoded data:");
            System.out.println(decodedBits);

            System.out.println(
                    "Round-trip status: "
                            + (bits.equals(decodedBits) ? "PASS" : "FAIL"));

            // 6. Run PCM encoding and decoding.
            double[] analogSamples = {
                    -1.0, -0.75, -0.5, -0.25, 0.0,
                    0.25, 0.5, 0.75, 1.0,
                    0.5, 0.0, -0.5, -1.0
            };

            int bitsPerSample = 3;

            PCMEncoder pcmEncoder = new PCMEncoder(bitsPerSample);

            PCMDecoder pcmDecoder = new PCMDecoder(bitsPerSample);

            String pcmBits = pcmEncoder.encode(analogSamples);
            double[] reconstructed = pcmDecoder.decode(pcmBits);

            System.out.println("\n========== PCM ==========");
            System.out.println("Bits per sample: " + bitsPerSample);
            System.out.println("Quantization levels: "
                    + pcmEncoder.getLevels());
            System.out.println("PCM bitstream: " + pcmBits);
            System.out.println("Reconstructed samples:");
            System.out.println(Arrays.toString(reconstructed));

            // 7. Display plots.
            SignalPlotter.plotDigital(
                    schemeName + " Signal", signal);

            SignalPlotter.plotPCM(
                    "PCM Reconstructed Signal", reconstructed);

            System.out.println("\nProcessing complete.");
        }
    }
}
