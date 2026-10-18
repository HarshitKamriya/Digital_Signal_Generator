
package visualization;

import javax.swing.*;
import java.awt.*;

public class SignalPlotter {

    public static void plotDigital(String title, int[] signal) {
        if (signal == null || signal.length == 0) {
            throw new IllegalArgumentException(
                    "Digital signal cannot be empty.");
        }

        double[] values = new double[signal.length];

        for (int i = 0; i < signal.length; i++) {
            values[i] = signal[i];
        }

        showPlot(title, values, true, "Sample index", "Signal level");
    }

    public static void plotPCM(String title, double[] samples) {
        if (samples == null || samples.length == 0) {
            throw new IllegalArgumentException(
                    "PCM samples cannot be empty.");
        }

        for (double sample : samples) {
            if (!Double.isFinite(sample)) {
                throw new IllegalArgumentException(
                        "Samples must be finite.");
            }
        }

        showPlot(title, samples, false,
                "Sample index", "Amplitude");
    }

    private static void showPlot(
            String title,
            double[] values,
            boolean digital,
            String xLabel,
            String yLabel) {

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(title);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.add(new PlotPanel(
                    values, digital, xLabel, yLabel));
            frame.setSize(900, 500);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private static class PlotPanel extends JPanel {

        private final double[] values;
        private final boolean digital;
        private final String xLabel;
        private final String yLabel;

        PlotPanel(double[] values, boolean digital,
                String xLabel, String yLabel) {
            this.values = values;
            this.digital = digital;
            this.xLabel = xLabel;
            this.yLabel = yLabel;
            setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);

            Graphics2D g = (Graphics2D) graphics.create();

            try {
                g.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                int left = 75;
                int right = getWidth() - 30;
                int top = 35;
                int bottom = getHeight() - 65;

                if (right <= left || bottom <= top) {
                    return;
                }

                double min = values[0];
                double max = values[0];

                for (double value : values) {
                    min = Math.min(min, value);
                    max = Math.max(max, value);
                }

                if (digital) {
                    min = Math.min(min, -1);
                    max = Math.max(max, 1);
                }

                if (min == max) {
                    min -= 1;
                    max += 1;
                }

                double padding = (max - min) * 0.15;
                min -= padding;
                max += padding;

                final double yMin = min;
                final double yMax = max;

                // Title
                g.setColor(Color.BLACK);
                g.setFont(new Font("SansSerif", Font.BOLD, 16));
                FontMetrics fm = g.getFontMetrics();
                g.drawString(titleText(),
                        (getWidth() - fm.stringWidth(titleText())) / 2,
                        22);

                // Horizontal grid and labels
                g.setFont(new Font("SansSerif", Font.PLAIN, 11));

                for (int i = 0; i <= 4; i++) {
                    double value = yMax
                            - i * (yMax - yMin) / 4.0;

                    int y = mapY(value, yMin, yMax, top, bottom);

                    g.setColor(new Color(220, 220, 220));
                    g.drawLine(left, y, right, y);

                    g.setColor(Color.DARK_GRAY);
                    g.drawString(
                            String.format("%.2f", value),
                            15, y + 4);
                }

                // Axes
                g.setColor(Color.BLACK);
                g.drawLine(left, top, left, bottom);
                g.drawLine(left, bottom, right, bottom);

                // Signal
                g.setColor(new Color(30, 100, 200));
                g.setStroke(new BasicStroke(2f));

                double width = right - left;
                double step = width / values.length;

                for (int i = 0; i < values.length; i++) {
                    int x1 = (int) Math.round(left + i * step);
                    int x2 = (int) Math.round(left + (i + 1) * step);

                    int y1 = mapY(
                            values[i], yMin, yMax, top, bottom);

                    // Horizontal segment for this sample.
                    g.drawLine(x1, y1, x2, y1);

                    // Vertical transition to the next sample.
                    if (i < values.length - 1) {
                        int y2 = mapY(
                                values[i + 1], yMin, yMax, top, bottom);
                        g.drawLine(x2, y1, x2, y2);
                    }
                }

                // Axis labels
                g.setColor(Color.BLACK);
                g.setFont(new Font("SansSerif", Font.PLAIN, 13));
                g.drawString(xLabel, (left + right) / 2, getHeight() - 20);

                g.rotate(-Math.PI / 2);
                g.drawString(yLabel, -(top + bottom) / 2 - 25, 20);
                g.rotate(Math.PI / 2);

            } finally {
                g.dispose();
            }
        }

        private String titleText() {
            return digital
                    ? "Digital Line-Coded Signal"
                    : "Reconstructed PCM Staircase Signal";
        }

        private int mapY(double value, double min, double max,
                int top, int bottom) {
            return (int) Math.round(
                    bottom - (value - min) / (max - min)
                            * (bottom - top));
        }
    }
}
