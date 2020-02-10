package com.github.davidbeesley.asciiart.util;

import java.awt.*;
import java.awt.image.BufferedImage;

public class FontWrapper {
    private Font font;

    public FontWrapper(Font font) {
        this.font = font;
    }


    public Font getFont() {
        return font;
    }

    public int getHeight(){
        Graphics2D g2d = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB).createGraphics();
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        int height = fm.getAscent()+fm.getDescent();
        g2d.dispose();
        return height;
    }

    public int getWidth(){
        Graphics2D g2d = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB).createGraphics();
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        int width = fm.stringWidth(Character.toString(' '));
        g2d.dispose();
        return width;
    }
}
