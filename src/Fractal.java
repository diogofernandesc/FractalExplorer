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

    /*
     * Every fractal takes a number of maximum iterations
     * minimum imaginary axis value, maximum imaginary axis value
     * minimum real axis value, maximum real axis value
     * The buffered is constructed with the height of this panel
     * Mouse listeners used for the rectangle on screen
     */

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

    /*
     * I get a graphics2D object to draw the buffered image onto with
     * Setting the color for the rectangle that is drawn for the zoom
     * Math.abs in drawRect is used to allow the rectangle to draw in both directions
     */
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

    /*
     * The color is chosen based on the iteration which has 16 possible values, decided based on the mod
     * This allows for almost a gradient
     * A similar color scheme to that used in the Ultra Fractal software
     */

    protected Color generateColors(int noIterations, double maxIterations) {

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

    /*
     * drawRect is a control variable that tells the adapter when to draw the rectangle in the paint method
     * When mouse is pressed you get initial x and y
     * As mouse is dragged you get continuous new end coordinates a rectangle is drawn every time
     * However buffered image is repainted every time so seems like one rectangle
     * If statement here used so that a click on the panel does not lead to accidental zoom
     * When mouse released event happens the boolean is set to false so that the rectangle does not remain on the panel
     */

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
