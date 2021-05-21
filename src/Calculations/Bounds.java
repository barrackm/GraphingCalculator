package Calculations;

public class Bounds {
    public double leftBound,rightBound, lowerBound, upperBound, xRange, yRange, xCentre, yCentre;

    public Bounds(double leftBound, double rightBound, double lowerBound, double upperBound) {
        this.rightBound = rightBound;
        this.leftBound = leftBound;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;

        this.xRange = rightBound - leftBound;
        this.xCentre = (rightBound + leftBound) / 2;
        this.yRange = upperBound - lowerBound;
        this.yCentre = (upperBound + lowerBound) / 2;
    }

    public void updateBounds(double leftBound, double rightBound, double lowerBound, double upperBound) {
        this.leftBound = leftBound;
        this.rightBound = rightBound;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;

        this.xRange = rightBound - leftBound;
        this.xCentre = (rightBound + leftBound) / 2;
        this.yRange = upperBound - lowerBound;
        this.yCentre = (upperBound + lowerBound) / 2;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return new Bounds(leftBound, rightBound, lowerBound, upperBound);
    }
}
