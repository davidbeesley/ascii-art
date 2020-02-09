package util;

import loggerOLD.Logger;

import java.awt.*;

public class ColorVectorUtil {

    public static double getColorSimilarity(Color a, Color b){
        Integer[] al = getVector(a);
        Integer[] bl= getVector(b);
        return angle(al,bl);
    }


    private static double angle(Integer[] a, Integer[] b){
        if (a.length != b.length){
            Logger.error("logic error");
            System.exit(1);
        }

        double dotProduct = 0;
        for (int i = 0; i < a.length; i++){
            dotProduct += a[i]*b[i];
        }
        double denominator = vectorLength(a)*vectorLength(b);
        return Math.acos(dotProduct/denominator);

    }

    private static double vectorLength(Integer[] a){
        double squaredSum = 0;
        for (int i = 0; i < a.length; i++){
            squaredSum += a[i] * a[i];

        }

        return Math.sqrt(squaredSum);
    }

    private static Integer[] getVector(Color c){
        Integer[] a = new Integer[5];


        a[0] = c.getAlpha();
        a[1] = c.getRed();

        a[2] = c.getBlue();
        a[3] = c.getGreen();
        a[4] = 1; /// TO PREVENT 0,0,0,0;
        return a;

    }
}
