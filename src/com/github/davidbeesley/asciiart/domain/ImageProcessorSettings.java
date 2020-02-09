package com.github.davidbeesley.asciiart.domain;

import java.awt.*;

public class ImageProcessorSettings {
    private Color matchColor;
    private double matchAngle;

    public ImageProcessorSettings(Color matchColor, double matchAngle) {
        this.matchColor = matchColor;
        this.matchAngle = matchAngle;
    }

    public Color getMatchColor() {
        return matchColor;
    }

    public double getMatchAngle() {
        return matchAngle;
    }
}
