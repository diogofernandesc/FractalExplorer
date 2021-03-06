
public class Complex {

    // a is the real part of the complex number
    // b is the imaginary part of the complex number

    // Calculation done using i^2 = -1
    // newA and newB used as local variables to store temporary values

    private double a;
    private double b;

    public Complex(double a, double b) {
        this.a = a;
        this.b = b;
    }

    public double getReal() {
        return a;
    }

    public double getImaginary() {
        return b;
    }

    public Complex square() {
        double newA = (a * a) - (b * b);
        double newB = 2 * a * b;
        return new Complex(newA, newB);
    }

    public double modulusSquared() {
        return (a * a) + (b * b);
    }

    public Complex squareTricorn() {
        double newA = (a * a) - (b * b);
        double newB = -2 * a * b;
        return new Complex(newA, newB);
    }

    public Complex add(Complex d) {
        double newA = this.getReal() + d.getReal();
        double newB = this.getImaginary() + d.getImaginary();
        return new Complex(newA, newB);
    }

}
