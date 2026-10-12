package scrambling;

public class B8ZS implements Scrambler {

    @Override
    public int[] scramble(int[] amiSignal) {

        if (amiSignal == null) {
            throw new IllegalArgumentException(
                    "AMI signal cannot be null.");
        }

        int[] result = amiSignal.clone();

        // Previous nonzero pulse; -1 is the initial reference.
        int lastPulse = -1;

        for (int i = 0; i < result.length; i++) {

            int value = result[i];

            if (value < -1 || value > 1) {
                throw new IllegalArgumentException(
                        "AMI signal must contain only -1, 0, and +1.");
            }

            // Update the previous pulse when a nonzero
            // AMI pulse is encountered.
            if (value != 0) {
                lastPulse = value;
            }

            // Check for eight consecutive zeros.
            if (i + 7 < result.length) {

                boolean eightZeros = true;

                for (int j = i; j < i + 8; j++) {
                    if (result[j] != 0) {
                        eightZeros = false;
                        break;
                    }
                }

                if (eightZeros) {

                    int p = lastPulse;

                    // B8ZS pattern: 000VB0VB
                    result[i + 3] = p;
                    result[i + 4] = -p;
                    result[i + 5] = 0;
                    result[i + 6] = -p;
                    result[i + 7] = p;

                    // The final pulse has the same polarity
                    // as the pulse before the substituted run.
                    lastPulse = p;

                    // Skip the substituted block.
                    i += 7;
                }
            }
        }

        return result;
    }
}