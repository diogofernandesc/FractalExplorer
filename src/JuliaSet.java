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

    public JuliaSet(int maxIterations, double minIm, double maxIm, double minReal, double maxReal) {

        this.maxIterations = maxIterations;
        this.minIm = minIm;
        this.maxIm = maxIm;
        this.minReal = minReal;
        this.maxReal = maxReal;
        this.setBounds(10, 20, 400, 355);
        width = this.getWidth();
        height = this.getHeight();
        I = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(I, null, null);

    }

    public abstract void calculatePoints(Double clickedX, double clickedY);

    public Color generateColors(int noIterations) {

        if (noIterations < maxIterations && noIterations > 0) {
            int i = noIterations % 16;
            Color[] palette = new Color[16];
            palette[0] = new Color(66, 30, 15);
            palette[1] = new Color(25, 7, 26);
            palette[2] = new Color(9, 1, 47);
            palette[3] = new Color(4, 4, 73);
            palette[4] = new Color(0, 7, 100);
            palette[5] = new Color(12, 44, 138);
            palette[6] = new Color(24, 82, 177);
            palette[7] = new Color(57, 125, 209);
            palette[8] = new Color(134, 181, 229);
            palette[9] = new Color(211, 236, 248);
            palette[10] = new Color(241, 233, 191);
            palette[11] = new Color(248, 201, 95);
            palette[12] = new Color(255, 170, 0);
            palette[13] = new Color(204, 128, 0);
            palette[14] = new Color(153, 87, 0);
            palette[15] = new Color(106, 52, 3);
            return palette[i];
        } else {
            return Color.BLACK;
        }
    }


}