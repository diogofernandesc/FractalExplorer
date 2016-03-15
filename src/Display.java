import java.awt.*;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.event.*;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;
import java.io.*;


public class Display {

    public static void main(String[] args) {
        Window window = new Window("Mandelbrot Set");
        window.init();
    }
}

class Window extends JFrame {

    private int maxIterations = 100;
    private double minReal = -2;
    private double maxReal = 2;
    private double minIm = -1.6;
    private double maxIm = 1.6;
    private Mandelbrot mandelbrotPanel;
    private Julia julia;
    private double clickedX;
    private double clickedY;
    private double releasedX;
    private double releasedY;
    private int zoomXStart;
    private int zoomYStart;
    private int zoomXEnd;
    private int zoomYEnd;
    private int drawX;
    private int drawY;
    public JTextField realAxisFieldMin, realAxisFieldMax, imaginaryAxisFieldMin, imaginaryAxisFieldMax;
    JLabel complexPoint;

    private BufferedImage image;
    private Rectangle zoomRectangle;


    public Window(String title) {
        super("Fractal Explorer");
    }

    protected void init() {
        this.setPreferredSize(new Dimension(1200, 800));
        this.setResizable(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        Container container = this.getContentPane();
        container.setLayout(new BorderLayout());
        container.setBackground(Color.LIGHT_GRAY);

        // New Mandelbrot JPanel is the left panel
        mandelbrotPanel = new Mandelbrot(maxIterations, minIm, maxIm, minReal, maxReal);

        RightPanel rightPanel = new RightPanel();
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(null);

        leftPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        leftPanel.setBorder(new TitledBorder(new EtchedBorder(), "Mandelbrot Set"));
        leftPanel.add(mandelbrotPanel);

        mandelbrotPanel.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {

                zoomXEnd = e.getX();
                zoomYEnd = e.getY();

                drawX = Math.min(zoomXEnd, zoomXStart);
                drawY = Math.min(zoomYEnd, zoomYStart);

                //mandelbrotPanel.repaint();
                //mandelbrotPanel.calculatePoints(maxIterations, minIm, maxIm, minReal, maxReal);
                //repaint();

            }

            @Override
            public void mouseMoved(MouseEvent e) {}
        });

        mandelbrotPanel.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                clickedY = maxIm - e.getY() * (maxIm - minIm) / mandelbrotPanel.getHeight();
                clickedX = minReal + e.getX() * (maxReal - minReal) / mandelbrotPanel.getWidth();
                if (clickedY < 0) {
                    complexPoint.setText(clickedX + " " + clickedY + "i");
                } else {
                    complexPoint.setText(clickedX + " + " + clickedY + "i");
                }
                julia.setBounds(10, 20, 400, 315);
                julia.calculatePoints(clickedX, clickedY);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                zoomXStart = e.getX();
                zoomYStart = e.getY();
                clickedY = maxIm - e.getY() * (maxIm - minIm) / mandelbrotPanel.getHeight();
                clickedX = minReal + e.getX() * (maxReal - minReal) / mandelbrotPanel.getWidth();

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                zoomXEnd = e.getX();
                zoomYEnd = e.getY();
                releasedX = minReal + e.getX() * (maxReal - minReal) / mandelbrotPanel.getWidth();
                releasedY = maxIm - e.getY() * (maxIm - minIm) / mandelbrotPanel.getHeight();

                Double yMax = Math.max(clickedY, releasedY);
                Double yMin = Math.min(clickedY, releasedY);
                Double xMax = Math.max(clickedX, releasedX);
                Double xMin = Math.min(clickedX, releasedX);

                minReal = Math.min(clickedX, releasedX);
                maxReal = Math.max(clickedX, releasedX);
                minIm = Math.min(clickedY, releasedY);
                maxIm = Math.max(clickedY, releasedY);

                realAxisFieldMax.setText(Double.toString(maxReal));
                realAxisFieldMin.setText(Double.toString(minReal));
                imaginaryAxisFieldMax.setText(Double.toString(maxIm));
                imaginaryAxisFieldMin.setText(Double.toString(minIm));

