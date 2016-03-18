import java.awt.*;

public class JuliaMultibrot extends JuliaSet {

    int value;

    public JuliaMultibrot(int maxIterations, double minIm, double maxIm, double minReal, double maxReal) {
        super(maxIterations, minIm, maxIm, minReal, maxReal);
    }

    /*
     * This method is interesting as it is squaring the square for as many times as is set
     * by the variable 'value'
     * I used a JSlider to implement the different multibrots to show how this works
     * It is multiple mandelbrots together in one
     */
    @Override
    public void calculatePoints(Double clickedX, double clickedY) {
        Complex c = new Complex(clickedX, clickedY);
        Complex z;
        Complex z2;
        int noIterations;
        Color paintColour;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                noIterations = 0;

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
                paintColour = generateColors(noIterations);
                I.setRGB(x, y, paintColour.getRGB());
            }
        }
        repaint();
    }

    public void setValue(int value) {
        this.value = value;
    }
}
