package com.github.davidbeesley.asciiart.domain;

import com.github.davidbeesley.asciiart.util.FontWrapper;

import java.awt.*;

public class StyleSettings {
    private double density = 0;
    private FontWrapper font;
    private Color foregroundColor, backgroundColor;
    private int padding;

    public StyleSettings(FontWrapper font, Color foregroundColor, Color backgroundColor, int padding) {
        this.font = font;
        this.foregroundColor = foregroundColor;
        this.backgroundColor = backgroundColor;
        this.padding = padding;

    }

    public double getDensity() {
        return density;
    }

    public void setDensity(double density) {
        this.density = density;
    }

    public FontWrapper getFont() {
        return font;
    }

    public Color getForegroundColor() {
        return foregroundColor;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public int getPadding() {
        return padding;
    }



    public double getFontHeightToWidth(){
        return 2.0; // todo!!!
    }
}
