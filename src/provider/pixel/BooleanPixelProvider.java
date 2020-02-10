package provider.pixel;


import java.awt.*;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class BooleanPixelProvider {

    double minAngle = PERFECT;
    Set<Color> matchers;
    boolean invert = false;

    public static final double PERFECT = .001;
    public static final double FINEST = .4;
    public static final double FINER = .5;
    public static final double FINE = .6;
    public static final double LOW = .7;
    public static final double MEDIUM = .8;
    public static final double HIGH = .9;
    public static final double LOOSE = 1.0;

    //private static final double


    public BooleanPixelProvider(double minAngle){
        matchers = new HashSet<>();
        this.minAngle = minAngle;
    }
    public BooleanPixelProvider(double minAngle, boolean invert){
        matchers = new HashSet<>();
        this.invert = invert;
        this.minAngle = minAngle;
    }

    public BooleanPixelProvider(double minAngle, boolean invert, Set<Color> matchers){
        this.matchers = matchers;
        this.invert = invert;
        this.minAngle = minAngle;
    }

    public void addMatcher(Color c){
        matchers.add(c);
    }

    public boolean getBoolean(Color c){
        //System.out.print(c.getRed()+ " " + c.getGreen()+ " " + c.getBlue() + " " + c.getAlpha() + "\n");
        boolean matched = true;
        if (invert) matched = false;
        ////Logger.trace(getMinAngle(c) + " " + minAngle);

        //if (c.getAlpha() == 255) return !matched;
        ////Logger.trace("RGBA:" + c.getRed() + " "+ c.getGreen() + " "+ c.getBlue() + " "+ c.getAlpha() + " " + getMinAngle(c));
        //System.exit(1);
        if (getMinAngle(c) <= minAngle){

            return matched;
        } else {

            return !matched;
        }
    }

    public double getDouble(Color c){
        return getMinAngle(c);
    }

    private double getMinAngle(Color c){
        Integer[] a = getVector(c);
        double min = Double.MAX_VALUE;
        for (Color matcher : matchers) {
            Integer[] b= getVector(matcher);
            if (angle(a,b) < min) min = angle(a,b);
            ////Logger.trace(a.toString() + " " + b.toString() + " ANGLE:    b" + angle(a,b));
        }
            return min;
    }

    public static double angle(Integer[] a, Integer[] b){
        if (a.length != b.length){
            //Logger.error("logic error");
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


    public static void printRanges(){
        Set<Double> angles = new TreeSet<>();
        Color a = Color.BLACK;
        Integer[] aV = getVector(a);
        for (int red = 0; red < 255; red += 10){
            for (int blue = 0; blue < 255; blue += 10){
                for (int green = 0; green < 255; green += 10){
                    for (int alpha = 0; alpha < 255; alpha += 10){
                        Color b = new Color(red, blue, green, alpha);
                        Integer[] bV = getVector(b);
                        angles.add(angle(aV,bV));
                    }
                }
            }
        }

        int count = 0;
        int every = 5000;
        for (double d: angles){
            count++;
            if (count == every) {
                System.out.println(d);
                count = 0;
            }
        }
        System.out.println(angles.size());
    }

    public static Set<Double> getSampleAngles(){
        Set<Double> set = new TreeSet<>();
        for (double d = .0; d < 1.1; d += 1.0/16){
            set.add(d);
        }
        return set;
    }
}
