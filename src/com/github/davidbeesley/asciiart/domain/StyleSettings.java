package com.github.davidbeesley.asciiart.domain;

import com.github.davidbeesley.asciiart.util.FontWrapper;
import com.github.davidbeesley.asciiart.util.ImageType;
import com.github.davidbeesley.asciiart.util.Padding;

import java.awt.*;

public class StyleSettings {
    private double density;
    private FontWrapper font;
    private Color foregroundColor, backgroundColor;
    private Padding padding;
    private ImageType imageType;

    public StyleSettings(double density, FontWrapper font, Color foregroundColor, Color backgroundColor, Padding padding, ImageType imageType) {
        this.density = density;
        this.font = font;
        this.foregroundColor = foregroundColor;
        this.backgroundColor = backgroundColor;
        this.padding = padding;
        this.imageType = imageType;
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

    public Padding getPadding() {
        return padding;
    }

    public ImageType getImageType() {
        return imageType;
    }
}
