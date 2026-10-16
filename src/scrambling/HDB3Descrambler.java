
package scrambling;

public class HDB3Descrambler {

    public int[] descramble(int[] signal) {

        if (signal == null) {
            throw new IllegalArgumentException(
                    "Signal cannot be null.");
        }

        int[] result = signal.clone();

        for (int i = 0; i + 3 < result.length; i++) {

            int a = result[i];
            int b = result[i + 1];
            int c = result[i + 2];
            int d = result[i + 3];

            // 000V: three zeros followed by a violation pulse.
            boolean zeroPattern = a == 0 && b == 0 && c == 0 && d != 0;

            // B00V: two equal-polarity nonzero pulses,
            // separated by two zeros.
            boolean bPattern = a != 0 && b == 0 && c == 0 && d == a;

            if (zeroPattern) {
                result[i] = 0;
                result[i + 1] = 0;
                result[i + 2] = 0;
                result[i + 3] = 0;
                i += 3;

            } else if (bPattern) {
                result[i] = 0;
                result[i + 1] = 0;
                result[i + 2] = 0;
                result[i + 3] = 0;
                i += 3;
            }
        }

        return result;
    }
}
