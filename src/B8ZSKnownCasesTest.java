
import java.util.Arrays;

import encoding.AMI;
import scrambling.B8ZS;
import scrambling.B8ZSDescrambler;

public class B8ZSKnownCasesTest {

    public static void main(String[] args) {
        String[] cases = {
                "10000",
                "110000",
                "1100000001",
                "1010000100001",
                "11000001000011000001"
        };

        AMI encoder = new AMI();
        B8ZS scrambler = new B8ZS();
        B8ZSDescrambler descrambler = new B8ZSDescrambler();

        for (String bits : cases) {
            int[] original = encoder.encode(bits);
            int[] scrambled = scrambler.scramble(original);
            int[] restored = descrambler.descramble(scrambled);

            String decoded = new encoding.AMI().toString();

            System.out.println("Input:     " + bits);
            System.out.println("AMI:       " + Arrays.toString(original));
            System.out.println("Scrambled: " + Arrays.toString(scrambled));
            System.out.println("Restored:  " + Arrays.toString(restored));

            // Convert the restored AMI signal back to bits.
            String recovered = new decoding.AMIDecoder().decode(restored);

            System.out.println("Recovered: " + recovered);
            System.out.println("Round trip: "
                    + (bits.equals(recovered) ? "PASS" : "FAIL"));
            System.out.println();
        }
    }
}
