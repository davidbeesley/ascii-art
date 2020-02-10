package com.github.davidbeesley.asciiart.exec;


import com.github.davidbeesley.asciiart.domain.*;
import com.github.davidbeesley.asciiart.task.EngineInterface;
import com.github.davidbeesley.asciiart.util.Dimension;
import com.github.davidbeesley.asciiart.util.logger.Logger;

import java.awt.*;

public class SampleAngleSizes {
    private Color matcherColor;
    private ImageWrapper imageWrapper;
    private EngineInterface engine;
    private boolean inverse;

    public SampleAngleSizes(EngineInterface engine, Color matcherColor, ImageWrapper imageWrapper, boolean inverse) {
        this.matcherColor = matcherColor;
        this.imageWrapper = imageWrapper;
        this.engine = engine;
        this.inverse = inverse;
    }

    public void make(){

        for (double d = 0.0; d < 1.5; d += .125)
        {
            BooleanMatrix initialBooleanMatrix = engine.imageToBooleanMatrix(imageWrapper, new ImageProcessorSettings(matcherColor, d, inverse));
            BooleanMatrix debugMatrix = engine.scaleBooleanMatrix(initialBooleanMatrix, new Dimension(20, 40));
            System.out.println("Angle " + d + "\n" + debugMatrix.toNoSpaceRepresentation());
        }

    }
}
