import java.awt.*;

public class MultiBrot extends Fractal {

    int value;

    public MultiBrot(int maxIterations, double minIm, double maxIm, double minReal, double maxReal, int value) {
        super(maxIterations, minIm, maxIm, minReal, maxReal);
        this.value = value;
    }

    /*
     * For a more specific definition of calculatePoints look at the MandelbrotSet class
     * This method squares a value constantly for a given amount of times given by the variable 'value'
     * This creates a fractal of multiple mandelbrots jointed together at a center point
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
                    z2 = z;
                    for (int i=1 ; i <= value ; i++) {
                        z2 = z.square();
                        z = z2;
                    }
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

    // This is used to set the value when used in the jslider
    public void setValue(int value) {
        this.value = value;
    }
}

