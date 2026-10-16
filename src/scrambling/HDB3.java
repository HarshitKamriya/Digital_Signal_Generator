
package scrambling;

public class HDB3 implements Scrambler {

    @Override
    public int[] scramble(int[] amiSignal) {

        if (amiSignal == null) {
            throw new IllegalArgumentException(
                    "AMI signal cannot be null.");
        }

        int[] result = amiSignal.clone();

        int lastPulse = 0;
        int pulseCount = 0;

        for (int i = 0; i < result.length; i++) {

            int level = result[i];

            if (level != -1 && level != 0 && level != 1) {
                throw new IllegalArgumentException(
                        "Invalid AMI level at index " + i);
            }

            if (level != 0) {

                if (lastPulse != 0 && level == lastPulse) {
                    throw new IllegalArgumentException(
                            "Input is not a valid AMI signal at index " + i);
                }

                lastPulse = level;
                pulseCount++;
            }

            // Detect four consecutive zeros.
            if (i + 3 >= result.length
                    || result[i] != 0
                    || result[i + 1] != 0
                    || result[i + 2] != 0
                    || result[i + 3] != 0) {
                continue;
            }

            if (pulseCount % 2 == 1) {

                // Odd pulse count: 000V.
                // V has the same polarity as the previous pulse.
                result[i] = 0;
                result[i + 1] = 0;
                result[i + 2] = 0;
                result[i + 3] = lastPulse == 0 ? -1 : lastPulse;

                lastPulse = result[i + 3];

            } else {

                // Even pulse count: B00V.
                // B is opposite to the previous pulse;
                // V has the same polarity as B.
                int b = lastPulse == 0 ? 1 : -lastPulse;

                result[i] = b;
                result[i + 1] = 0;
                result[i + 2] = 0;
                result[i + 3] = b;

                lastPulse = b;
            }

            pulseCount = 0;
            i += 3;
        }

        return result;
    }
}
