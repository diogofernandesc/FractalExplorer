import java.awt.*;

public class Tricorn extends Fractal {

    public Tricorn(int maxIterations, double minIm, double maxIm, double minReal, double maxReal) {
        super(maxIterations, minIm, maxIm, minReal, maxReal);
    }

    /*
     * Here I simply used the squareTricorn() method instead of the normal square
     * method for the mandelbrot
     * The squareTricorn() is almost identical to the mandelbrot except that it is z(i+1) = z(i)^-2 + c
     * This means that the imaginary value is calculated via -2ab
     * How this is implemented is shown in the Complex class
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
                noIterations = 0;

                // zX and zY initially 0
                c = new Complex(zX, zY);

                zY = maxIm - y * (maxIm - minIm) / height;
                zX = minReal + x * (maxReal - minReal) / width;
                z = new Complex(zX, zY);

                while (z.modulusSquared() < 4 && noIterations < maxIterations) {
                    z2 = z.squareTricorn();
                    z2 = z2.add(c);
                    z = z2;
                    noIterations++;
                }

                paintColour = generateColors(noIterations, maxIterations);
                I.setRGB(x, y, paintColour.getRGB());
            }
        }
        repaint();
    }
}