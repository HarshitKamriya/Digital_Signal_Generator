
import scrambling.HDB3;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {

        HDB3 encoder = new HDB3();

        int[] oddPulseCase = { 1, 0, 0, 0, 0 };
        int[] evenPulseCase = { 1, -1, 0, 0, 0, 0 };

        System.out.println("Odd pulse count:");
        System.out.println(
                Arrays.toString(encoder.scramble(oddPulseCase)));

        System.out.println("\nEven pulse count:");
        System.out.println(
                Arrays.toString(encoder.scramble(evenPulseCase)));
    }
}
