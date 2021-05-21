package Utlities;

import javafx.scene.shape.Circle;

import java.util.ArrayList;

import Jama.Matrix;

public class Function {
    public Function(double[][] cords) {
        this.cords = cords;
        this.numAnchors = cords.length;
    }

    ArrayList<Circle> anchors;
    int numAnchors;
    double[][] cords;

    public double[] getCoeffs() {
        double[][] lhsArray = new double[numAnchors][numAnchors];
        double[] rhsArray = new double[numAnchors];
        if (numAnchors < 2) return new double[0];
        for(int i = 0; i < numAnchors; i++) {
            for(int j = 0; j < numAnchors; j++) {
                lhsArray[i][j] = Math.pow(cords[i][0], j);
            }
            rhsArray[i] = cords[i][1];
        }
        //Creating Matrix Objects with arrays
        Matrix lhs = new Matrix(lhsArray);
        Matrix rhs = new Matrix(rhsArray, numAnchors);
        //Calculate Solved Matrix
        boolean solvable = true;
        for(int i = 0; i < cords.length; i++) {
            for(int j = i; j < cords.length; j++) {
                if (cords[i][0] == cords[j][0] && i != j) {
                    solvable = false;
                    break;
                }
            }
        }
        //Printing Answers
        if (solvable) {
            Matrix ans = lhs.solve(rhs);
            double[] coeffs = new double[numAnchors];
            for(int i = 0; i < numAnchors; i++) {
                coeffs[i] = ans.get(i, 0);
            }

            return coeffs;
        } else {
            return new double[0];
        }

    }

    public String getFunction() {
        double[] coeffs = getCoeffs();
        if (coeffs.length == 0) {
           return  "Not Solvable";
        }
        int numDecimals = 3;
        //String format = "%." + numDecimals + "f";
        String format;
        String str = "f(x) = ";
        for(int i = 0; i < numAnchors; i++) {
            format = setFormat(coeffs[i]);
            if (i == 0) {
                str += String.format(format, coeffs[i]);
            } else {
                if (coeffs[i] > 0) {
                    str += " + ";
                } else if (coeffs[i] < 0) {
                    str += " - ";
                }
                if (i == 1) {
                    str += String.format(format, Math.abs(coeffs[i])) + "(x)";
                } else {
                    str += String.format(format, Math.abs(coeffs[i])) + "(x^" + i + ")";
                }
            }

        }
        return str;
    }

    private String setFormat(double number) {
        if (Math.abs(number) < 0.01) return "%.2e";
        else return "%.3f";
    }
}

