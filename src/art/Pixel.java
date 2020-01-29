package art;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Pixel {

    private int height;
    private int width;
    private BufferedImage image;
    private char c;

    public Pixel(BufferedImage image, char c){
        this.image = image;
        height = image.getHeight();
        width = image.getWidth();
        this.c = c;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Color getColor(int width, int height){

        return new Color(image.getRGB(width, height), true);
    }

    public char getChar() {return  c;}
}