                mandelbrotPanel.calculatePoints(maxIterations,minIm,maxIm,minReal, maxReal);

//                mandelbrotPanel.calculatePoints(maxIterations, yMin, yMax, xMin, xMax);
//                if ((clickedX > releasedX) && (clickedY > releasedY)) {
//                    realAxisFieldMin.setText(Double.toString(releasedX));
//                    minReal = releasedX;
//                    realAxisFieldMax.setText(Double.toString(clickedX));
//                    maxReal = clickedX;
//                    imaginaryAxisFieldMin.setText(Double.toString(releasedY));
//                    minIm = releasedY;
//                    imaginaryAxisFieldMax.setText(Double.toString(clickedY));
//                    maxIm = clickedY;
//                    mandelbrotPanel.calculatePoints(maxIterations, releasedY, clickedY, releasedX, clickedX);
//
//                } else if ((releasedX > clickedX) && (releasedY > clickedY)) {
//                    realAxisFieldMin.setText(Double.toString(clickedX));
//                    minReal = clickedX;
//                    realAxisFieldMax.setText(Double.toString(releasedX));
//                    maxReal = releasedX;
//                    imaginaryAxisFieldMin.setText(Double.toString(clickedY));
//                    minIm = clickedY;
//                    imaginaryAxisFieldMax.setText(Double.toString(releasedY));
//                    maxIm = releasedY;
//                    mandelbrotPanel.calculatePoints(maxIterations, clickedY, releasedY, clickedX, releasedX);
//
//                } else if ((clickedX > releasedX) && (releasedY > clickedY)) {
//                    realAxisFieldMin.setText(Double.toString(releasedX));
//                    minReal = releasedX;
//                    realAxisFieldMax.setText(Double.toString(clickedX));
//                    maxReal = clickedX;
//                    imaginaryAxisFieldMin.setText(Double.toString(clickedY));
//                    minIm = clickedY;
//                    imaginaryAxisFieldMax.setText(Double.toString(releasedY));
//                    maxIm = releasedY;
//                    mandelbrotPanel.calculatePoints(maxIterations, clickedY, releasedY, releasedX, clickedX);
//
//                } else if ((releasedX > clickedX) && (clickedY > releasedY)) {
//                    realAxisFieldMin.setText(Double.toString(clickedX));
//                    minReal = clickedX;
//                    realAxisFieldMax.setText(Double.toString(releasedX));
//                    maxReal = releasedX;
//                    imaginaryAxisFieldMin.setText(Double.toString(releasedY));
//                    minIm = releasedY;
//                    imaginaryAxisFieldMax.setText(Double.toString(clickedY));
//                    maxIm = clickedY;
//                    mandelbrotPanel.calculatePoints(maxIterations, releasedY, clickedY, clickedX, releasedX);
//
//                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        });

        // Adding to the panels/frames structure:

        container.add(leftPanel, BorderLayout.CENTER);
        container.add(rightPanel, BorderLayout.EAST);

        this.pack();
        this.setVisible(true);
        this.setResizable(false);

    }

        //x,y,w,h



    class RightPanel extends JPanel {

        File pointFile = new File("Points.txt");

        FileInputStream fileStream;
        BufferedReader br;
        BufferedWriter out;

        public RightPanel() {
            //layout = new GridBagLayout();
            //JPanel rightPanel = new JPanel();
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            this.setPreferredSize(new Dimension(430, 750));
            //this.setSize(600, 1080);
            this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            this.setBorder(new TitledBorder(new EtchedBorder(), "Settings"));

            JPanel realAxisPanel = new JPanel();
            //realAxisPanel.setSize(600,180);
            realAxisPanel.setLayout(new GridLayout(2, 2));
            realAxisPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            realAxisPanel.setBorder(new TitledBorder(new EtchedBorder(), "Real Axis"));

            JLabel minRealLabel = new JLabel("Minimum Value:", JLabel.CENTER);
            JLabel maxRealLabel = new JLabel("Maximum Value:", JLabel.CENTER);
            realAxisFieldMin = new JTextField();
            realAxisFieldMax = new JTextField();
            realAxisFieldMin.setText(Double.toString(minReal));
            realAxisFieldMax.setText(Double.toString(maxReal));

            realAxisPanel.add(minRealLabel);
            realAxisPanel.add(realAxisFieldMin);
            realAxisPanel.add(maxRealLabel);
            realAxisPanel.add(realAxisFieldMax);

            JPanel imagAxisPanel = new JPanel();
            imagAxisPanel.setSize(600, 180);
            imagAxisPanel.setLayout(new GridLayout(2, 2));
            imagAxisPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            imagAxisPanel.setBorder(new TitledBorder(new EtchedBorder(), "Imaginary Axis"));

            JLabel minImLabel = new JLabel("Minimum Value:", JLabel.CENTER);
            JLabel maxImLabel = new JLabel("Maximum Value:", JLabel.CENTER);
            imaginaryAxisFieldMin = new JTextField(3);
            imaginaryAxisFieldMin.setAlignmentX(Component.CENTER_ALIGNMENT);
            imaginaryAxisFieldMax = new JTextField(3);
            imaginaryAxisFieldMin.setText(Double.toString(minIm));
            imaginaryAxisFieldMax.setText(Double.toString(maxIm));

            imagAxisPanel.add(minImLabel);
            imagAxisPanel.add(imaginaryAxisFieldMin);
            imagAxisPanel.add(maxImLabel);
            imagAxisPanel.add(imaginaryAxisFieldMax);

            // Iteration content:

            JPanel iterationPanel = new JPanel();
            iterationPanel.setLayout(new FlowLayout());
            iterationPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            iterationPanel.setBorder(new TitledBorder(new EtchedBorder(), "Iteration Controller"));

            JLabel iterationNumber = new JLabel("Number:", SwingConstants.RIGHT);
            JTextField iterationField = new JTextField(3);
            iterationField.setText(Integer.toString(maxIterations)); // Set the value of the variable to be field content
            JButton redrawButton = new JButton("Redraw fractal");
            redrawButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    maxIterations = Integer.parseInt(iterationField.getText());
                    minIm = Double.parseDouble(imaginaryAxisFieldMin.getText());
                    maxIm = Double.parseDouble(imaginaryAxisFieldMax.getText());
                    minReal = Double.parseDouble(realAxisFieldMin.getText());
                    maxReal = Double.parseDouble(realAxisFieldMax.getText());
                    mandelbrotPanel.calculatePoints(maxIterations, minIm, maxIm, minReal, maxReal);
                }
            });

            iterationPanel.add(iterationNumber);
            iterationPanel.add(iterationField);
            iterationPanel.add(redrawButton);

            // Choose fractal:

            JPanel chooseFractalPanel = new JPanel();
            chooseFractalPanel.setLayout(new FlowLayout());
            chooseFractalPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            chooseFractalPanel.setBorder(new TitledBorder(new EtchedBorder(), "Choose Fractal"));
            JRadioButton mandelbrotRadio = new JRadioButton("Mandelbrot");
            chooseFractalPanel.add(mandelbrotRadio);

            // Current point:

            JPanel currentPointPanel = new JPanel();
            currentPointPanel.setLayout(new FlowLayout());
            currentPointPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            currentPointPanel.setBorder(new TitledBorder(new EtchedBorder(), "User Selected Point"));
            complexPoint = new JLabel();
            currentPointPanel.add(complexPoint);


            // Favourites content:
            JPanel favouritesFractal = new JPanel();
            JTabbedPane tabbedPanel = new JTabbedPane();
            JPanel favouritesPanel = new JPanel();
            tabbedPanel.addTab("Favourites", favouritesPanel);
            tabbedPanel.addTab("Choose Fractal", chooseFractalPanel);
            favouritesFractal.add(tabbedPanel);

            JPanel top = new JPanel();
            top.setLayout(new FlowLayout());
            favouritesPanel.setLayout(new BorderLayout());
            favouritesPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            favouritesPanel.setBorder(new TitledBorder(new EtchedBorder(), "Favourites"));

            JButton addFavourites = new JButton("Add to favourites");
            JButton saveImage = new JButton("Save current julia");
            //DefaultComboBoxModel pointList = new DefaultComboBoxModel();
            //ArrayList<String> pointList = new ArrayList<String>();
            JComboBox pointCombo = new JComboBox();

