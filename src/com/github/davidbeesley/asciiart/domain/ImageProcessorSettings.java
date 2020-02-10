package com.github.davidbeesley.asciiart.domain;

import java.awt.*;

public class ImageProcessorSettings {
    private Color matchColor;
    private double matchAngle;
    private boolean inverse;

    public ImageProcessorSettings(Color matchColor, double matchAngle, boolean inverse) {
        this.matchColor = matchColor;
        this.matchAngle = matchAngle;
        this.inverse = inverse;
    }

    public Color getMatchColor() {
        return matchColor;
    }

    public double getMatchAngle() {
        return matchAngle;
    }

    public boolean isInverse(){
        return inverse;
    }
}
