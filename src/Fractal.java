import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public abstract class Fractal extends JPanel {

    protected int maxIterations;
    protected double minIm, maxIm, minReal, maxReal;
    protected int width, height, zoomXEnd, zoomYEnd, zoomXStart, zoomYStart, drawX, drawY;
    protected double zX = 0;
    protected double zY = 0;
    protected BufferedImage I;
    protected boolean drawRect;
    protected Graphics2D g2;

    public Fractal(int maxIterations, double minIm, double maxIm, double minReal, double maxReal) {
        this.maxIterations = maxIterations;
        this.minIm = minIm;
        this.maxIm = maxIm;
        this.minReal = minReal;
        this.maxReal = maxReal;
        this.setBounds(10, 20, 750, 750);

        width = this.getWidth();
        height = this.getHeight();

        I = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        calculatePoints(maxIterations, minIm, maxIm, minReal, maxReal);

        MouseListen listen = new MouseListen();
        this.addMouseListener(listen);
        this.addMouseMotionListener(listen);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2 = (Graphics2D) g;
        g2.drawImage(I, 0, 0, null);
        g2.setColor(Color.WHITE);
        if (drawRect) {
            g2.drawRect(drawX, drawY, Math.abs(zoomXEnd - zoomXStart), Math.abs(zoomYEnd - zoomYStart));
        }
    }

    // Each fractal has its own way to implement calculatePoints
    // This is where the drawing happens
    public abstract void calculatePoints(int maxIterations, double minIm, double maxIm, double minReal, double maxReal);

    protected Color generateColors(int noIterations, double maxIterations) {

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

    class MouseListen extends MouseAdapter {

        public void mousePressed(MouseEvent e) {
            drawRect = true;
            zoomXStart = e.getX();
            zoomYStart = e.getY();
        }

        public void mouseDragged(MouseEvent e) {
            zoomXEnd = e.getX();
            zoomYEnd = e.getY();
            if (((Math.abs(zoomXStart - zoomXEnd)) > 2) || ((Math.abs(zoomYStart - zoomYEnd)) > 2 )) {
                drawX = Math.min(zoomXEnd, zoomXStart);
                drawY = Math.min(zoomYEnd, zoomYStart);
                repaint();
            }
        }

        public void mouseReleased(MouseEvent e) {
            drawRect = false;
            zoomXEnd = e.getX();
            zoomYEnd = e.getY();
        }

    }
}
