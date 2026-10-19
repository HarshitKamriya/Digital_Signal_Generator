import java.util.Arrays;
import modulation.DeltaModulator;
import modulation.DeltaDemodulator;

public class DeltaModulationTest {

    public static void main(String[] args) {
        double[] samples = {
                0.1, 0.3, 0.6, 0.4, 0.0, -0.3, -0.6, -0.2, 0.4
        };

        double stepSize = 0.2;

        DeltaModulator encoder = new DeltaModulator(stepSize);
        DeltaDemodulator decoder = new DeltaDemodulator(stepSize);

        String bits = encoder.encode(samples);
        double[] reconstructed = decoder.decode(bits);

        System.out.println("Original samples:");
        System.out.println(Arrays.toString(samples));

        System.out.println("\nDelta-modulated bitstream:");
        System.out.println(bits);

        System.out.println("\nReconstructed staircase:");
        System.out.println(Arrays.toString(reconstructed));
    }
}
