
package scrambling;

public class HDB3 implements Scrambler {

    @Override
    public int[] scramble(int[] amiSignal) {

        if (amiSignal == null) {
            throw new IllegalArgumentException(
                    "AMI signal cannot be null.");
        }

        int[] result = amiSignal.clone();

        int lastPulse = -1;
        int pulseCount = 0;

        for (int i = 0; i < result.length; i++) {

            int value = result[i];

            if (value < -1 || value > 1) {
                throw new IllegalArgumentException(
                        "AMI signal must contain only -1, 0, and +1.");
            }

            if (value != 0) {
                lastPulse = value;
                pulseCount++;
            }

            // Check for four consecutive zeros.
            if (i + 3 < result.length
                    && result[i] == 0
                    && result[i + 1] == 0
                    && result[i + 2] == 0
                    && result[i + 3] == 0) {

                if (pulseCount % 2 == 0) {
                    // B00V: B is a normal AMI pulse.
                    int b = -lastPulse;
                    result[i] = b;
                    result[i + 1] = 0;
                    result[i + 2] = 0;
                    result[i + 3] = b;

                    lastPulse = b;
                } else {
                    // 000V: V has the same polarity
                    // as the previous nonzero pulse.
                    result[i] = 0;
                    result[i + 1] = 0;
                    result[i + 2] = 0;
                    result[i + 3] = lastPulse;
                }

                pulseCount = 0;
                i += 3;
            }
        }

        return result;
    }
}
