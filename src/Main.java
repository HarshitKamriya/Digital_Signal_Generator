
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
import modulation.DeltaModulator;
import modulation.DeltaDemodulator;

import scrambling.Scrambler;
import scrambling.B8ZS;
import scrambling.B8ZSDescrambler;
import scrambling.HDB3;
import scrambling.HDB3Descrambler;

import visualization.SignalPlotter;

public class Main {

    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(System.in)) {

            System.out.println("==================================");
            System.out.println(" DIGITAL SIGNAL ENCODER/DECODER");
            System.out.println("==================================");

            // 1. Select input type.
            System.out.println("\nSelect input type:");
            System.out.println("1. Digital input");
            System.out.println("2. Analog input (PCM / Delta Modulation)");
            System.out.print("Choice: ");

            int inputChoice = scanner.nextInt();

            if (inputChoice != 1 && inputChoice != 2) {
                System.out.println("Invalid input choice.");
                return;
            }

            String bits;
            double[] reconstructedSamples = null;

            // 2. Generate digital data or convert analog samples to bits.
            if (inputChoice == 1) {

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

                DigitalDataGenerator generator = new DigitalDataGenerator();
                bits = generator.generate(length, dataType);

            } else {

                System.out.println("\nSelect analog modulation:");
                System.out.println("1. Pulse Code Modulation (PCM)");
                System.out.println("2. Delta Modulation (DM)");
                System.out.print("Choice: ");

                int modulationChoice = scanner.nextInt();

                if (modulationChoice != 1 && modulationChoice != 2) {
                    System.out.println("Invalid modulation choice.");
                    return;
                }

                System.out.print("Enter number of analog samples: ");
                int sampleCount = scanner.nextInt();

                if (sampleCount < 1) {
                    System.out.println("Sample count must be positive.");
                    return;
                }

                double[] analogSamples = new double[sampleCount];

                System.out.println("Enter sample values in [-1, 1]:");

                for (int i = 0; i < sampleCount; i++) {
                    analogSamples[i] = scanner.nextDouble();

                    if (!Double.isFinite(analogSamples[i])
                            || analogSamples[i] < -1.0
                            || analogSamples[i] > 1.0) {
                        System.out.println(
                                "Each sample must be finite and in [-1, 1].");
                        return;
                    }
                }

                if (modulationChoice == 1) {

                    System.out.print("Enter PCM bits per sample (1-16): ");
                    int bitsPerSample = scanner.nextInt();

                    if (bitsPerSample < 1 || bitsPerSample > 16) {
                        System.out.println(
                                "Bits per sample must be between 1 and 16.");
                        return;
                    }

                    PCMEncoder pcmEncoder = new PCMEncoder(bitsPerSample);
                    PCMDecoder pcmDecoder = new PCMDecoder(bitsPerSample);

                    bits = pcmEncoder.encode(analogSamples);
                    reconstructedSamples = pcmDecoder.decode(bits);

                    System.out.println("\n========== PCM ==========");
                    System.out.println("Bits per sample: "
                            + pcmEncoder.getBitsPerSample());
                    System.out.println("Quantization levels: "
                            + pcmEncoder.getLevels());

                } else {

                    System.out.print("Enter DM step size (e.g. 0.2): ");
                    double stepSize = scanner.nextDouble();

                    if (!Double.isFinite(stepSize) || stepSize <= 0.0) {
                        System.out.println(
                                "Step size must be positive and finite.");
                        return;
                    }

                    DeltaModulator dmEncoder = new DeltaModulator(stepSize);
                    DeltaDemodulator dmDecoder = new DeltaDemodulator(stepSize);

                    bits = dmEncoder.encode(analogSamples);
                    reconstructedSamples = dmDecoder.decode(bits);

                    System.out.println("\n===== DELTA MODULATION =====");
                    System.out.println("Step size: " + stepSize);
                }

                System.out.println("Digital bitstream:");
                System.out.println(bits);

                System.out.println("Reconstructed analog samples:");
                System.out.println(Arrays.toString(reconstructedSamples));

                SignalPlotter.plotPCM(
                        modulationChoice == 1
                                ? "PCM Reconstructed Signal"
                                : "Delta Modulation Reconstructed Signal",
                        reconstructedSamples);
            }

            // 3. Display the digital bitstream and longest palindrome.
            System.out.println("\nDigital data stream:");
            System.out.println(bits);
            System.out.println("Bit count: " + bits.length());

            String palindrome = PalindromeFinder.findLongestPalindrome(bits);

            System.out.println("\nLongest palindromic substring:");
            System.out.println(palindrome);
            System.out.println("Palindrome length: " + palindrome.length());

            // 4. Select a line-coding scheme.
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

            // 5. Encode the bitstream.
            int[] encodedSignal = encoder.encode(bits);
            int[] transmittedSignal = encodedSignal.clone();

            boolean scramblingApplied = false;
            String selectedScheme = schemeName;

            // Scrambling is available only with AMI.
            if (schemeChoice == 5) {

                System.out.println("\nApply scrambling?");
                System.out.println("1. No scrambling");
                System.out.println("2. B8ZS");
                System.out.println("3. HDB3");
                System.out.print("Choice: ");

                int scrambleChoice = scanner.nextInt();

                if (scrambleChoice == 2) {
                    Scrambler scrambler = new B8ZS();
                    transmittedSignal = scrambler.scramble(encodedSignal);
                    selectedScheme = "AMI + B8ZS";
                    scramblingApplied = true;

                } else if (scrambleChoice == 3) {
                    Scrambler scrambler = new HDB3();
                    transmittedSignal = scrambler.scramble(encodedSignal);
                    selectedScheme = "AMI + HDB3";
                    scramblingApplied = true;

                } else if (scrambleChoice != 1) {
                    System.out.println("Invalid scrambling choice.");
                    return;
                }
            }

            // 6. Display the transmitted signal.
            System.out.println("\nSelected scheme: " + selectedScheme);
            System.out.println("Transmitted signal:");
            System.out.println(Arrays.toString(transmittedSignal));

            // 7. Descramble before line decoding.
            int[] signalForDecoding = transmittedSignal.clone();

            if (scramblingApplied) {

                if (selectedScheme.equals("AMI + B8ZS")) {
                    B8ZSDescrambler descrambler = new B8ZSDescrambler();
                    signalForDecoding = descrambler.descramble(signalForDecoding);

                } else {
                    HDB3Descrambler descrambler = new HDB3Descrambler();
                    signalForDecoding = descrambler.descramble(signalForDecoding);
                }
            }

            // 8. Recover the bitstream and verify the round trip.
            String decodedBits = decoder.decode(signalForDecoding);

            System.out.println("\nDecoded digital data:");
            System.out.println(decodedBits);

            boolean passed = bits.equals(decodedBits);

            System.out.println("Round-trip status: "
                    + (passed ? "PASS" : "FAIL"));

            // 9. Plot the transmitted line-coded signal.
            SignalPlotter.plotDigital(
                    selectedScheme + " Signal",
                    transmittedSignal);

            System.out.println("\nProcessing complete.");

        } catch (IllegalArgumentException e) {
            System.out.println("Input or processing error: " + e.getMessage());
        }
    }
}
