
import data.DataType;
import data.DigitalDataGenerator;
import data.PalindromeFinder;

import encoding.LineEncoder;
import encoding.NRZL;
import encoding.NRZI;
import encoding.Manchester;
import encoding.DifferentialManchester;
import encoding.AMI;

import scrambling.Scrambler;
import scrambling.B8ZS;
import scrambling.HDB3;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        DigitalDataGenerator generator = new DigitalDataGenerator();

        PalindromeFinder palindromeFinder = new PalindromeFinder();

        List<LineEncoder> encoders = Arrays.asList(
                new NRZL(),
                new NRZI(),
                new Manchester(),
                new DifferentialManchester(),
                new AMI());

        String[] encoderNames = {
                "NRZ-L",
                "NRZ-I",
                "Manchester",
                "Differential Manchester",
                "AMI"
        };

        System.out.println("================================");
        System.out.println(" DIGITAL SIGNAL ENCODER");
        System.out.println("================================");

        int length = readInt(
                "Enter digital data length: ", 1, 100000);

        System.out.println("\nChoose data generation type:");
        System.out.println("1. Completely random data");
        System.out.println("2. Data containing 4 consecutive zeros");
        System.out.println("3. Data containing 8 consecutive zeros");

        int dataChoice = readInt("Your choice: ", 1, 3);

        DataType dataType = Arrays.asList(
                DataType.RANDOM,
                DataType.FOUR_ZEROS,
                DataType.EIGHT_ZEROS).get(dataChoice - 1);

        if (dataChoice == 2 && length < 4 ||
                dataChoice == 3 && length < 8) {
            System.out.println(
                    "Length is too short for the selected pattern.");
            return;
        }

        String bits = generator.generate(length, dataType);

        System.out.println("\nGenerated digital data:");
        System.out.println(bits);

        String palindrome = palindromeFinder.findLongestPalindrome(bits);

        System.out.println("\nLongest palindrome:");
        System.out.println(palindrome);

        System.out.println("\nChoose line encoding:");

        for (int i = 0; i < encoderNames.length; i++) {
            System.out.println((i + 1) + ". " + encoderNames[i]);
        }

        int encodingChoice = readInt("Your choice: ", 1, encoders.size());

        LineEncoder encoder = encoders.get(encodingChoice - 1);

        int[] signal = encoder.encode(bits);

        System.out.println("\nSelected scheme: "
                + encoderNames[encodingChoice - 1]);

        System.out.println("Encoded signal:");
        System.out.println(Arrays.toString(signal));

        // Scrambling is available only for AMI.
        if (encoder instanceof AMI) {

            System.out.println("\nChoose scrambling:");
            System.out.println("1. No scrambling");
            System.out.println("2. B8ZS");
            System.out.println("3. HDB3");

            int scrambleChoice = readInt("Your choice: ", 1, 3);

            if (scrambleChoice == 2) {

                Scrambler scrambler = new B8ZS();
                int[] scrambled = scrambler.scramble(signal);

                System.out.println("\nB8ZS signal:");
                System.out.println(Arrays.toString(scrambled));

            } else if (scrambleChoice == 3) {

                Scrambler scrambler = new HDB3();
                int[] scrambled = scrambler.scramble(signal);

                System.out.println("\nHDB3 signal:");
                System.out.println(Arrays.toString(scrambled));
            }
        }

        System.out.println("\nEncoding completed.");
    }

    private static int readInt(
            String message, int min, int max) {

        while (true) {
            System.out.print(message);

            String input = scanner.nextLine().trim();

            try {
                int value = Integer.parseInt(input);

                if (value >= min && value <= max) {
                    return value;
                }

            } catch (NumberFormatException ignored) {
                // Ask again for invalid input.
            }

            System.out.println(
                    "Enter an integer between "
                            + min + " and " + max + ".");
        }
    }
}
