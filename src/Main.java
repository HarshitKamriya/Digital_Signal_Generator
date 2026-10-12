import java.util.Arrays;

import data.DataType;
import data.DigitalDataGenerator;
import data.PalindromeFinder;
import encoding.AMI;
import encoding.DifferentialManchester;
import encoding.LineEncoder;
import encoding.Manchester;
import encoding.NRZI;
import encoding.NRZL;
import scrambling.B8ZS;
import scrambling.Scrambler;

public class Main {
    public static void main(String[] args) {
        // testing digital data generator
        // DigitalDataGenerator generator = new DigitalDataGenerator();

        // String randomData = generator.generate(12,DataType.RANDOM);

        // String fixedfourData = generator.generate(12, DataType.FOUR_ZEROS);
        // String fixedeightData = generator.generate(12, DataType.EIGHT_ZEROS);

        // System.out.println("Random Data: ");
        // System.out.println(randomData);

        // System.out.println();

        // System.out.println("Data with 4 consecutive zero : ");
        // System.out.println(fixedfourData);
        // System.out.println();

        // System.out.println("Data with 8 consecutive zero : ");
        // System.out.println(fixedeightData);

        // DigitalDataGenerator generator = new DigitalDataGenerator();
        // PalindromeFinder palindromeFinder = new PalindromeFinder();


        // String data = generator.generate(32, DataType.RANDOM);

        // String palindrome = palindromeFinder.findLongestPalindrome(data);
        // System.out.println("Digital DAta: ");
        // System.out.println(data);

        // System.out.println();

        // System.out.println("Longest Palindrome: ");
        // System.out.println(palindrome);
        // System.out.println();



        // String bits = "10110010";
        // LineEncoder encoder = new NRZL();

        // int[] signal = encoder.encode(bits);

        // System.out.println("Digital Data: "+bits);

        // System.out.println(
        //     "Encoded Signal: " + Arrays.toString(signal)
        // );


        // // connecting to digital data generator
        // DigitalDataGenerator generator = new DigitalDataGenerator();

        // PalindromeFinder finder = new PalindromeFinder();

        // // Generate the digital stream
        // String bits = generator.generate(
        //         32, DataType.EIGHT_ZEROS);

        // // Find its longest palindrome
        // String palindrome = finder.findLongestPalindrome(bits);

        // // Encode using NRZ-L
        // LineEncoder encoder = new NRZL();
        // int[] signal = encoder.encode(bits);

        // System.out.println("Digital Data: " + bits);
        // System.out.println("Longest Palindrome: " + palindrome);
        // System.out.println(
        //         "NRZ-L Signal: " + Arrays.toString(signal));


        

        // testing nrzi
        // String bits = "10110";

        // LineEncoder encoder = new NRZI();

        // int[] signal = encoder.encode(bits);

        // System.out.println("Digital Data: " + bits);
        // System.out.println(
        //     "NRZ-I Signal: " + Arrays.toString(signal)
        // );




        // verifying manchester
        // String bits = "1010";

        // LineEncoder encoder = new Manchester();

        // int[] signal = encoder.encode(bits);

        // System.out.println("Digital Data: " + bits);
        // System.out.println(
        //     "Manchester Signal: " + Arrays.toString(signal)
        // );


        // verifying differential manchester
        // String bits = "1010";

        // LineEncoder encoder = new DifferentialManchester();

        // int[] signal = encoder.encode(bits);

        // System.out.println("Digital Data: " + bits);
        // System.out.println(
        //     "Differential Manchester Signal: "
        //     + Arrays.toString(signal)
        // );



        // verifying AMI
        // String bits = "10110011";

        // LineEncoder encoder = new AMI();

        // int[] signal = encoder.encode(bits);

        // System.out.println("Digital Data: " + bits);
        // System.out.println(
        //     "AMI Signal: " + Arrays.toString(signal)
        // );


        // testing B8ZS

        int[] amiSignal = {
            1, 0, 0, 0, 0, 0, 0, 0, 0
        };

        Scrambler scrambler = new B8ZS();

        int[] scrambled = scrambler.scramble(amiSignal);

        System.out.println(
            "Original AMI: " + Arrays.toString(amiSignal)
        );

        System.out.println(
            "B8ZS Signal:  " + Arrays.toString(scrambled)
        );




    }
}
