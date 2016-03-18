import java.awt.*;

public class MandelbrotSet extends Fractal {

    public MandelbrotSet(int maxIterations, double minIm, double maxIm, double minReal, double maxReal) {
        super(maxIterations, minIm, maxIm, minReal, maxReal);
    }

    /*
     * The standard formula of Z(i+1) = z(i) * z(i) + c is implemented here
     * by creating a complex value from the x and y coordinates of each pixel
     * A temporary z value 'z2' is created which is used to store the calculations on the z
     * Squaring the value from the pixel and adding it to c for each of the iterations
     */

    @Override
    public void calculatePoints(int maxIterations, double minIm, double maxIm, double minReal, double maxReal) {
        Complex c;
        Complex z;
        Complex z2;
        int noIterations;
        Color paintColour;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                noIterations = 0; // Iterations always 0 at the start of a new value

                // zX and zY initially 0
                c = new Complex(zX, zY);

                zY = maxIm - y * (maxIm - minIm) / height;  // Create complex values
                zX = minReal + x * (maxReal - minReal) / width;
                z = new Complex(zX, zY);

                while (z.modulusSquared() < 4 && noIterations < maxIterations) {
                    z2 = z.square();
                    z2 = z2.add(c);
                    z = z2;
                    noIterations++;
                }

                paintColour = generateColors(noIterations, maxIterations);
                I.setRGB(x, y, paintColour.getRGB()); // Paint each individual pixel on the buffered image
            }
        }
        repaint();
    }
}
