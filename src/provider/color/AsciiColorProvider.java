package provider.color;

import util.AnsiColor;
import util.ColorVectorUtil;

import java.awt.*;

public class AsciiColorProvider {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final AnsiColor BLACK = new AnsiColor(Color.BLACK, ANSI_BLACK);
    public static final AnsiColor RED = new AnsiColor(Color.RED, ANSI_RED);
    public static final AnsiColor GREEN = new AnsiColor(Color.GREEN, ANSI_GREEN);
    public static final AnsiColor YELLOW = new AnsiColor(Color.YELLOW, ANSI_YELLOW);
    public static final AnsiColor BLUE = new AnsiColor(Color.BLUE, ANSI_BLUE);
    public static final AnsiColor PURPLE = new AnsiColor(new Color(128,0,128), ANSI_PURPLE);
    public static final AnsiColor CYAN = new AnsiColor(Color.CYAN, ANSI_CYAN);
    public static final AnsiColor WHITE = new AnsiColor(Color.WHITE, ANSI_WHITE);

    public static final AnsiColor[] colors = {BLACK, RED, GREEN, YELLOW, BLUE, PURPLE, CYAN, WHITE};

    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";


    public String getColor(Color color, char c){

        double min = 10;
        AnsiColor ansi = null;
        for (int i = 0; i < colors.length; i++){
            AnsiColor possible = colors[i];
            if (possible.colorSimilarity(color) < min){
                ansi = possible;
                min = possible.colorSimilarity(color);
            }
        }
        //System.out.print(ansi.getColor().getRGB()+"");
        return ansi.color(c+"");

    }
}
