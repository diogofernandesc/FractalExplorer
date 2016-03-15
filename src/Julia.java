import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

class Julia extends JPanel {

    int width;
    int height;
    double zX = 0; //500
    double zY = 0; // 960
    private BufferedImage I;
    private int maxIterations;
    private double minIm;
    private double maxIm;
    private double minReal;
    private double maxReal;

    public Julia(int maxIterations, double minIm, double maxIm, double minReal, double maxReal) {

        this.maxIterations = maxIterations;
        this.minIm = minIm;
        this.maxIm = maxIm;
        this.minReal = minReal;
        this.maxReal = maxReal;
        this.setBounds(10, 20, 400, 330);
        width = this.getWidth();
        height = this.getHeight();
        I = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //g.drawLine(x, y, x, y);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(I, null, null);

    }

    protected void calculatePoints(double clickedX, double clickedY) {
        Complex c = new Complex(clickedX, clickedY);
        //Complex c = new Complex(-0.7433962264150944, -0.0758762886597939);
        Complex z;
        Complex d;
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
                    z2 = z.square();
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

    public Color generateColors(int noIterations) {

        if (noIterations < maxIterations && noIterations > 0) {
            int i = noIterations % 16;
            Color[] pallete = new Color[16];
            pallete[0] = new Color(66, 30, 15);
            pallete[1] = new Color(25, 7, 26);
            pallete[2] = new Color(9, 1, 47);
            pallete[3] = new Color(4, 4, 73);
            pallete[4] = new Color(0, 7, 100);
            pallete[5] = new Color(12, 44, 138);
            pallete[6] = new Color(24, 82, 177);
            pallete[7] = new Color(57, 125, 209);
            pallete[8] = new Color(134, 181, 229);
            pallete[9] = new Color(211, 236, 248);
            pallete[10] = new Color(241, 233, 191);
            pallete[11] = new Color(248, 201, 95);
            pallete[12] = new Color(255, 170, 0);
            pallete[13] = new Color(204, 128, 0);
            pallete[14] = new Color(153, 87, 0);
            pallete[15] = new Color(106, 52, 3);
            return pallete[i];
        } else {
            return Color.BLACK;
        }
    }


}