//            JScrollPane listSP = new JScrollPane(pointCombo);

            try {
                br = new BufferedReader(new FileReader("Points.txt"));
                String line = null;

                while ((line = br.readLine()) != null) {
                    pointCombo.addItem(line.toString());
                }


                //pointCombo.
                //pointCombo.removeAllItems();

            } catch (IOException e1) {}


            addFavourites.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        if (!pointFile.exists()) {
                            pointFile.createNewFile();
                        }


                        out = new BufferedWriter(new FileWriter("Points.txt", true));
                        out.write(Double.toString(clickedX) + "," + Double.toString(clickedY) + "\n");
                        out.close();

                        br = new BufferedReader(new FileReader("Points.txt"));
                        String line = null;


                        //pointCombo.removeAllItems();
                        while ((line = br.readLine()) != null) {
                            pointCombo.addItem(line.toString());
                            pointCombo.removeItem(line.toString());
                            pointCombo.addItem(line.toString());

                        }


                    } catch (IOException e1) {}
                }
            });
            //


            pointCombo.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {

                        br = new BufferedReader(new FileReader("Points.txt"));
                        String line = null;
                        String[] points;
                        Object selectedItem = pointCombo.getSelectedItem();
                        points = pointCombo.getSelectedItem().toString().split(",");
                        julia.calculatePoints(Double.parseDouble(points[0]), Double.parseDouble(points[1]));

                        pointCombo.removeAllItems();
                        while ((line = br.readLine()) != null) {
                            pointCombo.addItem(line.toString());
                        }

                        pointCombo.setSelectedItem(selectedItem);

                    }catch(IOException e1) {}

                }
            });

            top.add(addFavourites);
            top.add(saveImage);
            favouritesPanel.add(top, BorderLayout.NORTH);
            favouritesPanel.add(pointCombo, BorderLayout.SOUTH);

            // Julia
            julia = new Julia(maxIterations, minIm, maxIm, minReal, maxReal);
            JPanel juliaPanel = new JPanel();
            juliaPanel.setPreferredSize(new Dimension(600, 540));
            juliaPanel.setLayout(null);
            juliaPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            juliaPanel.setBorder(new TitledBorder(new EtchedBorder(), "Julia Set"));
            juliaPanel.add(julia);

            this.add(realAxisPanel);
            this.add(imagAxisPanel);
            this.add(iterationPanel);
            //this.add(chooseFractalPanel);
            this.add(currentPointPanel);
            this.add(favouritesFractal);
            this.add(juliaPanel);
        }

    }
}

