package Final_Project;

public class LinearRegressionModel {

    private double[] x;
    private double[] y;
    public static int count;

    public LinearRegressionModel() {
        count = 0;
        int size = 100;
        x = new double[size];
        y = new double[size];
    }

    public LinearRegressionModel(double[] xVals, double[] yVals, int setCount) {
        x = xVals;
        y = yVals;
        count = setCount;
    }

    public double[] getX() {
        return x;
    }

    public double[] getY() {
        return y;
    }

    public void addDataPair(double xVal, double yVal) {
        if (count < x.length) {
            x[count] = xVal;
            y[count] = yVal;
            count++;
        } else {
            System.out.println("The Array is full");

        }
    }

    private double sumSingleValues(char val) {
        double sum = 0;
        if (val == 'x') {
            for (int i = 0; i < count; i++) {
                sum += x[i];
            }
        } else if (val == 'y') {
            for (int i = 0; i < count; i++) {
                sum += y[i];
            }
        } else {
            System.out.println("Please enter a valid list ('x' or 'y')");
        }
        return sum;
    }

    private double sumDualValues(String val) {
        double sum = 0;
        if (val.equals("x * x")) {
            for (int i = 0; i < count; i++) {
                sum += (x[i] * x[i]);
            }
        } else if (val.equals("x * y")) {
            for (int i = 0; i < count; i++) {
                sum += (y[i] * x[i]);
            }
        } else {
            System.out.println("Please enter a valid list ('x * x' or 'x * y')");
        }
        return sum;
    }

    private double calculateAverageValue(char val) {
        return (sumSingleValues(val) / count);
    }

    public double calculateSlope() {
        double xSum = sumSingleValues('x');
        double ySum = sumSingleValues('y');
        double xySum = sumDualValues("x * y");
        double xxSum = sumDualValues("x * x");
        return (((count * xySum) - (xSum * ySum)) / (count * xxSum - (Math.pow(xSum, 2))));
    }

    public double calculateIntercept() {
        return (calculateAverageValue('y') - (calculateAverageValue('x') * calculateSlope()));
    }

    public double findXgivenY(double y) {
        return ((y - calculateIntercept()) / calculateSlope());
    }

    public double findYgivenX(double x) {
        return (calculateSlope() * x + calculateIntercept());
    }

    public String toString() {
        return ("y = " + calculateSlope() + "x +" + calculateIntercept());
    }
}