package Utlities;

import Calculations.Bounds;
import Panes.GraphPane;

public class PointConversion {
    public static double xCordToPixel(double xCord, GraphPane graphPane) {
        return (xCord - graphPane.getBounds().leftBound) * graphPane.getWidth() / graphPane.getBounds().xRange;
    }

    public static double xPixelToCord(double xPixel, GraphPane graphPane) {
        return xPixel * graphPane.getBounds().xRange / graphPane.getWidth() + graphPane.getBounds().leftBound;
    }

    public static double yCordToPixel(double yCord, GraphPane graphPane) {
        return (graphPane.getBounds().upperBound - yCord) * graphPane.getHeight() / graphPane.getBounds().yRange;
    }

    public static double yPixelToCord(double yPixel, GraphPane graphPane) {
        return -1 * yPixel * graphPane.getBounds().yRange / graphPane.getHeight() + graphPane.getBounds().upperBound;
    }

    public static double xCordToPixel(double xCord, GraphPane graphPane, Bounds bounds) {
        return (xCord - bounds.leftBound) * graphPane.getWidth() / bounds.xRange;
    }

    public static double xPixelToCord(double xPixel, GraphPane graphPane, Bounds bounds) {
        return xPixel * bounds.xRange / graphPane.getWidth() + bounds.leftBound;
    }

    public static double yCordToPixel(double yCord, GraphPane graphPane, Bounds bounds) {
        return (bounds.upperBound - yCord) * graphPane.getHeight() / bounds.yRange;
    }

    public static double yPixelToCord(double yPixel, GraphPane graphPane, Bounds bounds) {
        return -1 * yPixel * bounds.yRange / graphPane.getHeight() +bounds.upperBound;
    }

}
