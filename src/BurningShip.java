import java.awt.*;
public class BurningShip extends Fractal {

    public BurningShip(int maxIterations, double minIm, double maxIm, double minReal, double maxReal) {
        super(maxIterations, minIm, maxIm, minReal, maxReal);
    }

    /*
     * Implementation of the formula where |re z(i)| = Math.abs(z.getReal())
     * |im Z(i)| = Math.abs(z.getImaginary())
     * For a more detailed explanation of this method look at calculatePoints on the MandelbrotSet class
     */

    @Override
    public void calculatePoints(int maxIterations, double minIm, double maxIm, double minReal, double maxReal) {
        Complex c;
        Complex z;
        int noIterations;
        Color paintColour;

        // Loops for every pixel on panel
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                noIterations = 0;

                // zX and zY initially 0
                c = new Complex(zX, zY);

                // Conversion of the coordinates to complex points
                zY = maxIm - y * (maxIm - minIm) / height;
                zX = minReal + x * (maxReal - minReal) / width;
                z = new Complex(zX, zY);

                while (z.modulusSquared() < 4 && noIterations < maxIterations) {
                    z = new Complex(Math.abs(z.getReal()), Math.abs(z.getImaginary()));
                    z = z.square();
                    z = z.add(c);
                    noIterations++;
                }

                paintColour = generateColors(noIterations, maxIterations);
                I.setRGB(x, y, paintColour.getRGB());
            }
        }
        repaint();
    }

}
