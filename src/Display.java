import java.awt.*;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.event.*;
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
    private MandelbrotSet mandelbrotPanel;
    private Tunnel tunnelDraw;
    private Julia julia;
    private double clickedX, clickedY, releasedX, releasedY;
    public JTextField realAxisFieldMin, realAxisFieldMax, imaginaryAxisFieldMin, imaginaryAxisFieldMax;
    JLabel complexPoint;
    JPanel leftPanel;
    MouseListenerDisplay mouseListenerMandelbrot;

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
        mandelbrotPanel = new MandelbrotSet(maxIterations, minIm, maxIm, minReal, maxReal);

        RightPanel rightPanel = new RightPanel();
        rightPanel.init();
        leftPanel = new JPanel();
        leftPanel.setLayout(null);

        leftPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        leftPanel.setBorder(new TitledBorder(new EtchedBorder(), "Mandelbrot Set"));
        leftPanel.add(mandelbrotPanel);

        mouseListenerMandelbrot = new MouseListenerDisplay(mandelbrotPanel);
        mandelbrotPanel.addMouseListener(mouseListenerMandelbrot);
        mandelbrotPanel.addMouseMotionListener(mouseListenerMandelbrot);

        // Adding to the panels/frames structure:

        container.add(leftPanel, BorderLayout.CENTER);
        container.add(rightPanel, BorderLayout.EAST);

        this.pack();
        this.setVisible(true);
        this.setResizable(false);

    }

    class MouseListenerDisplay implements MouseListener, MouseMotionListener {

        Fractal fractal;

        public MouseListenerDisplay(Fractal fractal) {
            this.fractal = fractal;
        }
        @Override
        public void mouseClicked(MouseEvent e) {
            clickedY = maxIm - e.getY() * (maxIm - minIm) / fractal.getHeight();
            clickedX = minReal + e.getX() * (maxReal - minReal) / fractal.getWidth();
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
            clickedY = maxIm - e.getY() * (maxIm - minIm) / fractal.getHeight();
            clickedX = minReal + e.getX() * (maxReal - minReal) / fractal.getWidth();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            releasedX = minReal + e.getX() * (maxReal - minReal) / fractal.getWidth();
            releasedY = maxIm - e.getY() * (maxIm - minIm) / fractal.getHeight();

            // Stop zoom when clicking
            if (((Math.abs(clickedX - releasedX)) > 0) || ((Math.abs(clickedY - releasedY)) > 0 )) {
                minReal = Math.min(clickedX, releasedX);
                maxReal = Math.max(clickedX, releasedX);
                minIm = Math.min(clickedY, releasedY);
                maxIm = Math.max(clickedY, releasedY);
            }

            realAxisFieldMax.setText(Double.toString(maxReal));
            realAxisFieldMin.setText(Double.toString(minReal));
            imaginaryAxisFieldMax.setText(Double.toString(maxIm));
            imaginaryAxisFieldMin.setText(Double.toString(minIm));

            fractal.calculatePoints(maxIterations,minIm,maxIm,minReal,maxReal);
        }

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}

        @Override
        public void mouseDragged(MouseEvent e) {}

        @Override
        public void mouseMoved(MouseEvent e) {
            Double draggedY = maxIm - e.getY() * (maxIm - minIm) / mandelbrotPanel.getHeight();
            Double draggedX = minReal + e.getX() * (maxReal - minReal) / mandelbrotPanel.getWidth();
            julia.calculatePoints(draggedX, draggedY);
        }
    }

    class RightPanel extends JPanel {

        File pointFile = new File("Points.txt");

        FileInputStream fileStream;
        BufferedReader br;
        BufferedWriter out;

        public RightPanel() {
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            this.setPreferredSize(new Dimension(430, 750));
            //this.setSize(600, 1080);
            this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            this.setBorder(new TitledBorder(new EtchedBorder(), "Settings"));

        }

        public void init() {

            // Create real axis box:
            createRealAxisBox();

            // Create imaginary axis box:
            createImaginaryAxisBox();

            // Create iteration content box:
            createIterationsBox();

            // Create current point box:
            createCurrentPointBox();

            // Create tabbed paned with favourites and choose fractal:
            createTabbedPaneBox();

            // Create julia set box:
            createJuliaBox();

        }

        public void createRealAxisBox() {
            JPanel realAxisPanel = new JPanel();
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

            this.add(realAxisPanel);
        }

        public void createImaginaryAxisBox() {
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

            this.add(imagAxisPanel);
        }

        public void createIterationsBox() {
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

            this.add(iterationPanel);
        }

        public void createCurrentPointBox() {
            JPanel currentPointPanel = new JPanel();
            currentPointPanel.setLayout(new FlowLayout());
            currentPointPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            currentPointPanel.setBorder(new TitledBorder(new EtchedBorder(), "User Selected Point"));
            complexPoint = new JLabel();
            currentPointPanel.add(complexPoint);

            this.add(currentPointPanel);
        }

        public void createTabbedPaneBox() {
            JPanel chooseFractalPanel = new JPanel();
            chooseFractalPanel.setLayout(new FlowLayout());
            chooseFractalPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            chooseFractalPanel.setBorder(new TitledBorder(new EtchedBorder(), "Choose Fractal"));
            JRadioButton mandelbrotRadio = new JRadioButton("Mandelbrot Set");
            mandelbrotRadio.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mandelbrotPanel.calculatePoints(maxIterations,minIm,maxIm,minReal,maxReal);
                }
            });

            JRadioButton tunnelRadio = new JRadioButton("Tunnel");
            tunnelRadio.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    tunnelDraw = new Tunnel(maxIterations,minIm,maxIm,minReal,maxReal);
                    leftPanel.remove(mandelbrotPanel);
                    leftPanel.add(tunnelDraw);
                    MouseListenerDisplay mouseListenerTunnel = new MouseListenerDisplay(tunnelDraw);
                    tunnelDraw.addMouseListener(mouseListenerTunnel);
                    tunnelDraw.addMouseMotionListener(mouseListenerTunnel);
                    tunnelDraw.calculatePoints(maxIterations,minIm,maxIm,minReal,maxReal);
                }
            });
            chooseFractalPanel.add(mandelbrotRadio);
            chooseFractalPanel.add(tunnelRadio);

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
            JComboBox pointCombo = new JComboBox();

            try {
                br = new BufferedReader(new FileReader("Points.txt"));
                String line = null;

                while ((line = br.readLine()) != null) {
                    pointCombo.addItem(line.toString());
                }

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

            this.add(favouritesFractal);
        }

        public void createJuliaBox() {
            julia = new Julia(maxIterations, minIm, maxIm, minReal, maxReal);
            JPanel juliaPanel = new JPanel();
            juliaPanel.setPreferredSize(new Dimension(600, 540));
            juliaPanel.setLayout(null);
            juliaPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            juliaPanel.setBorder(new TitledBorder(new EtchedBorder(), "Julia Set"));
            juliaPanel.add(julia);

            this.add(juliaPanel);
        }

    }
}

