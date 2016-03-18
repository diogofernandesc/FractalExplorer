import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class JuliaSet extends JPanel {

    protected int width;
    protected int height;
    protected double zX = 0; //500
    protected double zY = 0; // 960
    protected BufferedImage I;
    protected int maxIterations;
    protected double minIm;
    protected double maxIm;
    protected double minReal;
    protected double maxReal;

    /*
     * Julia set is the same in that it takes the same values as the main fractals
     */
    public JuliaSet(int maxIterations, double minIm, double maxIm, double minReal, double maxReal) {

        this.maxIterations = maxIterations;
        this.minIm = minIm;
        this.maxIm = maxIm;
        this.minReal = minReal;
        this.maxReal = maxReal;
        this.setBounds(10, 20, 400, 340);
        width = this.getWidth();
        height = this.getHeight();
        I = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    /*
     * The julia is drawn to a buffered image
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(I, null, null);

    }

    // Each julia set is implemented differently
    public abstract void calculatePoints(Double clickedX, double clickedY);

    /*
     * Same colouring as the standard main fractals
     * An explanation of this is provided in the Fractal class
     */

    public Color generateColors(int noIterations) {

        if (noIterations < maxIterations && noIterations > 0) {
            int i = noIterations % 16;
            Color[] palette = new Color[16];
            palette[0] = new Color(66, 30, 16);
            palette[1] = new Color(25, 7, 27);
            palette[2] = new Color(9, 1, 48);
            palette[3] = new Color(4, 4, 74);
            palette[4] = new Color(0, 6, 100);
            palette[5] = new Color(12, 44, 139);
            palette[6] = new Color(24, 82, 178);
            palette[7] = new Color(57, 125, 210);
            palette[8] = new Color(134, 181, 230);
            palette[9] = new Color(211, 236, 249);
            palette[10] = new Color(241, 233, 192);
            palette[11] = new Color(248, 201, 96);
            palette[12] = new Color(255, 171, 0);
            palette[13] = new Color(204, 129, 0);
            palette[14] = new Color(153, 88, 0);
            palette[15] = new Color(106, 53, 3);
            return palette[i];
        } else {
            return Color.BLACK;
        }
    }


}