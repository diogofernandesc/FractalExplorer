import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class Display {

    public static void main(String[] args) {
        Window window = new Window("Fractal Explorer");
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
    private BurningShip burningShipDraw;
    private MultiBrot multiBrotDraw;
    private Tricorn tricornDraw;
    private JuliaMandelbrot juliaMandelBrotSet;
    private JuliaMultibrot juliaMultiBrotSet;
    private JuliaBurningShip juliaBurningShipSet;
    private JuliaTricorn juliaTricornSet;
    private double clickedX, clickedY, releasedX, releasedY;
    public JTextField realAxisFieldMin, realAxisFieldMax, imaginaryAxisFieldMin, imaginaryAxisFieldMax, iterationField;
    private JLabel complexPoint;
    private JPanel leftPanel;
    private Double draggedY;
    private Double draggedX;
    private String currentBrot;
    private Fractal currentFractal;
    private JuliaSet currentJulia;
    private MouseListenerDisplay displayMouseListener;
    private KeyListenerDisplay displayKeyListener;
    private boxListener boxOnlyListener;

    public Window(String title) {
        super("Fractal Explorer");
    }

    /*
     * This is where the main GUI is initialized:
     * boxOnlyListener is used so that the enter button can be used to trigger a redraw of the fractal
     * instead of clicking the redraw fractal button
     *
     * This is split into two main panels left and right
     * The left panel consists of the main fractal, the right of the main controls and julia set
     * All fractals and julias use the same mount and keyboard listeners so that control can be swapped across
     */
    protected void init() {
        this.setPreferredSize(new Dimension(1200, 800));
        this.setResizable(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        Container container = this.getContentPane();
        container.setLayout(new BorderLayout());
        container.setBackground(Color.LIGHT_GRAY);

        boxOnlyListener = new boxListener();
        displayMouseListener = new MouseListenerDisplay();
        displayKeyListener = new KeyListenerDisplay();

        // Mandelbrot creation:
        mandelbrotPanel = new MandelbrotSet(maxIterations, minIm, maxIm, minReal, maxReal);
        juliaMandelBrotSet = new JuliaMandelbrot(maxIterations, minIm, maxIm, minReal, maxReal);
        mandelbrotPanel.addMouseListener(displayMouseListener);
        mandelbrotPanel.addMouseMotionListener(displayMouseListener);
        mandelbrotPanel.addKeyListener(displayKeyListener);
        mandelbrotPanel.setFocusable(true);
        currentBrot = "mandelbrot";

        //Multibrot creation:
        multiBrotDraw = new MultiBrot(maxIterations, minIm, maxIm, minReal, maxReal, 2);
        juliaMultiBrotSet = new JuliaMultibrot(maxIterations, minIm, maxIm, minReal, maxReal);
        multiBrotDraw.addMouseListener(displayMouseListener);
        multiBrotDraw.addMouseMotionListener(displayMouseListener);
        multiBrotDraw.addKeyListener(displayKeyListener);

        //Burning ship creation:
        burningShipDraw = new BurningShip(maxIterations, minIm, maxIm, minReal, maxReal);
        juliaBurningShipSet = new JuliaBurningShip(maxIterations, minIm, maxIm, minReal, maxReal);
        burningShipDraw.addMouseListener(displayMouseListener);
        burningShipDraw.addMouseMotionListener(displayMouseListener);
        burningShipDraw.addKeyListener(displayKeyListener);

        // Tricorn creation:
        tricornDraw = new Tricorn(maxIterations, minIm, maxIm, minReal, maxReal);
        juliaTricornSet = new JuliaTricorn(maxIterations, minIm, maxIm, minReal, maxReal);
        tricornDraw.addMouseListener(displayMouseListener);
        tricornDraw.addMouseMotionListener(displayMouseListener);
        tricornDraw.addKeyListener(displayKeyListener);

        RightPanel rightPanel = new RightPanel();
        rightPanel.init();

        leftPanel = new JPanel();
        leftPanel.setLayout(null);

        leftPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        leftPanel.setBorder(new TitledBorder(new EtchedBorder(), "Main Fractal"));
        leftPanel.add(mandelbrotPanel);

        // Adding to the panels/frames structure:

        container.add(leftPanel, BorderLayout.CENTER);
        container.add(rightPanel, BorderLayout.EAST);

        this.pack();
        this.setVisible(true);
        this.setResizable(false);
    }

    /* Keeps track of what the current fractal and julia are
     * This is used when user is choosing between drawing different fractals
     * So that julia can change and the mouse events can execute fine for the different fractals
     * This also allows the keyboard events to swap between fractals fine
     */
    public void getCurrentBrot() {
        if (currentBrot == "mandelbrot") {
            currentFractal = mandelbrotPanel;
            currentJulia = juliaMandelBrotSet;

        } else if (currentBrot == "multibrot") {
            currentFractal = multiBrotDraw;
            currentJulia = juliaMultiBrotSet;

        } else if (currentBrot == "burning ship") {
            currentFractal = burningShipDraw;
            currentJulia = juliaBurningShipSet;

        } else if (currentBrot == "tricorn") {
            currentFractal = tricornDraw;
            currentJulia = juliaTricornSet;
        }
    }

    // Simple method to reset the axis when a different fractal is drawn from the options
    protected void resetAxisValues() {
        minReal = -2.0;
        maxReal = 2.0;
        minIm = -1.6;
        maxIm = 1.6;
        realAxisFieldMin.setText(Double.toString(minReal));
        realAxisFieldMax.setText(Double.toString(maxReal));
        imaginaryAxisFieldMin.setText(Double.toString(minIm));
        imaginaryAxisFieldMax.setText(Double.toString(maxIm));

    }

    class MouseListenerDisplay implements MouseListener, MouseMotionListener {

        public MouseListenerDisplay() {
            getCurrentBrot();
        }

        /* Part of the implementation of part four
         * This event sets the complex point JLabel to whatever the current point is
         * setBounds here is used to adjust the size of the julia as this event causes components to shift
         * downwards slightly
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            getCurrentBrot();
            clickedY = maxIm - e.getY() * (maxIm - minIm) / currentFractal.getHeight();
            clickedX = minReal + e.getX() * (maxReal - minReal) / currentFractal.getWidth();
            if (clickedY < 0) {
                complexPoint.setText(clickedX + " " + clickedY + "i");
            } else {
                complexPoint.setText(clickedX + " + " + clickedY + "i");
            }
            currentJulia.setBounds(10, 20, 400, 325);
            currentJulia.calculatePoints(clickedX, clickedY);
        }

        // This is required as the initial x and y complex points for the zooming function
        @Override
        public void mousePressed(MouseEvent e) {
            getCurrentBrot();
            clickedY = maxIm - e.getY() * (maxIm - minIm) / currentFractal.getHeight();
            clickedX = minReal + e.getX() * (maxReal - minReal) / currentFractal.getWidth();
        }

        /*
         * releasedX and Y are the points that the zoom is performed on (end ones)
         * the if statement is to make sure that the zoom doesn't occur on a click
         * Math.min and max are used here so that you can compute the zoom on the left or right of the initial point
         * This is because there are situations where u want to zoom in all directions
         * Also updates the axis fields to show the new axis values after the zoom
         */
        @Override
        public void mouseReleased(MouseEvent e) {
            getCurrentBrot();
            releasedX = minReal + e.getX() * (maxReal - minReal) / currentFractal.getWidth();
            releasedY = maxIm - e.getY() * (maxIm - minIm) / currentFractal.getHeight();

            // Stop zoom when clicking
            if (((Math.abs(clickedX - releasedX)) > 0) || ((Math.abs(clickedY - releasedY)) > 0)) {
                minReal = Math.min(clickedX, releasedX);
                maxReal = Math.max(clickedX, releasedX);
                minIm = Math.min(clickedY, releasedY);
                maxIm = Math.max(clickedY, releasedY);
            }

            realAxisFieldMax.setText(Double.toString(maxReal));
            realAxisFieldMin.setText(Double.toString(minReal));
            imaginaryAxisFieldMax.setText(Double.toString(maxIm));
            imaginaryAxisFieldMin.setText(Double.toString(minIm));

            currentFractal.calculatePoints(maxIterations, minIm, maxIm, minReal, maxReal);
        }

        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
        public void mouseDragged(MouseEvent e) {}

        /*
         * Used to create the live julia as part of one of the extensions
         * Converting the points to complex points and drawing the julia
         */
        @Override
        public void mouseMoved(MouseEvent e) {
            getCurrentBrot();
            clickedY = maxIm - e.getY() * (maxIm - minIm) / currentFractal.getHeight();
            clickedX = minReal + e.getX() * (maxReal - minReal) / currentFractal.getWidth();
            draggedY = maxIm - e.getY() * (maxIm - minIm) / mandelbrotPanel.getHeight();
            draggedX = minReal + e.getX() * (maxReal - minReal) / mandelbrotPanel.getWidth();
            currentJulia.calculatePoints(draggedX, draggedY);
        }
    }

    class boxListener implements KeyListener {

        // Used in the axis labels and iteration box as an alternative to clicking redraw fractal
        @Override
        public void keyPressed(KeyEvent e) {
            getCurrentBrot();
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                maxIterations = Integer.parseInt(iterationField.getText());
                minIm = Double.parseDouble(imaginaryAxisFieldMin.getText());
                maxIm = Double.parseDouble(imaginaryAxisFieldMax.getText());
                minReal = Double.parseDouble(realAxisFieldMin.getText());
                maxReal = Double.parseDouble(realAxisFieldMax.getText());
                currentFractal.calculatePoints(maxIterations, minIm, maxIm, minReal, maxReal);
            }
        }

        public void keyReleased(KeyEvent e) {}
        public void keyTyped(KeyEvent e) {}
    }

    class KeyListenerDisplay implements KeyListener {

        public KeyListenerDisplay() {
            getCurrentBrot();
        }

        /*
         * This is part of one of the extensions, please read the ReadMe for a deeper explanation
         * allows the axis to be changed to zoom in and out
         * Also sets the axis labels accordingly so user can keep track
         */
        public void zoom(double amount) {
            getCurrentBrot();
            minReal = draggedX - amount;
            maxReal = draggedX + amount;
            minIm = draggedY - amount;
            maxIm = draggedY + amount;
            realAxisFieldMin.setText(Double.toString(draggedX - amount));
            realAxisFieldMax.setText(Double.toString(draggedX + amount));
            imaginaryAxisFieldMin.setText(Double.toString(draggedY - amount));
            imaginaryAxisFieldMax.setText(Double.toString(draggedY + amount));
            maxIterations = Integer.parseInt(iterationField.getText());
            currentFractal.calculatePoints(maxIterations, minIm, maxIm, minReal, maxReal);
        }

        public void keyTyped(KeyEvent e) {}

        /*
         * Enter has the same functionality, redrawing the fractal with the updated values
         * Left arrow key moves the fractal to the left by increasing the real max value by 0.5
         * Right arrow moves fractal to the right by decreasing the real minimum value by 0.5
         * Up arrow moves fractal up by decreasing the imaginary axis minimum value by 0.5
         * Down arrow moves fractal down by increasing the maximum imaginary axis by 0.5
         * Backspace resets the current fractal by resetting all the axis values to their defaults
         * The functionality of keys 1-9 are explained in the readme file: (This is one of my own extensions)
         * Essentially, they all have different levels of 'zoom depth' with 1 being the least and 9 the most
         * If you are at zoom 9 depth clicking lower numbers zooms out, if you are zoom depth 1, clicking higher numbers zooms in
         */

        @Override
        public void keyPressed(KeyEvent e) {
            getCurrentBrot();
            if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                maxIterations = Integer.parseInt(iterationField.getText());
                minIm = Double.parseDouble(imaginaryAxisFieldMin.getText());
                maxIm = Double.parseDouble(imaginaryAxisFieldMax.getText());
                minReal = Double.parseDouble(realAxisFieldMin.getText());
                maxReal = Double.parseDouble(realAxisFieldMax.getText());
                currentFractal.calculatePoints(maxIterations, minIm, maxIm, minReal, maxReal);

            } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                maxReal = Double.parseDouble(realAxisFieldMax.getText()) + 0.5;
                realAxisFieldMax.setText(Double.toString(maxReal));
                currentFractal.calculatePoints(maxIterations, minIm, maxIm, minReal, maxReal);

            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                minReal = Double.parseDouble(realAxisFieldMin.getText()) - 0.5;
                realAxisFieldMin.setText(Double.toString(minReal));
                currentFractal.calculatePoints(maxIterations, minIm, maxIm, minReal, maxReal);

            } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                minIm = Double.parseDouble(imaginaryAxisFieldMin.getText()) - 0.5;
                imaginaryAxisFieldMin.setText(Double.toString(minIm));
                currentFractal.calculatePoints(maxIterations, minIm, maxIm, minReal, maxReal);

            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                maxIm = Double.parseDouble(imaginaryAxisFieldMax.getText()) + 0.5;
                imaginaryAxisFieldMax.setText(Double.toString(maxIm));
                currentFractal.calculatePoints(maxIterations, minIm, maxIm, minReal, maxReal);

            } else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                maxReal = 2;
                minReal = -2;
                maxIm = 1.6;
                minIm = -1.6;
                realAxisFieldMin.setText(Double.toString(minReal));
                realAxisFieldMax.setText(Double.toString(maxReal));
                imaginaryAxisFieldMin.setText(Double.toString(minIm));
                imaginaryAxisFieldMax.setText(Double.toString(maxIm));
                currentFractal.calculatePoints(maxIterations, minIm, maxIm, minReal, maxReal);

            } else if (e.getKeyCode() == KeyEvent.VK_1) {
                zoom(0.05);

            } else if (e.getKeyCode() == KeyEvent.VK_2) {
                zoom(0.01);

            } else if (e.getKeyCode() == KeyEvent.VK_3) {
                zoom(0.005);

            } else if (e.getKeyCode() == KeyEvent.VK_4) {
                zoom(0.001);

            } else if (e.getKeyCode() == KeyEvent.VK_5) {
                zoom(0.0005);

            } else if (e.getKeyCode() == KeyEvent.VK_6) {
                zoom(0.0001);

            } else if (e.getKeyCode() == KeyEvent.VK_7) {
                zoom(0.00005);

            } else if (e.getKeyCode() == KeyEvent.VK_8) {
                zoom(0.00001);

            } else if (e.getKeyCode() == KeyEvent.VK_9) {
                zoom(0.000005);
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }

    class RightPanel extends JPanel {

        JPanel juliaPanel;
        File pointFile = new File("Points.txt");

        BufferedReader br;
        BufferedWriter out;

        public RightPanel() {
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            this.setPreferredSize(new Dimension(430, 750));
            this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            this.setBorder(new TitledBorder(new EtchedBorder(), "Settings"));

        }

        // Calls all methods that create the controls for the explorer
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
            createJuliaBox(juliaMandelBrotSet);

        }

        /*
         * Creates the two real axis boxes and their labels on a 2x2 grid layout
         * Also adds the boxListener key listener to each field so that the user can click enter to redraw
         */

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

            realAxisFieldMin.addKeyListener(boxOnlyListener);
            realAxisFieldMax.addKeyListener(boxOnlyListener);
            realAxisFieldMin.requestFocus();
            realAxisFieldMax.requestFocus();
            this.add(realAxisPanel);
        }

        /*
         * Creates the two imaginary axis boxes and their labels on a 2x2 grid layout
         * Also adds the boxListener key listener to each field so that the user can click enter to redraw
         */

        public void createImaginaryAxisBox() {
            JPanel imagAxisPanel = new JPanel();
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

            imaginaryAxisFieldMin.addKeyListener(boxOnlyListener);
            imaginaryAxisFieldMax.addKeyListener(boxOnlyListener);
            imaginaryAxisFieldMin.requestFocus();
            imaginaryAxisFieldMax.requestFocus();

            this.add(imagAxisPanel);
        }

        /*
         * Creates the iteration field and the redraw button
         * The redraw button has an actionListener that redraws the fractal by taking the current axis field values
         * The iteration field also has the same keylistener as the axis fields
         */
        public void createIterationsBox() {
            JPanel iterationPanel = new JPanel();
            iterationPanel.setLayout(new FlowLayout());
            iterationPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            iterationPanel.setBorder(new TitledBorder(new EtchedBorder(), "Iteration Controller"));

            JLabel iterationNumber = new JLabel("Number:", SwingConstants.RIGHT);
            iterationField = new JTextField(3);
            iterationField.addKeyListener(boxOnlyListener);
            iterationField.requestFocus();
            iterationField.setText(Integer.toString(maxIterations)); // Set the value of the variable to be field content
            JButton redrawButton = new JButton("Redraw fractal");
            redrawButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    getCurrentBrot();
                    maxIterations = Integer.parseInt(iterationField.getText());
                    minIm = Double.parseDouble(imaginaryAxisFieldMin.getText());
                    maxIm = Double.parseDouble(imaginaryAxisFieldMax.getText());
                    minReal = Double.parseDouble(realAxisFieldMin.getText());
                    maxReal = Double.parseDouble(realAxisFieldMax.getText());
                    currentFractal.calculatePoints(maxIterations, minIm, maxIm, minReal, maxReal);
                }
            });

            iterationPanel.add(iterationNumber);
            iterationPanel.add(iterationField);
            iterationPanel.add(redrawButton);

            this.add(iterationPanel);
        }

        /*
         * Simply adds the JLabel current point to the screen when the user clicks
         */

        public void createCurrentPointBox() {
            JPanel currentPointPanel = new JPanel();
            currentPointPanel.setLayout(new FlowLayout());
            currentPointPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            currentPointPanel.setBorder(new TitledBorder(new EtchedBorder(), "User Selected Point"));
            complexPoint = new JLabel();
            currentPointPanel.add(complexPoint);

            this.add(currentPointPanel);
        }

        /*
         * Here there are 3 tabbed panes; one for favourites, one for saving the favourites as images (extension)
         * and one to choose which fractal to draw
         * The choice of fractals to draw are on radio buttons each with an action listener with almost exactly the
         * same functionality:
         * 1. Call currentBrot to set the currentFractal to the fractal clicked
         * 2. Remove any other fractals from the left panel and add the clicked one
         * 3. Reset the axis for a clear image
         * 4. Draw the new fractal and the new julia in their respective positions
         * 5. Allow the user to perform the functionality of the key and mouse listeners implemented
         *
         * The multibrot has a slider that is explained in the readme, basically it sets the amount of times squared
         * Which has several options to choose from that redraw at every change, this slider is only functional if
         * the radio button for the multibrot is selected [Extension]
         *
         * The favourites works with a combo box by adding the selected point to a text file and then reading that file
         * and updating the combo box, if one of the points is selected on the combo box that is drawn to the julia area
         *
         * The image displayed in the julia can also be saved to a file in the 2nd tab by giving a name
         * this is saved in the same directory as the .java files [extension]
         *
         */
        public void createTabbedPaneBox() {

            JPanel chooseFractalPanel = new JPanel();
            chooseFractalPanel.setLayout(new BorderLayout());
            JPanel fractalTop = new JPanel();
            fractalTop.setLayout(new FlowLayout());
            JPanel fractalBottom = new JPanel();
            fractalBottom.setLayout(new FlowLayout());
            chooseFractalPanel.add(fractalTop, BorderLayout.NORTH);
            chooseFractalPanel.add(fractalBottom, BorderLayout.SOUTH);

            ButtonGroup fractalGroup = new ButtonGroup();
            JRadioButton mandelbrotRadio = new JRadioButton("Mandelbrot Set");
            fractalGroup.add(mandelbrotRadio);
            mandelbrotRadio.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    currentBrot = "mandelbrot";
                    leftPanel.remove(multiBrotDraw);
                    leftPanel.remove(burningShipDraw);
                    leftPanel.remove(tricornDraw);
                    leftPanel.add(mandelbrotPanel);
                    resetAxisValues();
                    mandelbrotPanel.calculatePoints(maxIterations,minIm,maxIm,minReal,maxReal);
                    mandelbrotPanel.setFocusable(true);
                    juliaPanel.add(juliaMandelBrotSet);
                    mandelbrotPanel.requestFocus();
                }
            });

            JRadioButton burningShipRadio = new JRadioButton("Burning Ship");
            fractalGroup.add(burningShipRadio);
            burningShipRadio.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    currentBrot = "burning ship";
                    leftPanel.remove(mandelbrotPanel);
                    leftPanel.remove(multiBrotDraw);
                    leftPanel.remove(tricornDraw);
                    leftPanel.add(burningShipDraw);
                    resetAxisValues();
                    burningShipDraw.calculatePoints(maxIterations,minIm,maxIm,minReal,maxReal);
                    burningShipDraw.setFocusable(true);
                    juliaPanel.add(juliaBurningShipSet);
                    burningShipDraw.requestFocus();
                }
            });

            JRadioButton multiBrotRadio = new JRadioButton("Multibrot");
            fractalGroup.add(multiBrotRadio);
            JSlider multiBrotSlider = new JSlider(2,5,2);

            multiBrotSlider.setMajorTickSpacing(1);
            multiBrotSlider.setPaintTicks(true);
            multiBrotSlider.setSnapToTicks(true);
            multiBrotSlider.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    if (multiBrotRadio.isSelected()) {
                        int sliderValue = multiBrotSlider.getValue();
                        currentBrot = "multibrot";
                        juliaMultiBrotSet.setValue(sliderValue);
                        leftPanel.remove(mandelbrotPanel);
                        leftPanel.remove(burningShipDraw);
                        leftPanel.remove(tricornDraw);
                        leftPanel.add(multiBrotDraw);
                        juliaPanel.add(juliaMultiBrotSet);
                        multiBrotDraw.setValue(sliderValue);
                        resetAxisValues();
                        multiBrotDraw.calculatePoints(maxIterations, minIm, maxIm, minReal, maxReal);
                        multiBrotDraw.setFocusable(true);
                        multiBrotDraw.requestFocus();
                    }
                }
            });

            multiBrotRadio.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    currentBrot = "multibrot";
                    juliaMultiBrotSet.setValue(multiBrotSlider.getValue());
                    leftPanel.remove(mandelbrotPanel);
                    leftPanel.remove(burningShipDraw);
                    leftPanel.remove(tricornDraw);
                    leftPanel.add(multiBrotDraw);
                    multiBrotDraw.setFocusable(true);
                    resetAxisValues();
                    multiBrotDraw.calculatePoints(maxIterations,minIm,maxIm,minReal,maxReal);
                    juliaPanel.add(juliaMultiBrotSet);
                    multiBrotDraw.requestFocus();
                }
            });

            JRadioButton tricornRadio = new JRadioButton("Tricorn");
            fractalGroup.add(tricornRadio);
            tricornRadio.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    currentBrot = "tricorn";
                    leftPanel.remove(mandelbrotPanel);
                    leftPanel.remove(burningShipDraw);
                    leftPanel.remove(multiBrotDraw);
                    leftPanel.add(tricornDraw);
                    tricornDraw.setFocusable(true);
                    resetAxisValues();
                    tricornDraw.calculatePoints(maxIterations,minIm,maxIm,minReal,maxReal);
                    juliaPanel.add(juliaTricornSet);
                    tricornDraw.requestFocus();
                }
            });

            fractalTop.add(mandelbrotRadio);
            fractalTop.add(burningShipRadio);
            fractalTop.add(tricornRadio);
            fractalBottom.add(multiBrotSlider);
            fractalBottom.add(multiBrotRadio);

            JPanel favouritesFractal = new JPanel();
            JTabbedPane tabbedPanel = new JTabbedPane();
            JPanel favouritesPanel = new JPanel();
            JPanel saveFavourite = new JPanel();

            tabbedPanel.addTab("Favourites", favouritesPanel);
            tabbedPanel.addTab("Save as Image", saveFavourite);
            tabbedPanel.addTab("Choose Fractal", chooseFractalPanel);

            favouritesFractal.add(tabbedPanel);

            JPanel top = new JPanel();
            top.setLayout(new FlowLayout());
            favouritesPanel.setLayout(new BorderLayout());
            saveFavourite.setLayout(new BorderLayout());

            JButton addFavourites = new JButton("Add to favourites");
            JButton saveImage = new JButton("Save current julia");

            JPanel bottomSaveTab = new JPanel();
            bottomSaveTab.setLayout(new FlowLayout());
            JTextField imageNameField = new JTextField(20);
            JLabel imageName = new JLabel("Image name:");

            saveImage.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    BufferedImage saveImage = new BufferedImage(currentJulia.getWidth(), currentJulia.getHeight(), BufferedImage.TYPE_INT_ARGB);
                    Graphics g = saveImage.getGraphics();
                    currentJulia.paint(g);
                    try {
                        ImageIO.write(saveImage, "png", new File(imageNameField.getText()+".png"));
                    } catch (IOException ex) {}
                }
            });

            JComboBox pointCombo = new JComboBox();

            try {
                br = new BufferedReader(new FileReader("Points.txt"));
                String line;

                while ((line = br.readLine()) != null) {
                    pointCombo.addItem(line.toString());
                }

            } catch (IOException e1) {}


            addFavourites.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        if (!pointFile.exists()) {
                            pointFile.createNewFile(); // Makes sure a points.txt file always exists that can be written to
                        }

                        out = new BufferedWriter(new FileWriter("Points.txt", true));
                        out.write(Double.toString(clickedX) + "," + Double.toString(clickedY) + "\n");
                        out.close();

                        br = new BufferedReader(new FileReader("Points.txt"));
                        String line = null;

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
                        getCurrentBrot();
                        br = new BufferedReader(new FileReader("Points.txt"));
                        String line;
                        String[] points;
                        Object selectedItem = pointCombo.getSelectedItem();
                        points = pointCombo.getSelectedItem().toString().split(",");
                        currentJulia.calculatePoints(Double.parseDouble(points[0]), Double.parseDouble(points[1]));

                        pointCombo.removeAllItems();
                        while ((line = br.readLine()) != null) {
                            pointCombo.addItem(line.toString());
                        }

                        pointCombo.setSelectedItem(selectedItem);

                    }catch(Exception e1) {}

                }
            });

            top.add(addFavourites);
            bottomSaveTab.add(imageName);
            bottomSaveTab.add(imageNameField);
            saveFavourite.add(saveImage,BorderLayout.NORTH);
            saveFavourite.add(bottomSaveTab,BorderLayout.SOUTH);
            favouritesPanel.add(top, BorderLayout.NORTH);
            favouritesPanel.add(pointCombo, BorderLayout.SOUTH);

            this.add(favouritesFractal);
        }

        // Creates the box in the right panel for the julia
        public void createJuliaBox(JuliaSet julia) {
            juliaPanel = new JPanel();
            juliaPanel.setPreferredSize(new Dimension(600, 540));
            juliaPanel.setLayout(null);
            juliaPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            juliaPanel.setBorder(new TitledBorder(new EtchedBorder(), "Julia Set"));
            juliaPanel.add(julia);

            this.add(juliaPanel);
        }

    }
}

