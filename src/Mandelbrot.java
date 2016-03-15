import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

class Mandelbrot extends JPanel {

    private int width;
    private int height;
    private int x;
    private int y;
    private double cX = 0;
    private double cY = 0;
    private double zX = 0; //500
    private double zY = 0; // 960
    private BufferedImage I;
    private int maxIterations;
    private double minIm;
    private double maxIm;
    private double minReal;
    private double maxReal;
    double clickedY;
    double clickedX;
    int zoomXEnd;
    int zoomYEnd;
    int zoomXStart;
    int zoomYStart;
    int drawX;
    int drawY;
    private boolean drawRect;
    Graphics2D g2d;
    Graphics2D g2;


    public Mandelbrot(int maxIterations, double minIm, double maxIm, double minReal, double maxReal) {

        this.maxIterations = maxIterations;
        this.minIm = minIm;
        this.maxIm = maxIm;
        this.minReal = minReal;
        this.maxReal = maxReal;
        this.setBounds(10, 20, 750, 750);

        width = this.getWidth();
        height = this.getHeight();
        I = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        g2d = I.createGraphics();
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

    public Graphics2D getGraphics2D() {
        return g2d;
    }

    protected void calculatePoints(int maxIterations, double minIm, double maxIm, double minReal, double maxReal) {
        Complex c;
        Complex z;
        Complex z2;
        int noIterations;
        double noIterationsColor;
        double logZn;
        double nu;
        Color[] colours;
        Color color1;
        Color color2;
        int colorIndex;
        Color paintColour;
        Color paintColourPrevious;
        Color painted;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                noIterations = 0;

                // zX and zY initially 0
                c = new Complex(zX, zY);

                zY = maxIm - y * (maxIm - minIm) / height;
                zX = minReal + x * (maxReal - minReal) / width;
                z = new Complex(zX, zY);

                while (z.modulusSquared() < 4 && noIterations < maxIterations) {
                    z2 = z.square();
                    z2 = z2.add(c);
                    z = z2;
                    noIterations++;
                }
//                logZn = Math.log(zX * zX + zY * zY) / 2;
//                nu = Math.log(logZn / Math.log(2) / Math.log(2));
//                noIterationsColor = noIterations;
//                noIterationsColor = noIterationsColor + 1 - nu;
//
//                paintColour = generateColors((int)Math.floor(noIterationsColor), maxIterations);
//                paintColourPrevious = generateColors((int)Math.floor(noIterationsColor) + 1, maxIterations);
//                painted = interpolateColours(paintColour, paintColourPrevious, noIterations % 1);
//                I.setRGB(x, y, painted.getRGB());

//                paintColourPrevious = generateColors(noIterations - 1, maxIterations);
                paintColour = generateColors(noIterations, maxIterations);
//                GradientPaint gradient = new GradientPaint(x, y, paintColour, x, y, paintColourPrevious);
//                paintColourPrevious = generateColors((int)Math.floor(noIterationsColor), maxIterations);



//                painted = interpolateColours(paintColourPrevious, paintColour, noIterations %1  );
                I.setRGB(x, y, paintColour.getRGB());

            }
        }
        repaint();
    }

    public Color generateColors(int noIterations, double maxIterations) {

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

    protected Color interpolateColours(Color color1, Color color2, double interpolationValue) {
        float r;
        float g;
        float b;

        //r = red1 + ((red2 - red1) * stage / 256);
        //r = (int) (color1.getRed() + ((color2.getRed() - color1.getRed()) * interpolationValue / 256));
        r = (float)(color1.getRed() * (interpolationValue) + color2.getRed() * (1-interpolationValue));
        g = (float)(color1.getGreen() * (interpolationValue) + color2.getRed() * (1-interpolationValue));
        //g = (int) (color1.getGreen() + ((color2.getGreen() - color1.getGreen()) * interpolationValue / 256));
        b = (float)(color1.getBlue() * (interpolationValue) + color2.getBlue() * (1-interpolationValue));


        return new Color(r, g, b);
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    class MouseListen extends MouseAdapter {

        public void mousePressed(MouseEvent e) {
            drawRect = true;
            zoomXStart = e.getX();
            zoomYStart = e.getY();
            clickedY = maxIm - e.getY() * (maxIm - minIm) / height;
            clickedX = minReal + e.getX() * (maxReal - minReal) / width;
            System.out.println(drawX + " " + drawY);
        }

        public void mouseDragged(MouseEvent e) {

            zoomXEnd = e.getX();
            zoomYEnd = e.getY();

            drawX = Math.min(zoomXEnd, zoomXStart);
            drawY = Math.min(zoomYEnd, zoomYStart);
            repaint();

            //repaint();
            //mandelbrotPanel.calculatePoints(maxIterations, minIm, maxIm, minReal, maxReal);

        }

        public void mouseReleased(MouseEvent e) {
            drawRect = false;
            zoomXEnd = e.getX();
            zoomYEnd = e.getY();
//            drawX = Math.min(zoomXEnd, zoomXStart);
//            drawY = Math.min(zoomYEnd, zoomYStart);
        }

    }


}

