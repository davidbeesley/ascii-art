package util;

import com.github.davidbeesley.asciiart.util.ColorVectorUtil;

import java.awt.*;

public class AnsiColor {
    private String code;
    private Color color;
    private static final String ANSI_RESET = "\u001B[0m";


    public AnsiColor(Color c, String s){
        code = s;
        color = c;
    }
    public Color getColor(){
        return color;
    }
    public String getCode(){
        return code;
    }
    public String color(String input){
        return code + input + ANSI_RESET;
    }
    public double colorSimilarity(Color other){
        //System.out.println(ColorVectorUtil.getColorSimilarity(color, other) + "");
        return ColorVectorUtil.getColorSimilarity(color, other);
    }
}
