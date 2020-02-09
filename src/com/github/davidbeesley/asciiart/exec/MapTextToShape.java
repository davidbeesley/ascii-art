package com.github.davidbeesley.asciiart.exec;

import com.github.davidbeesley.asciiart.domain.*;
import com.github.davidbeesley.asciiart.task.EngineInterface;
import com.github.davidbeesley.asciiart.util.Dimension;

public class MapTextToShape {
    private StyleSettings styleSettings;
    private ImageProcessorSettings imageProcessorSettings;
    private TextSource textSource;
    private ImageWrapper imageWrapper;
    private EngineInterface engine;

    public MapTextToShape(EngineInterface engine, StyleSettings styleSettings, ImageProcessorSettings imageProcessorSettings, TextSource textSource, ImageWrapper imageWrapper) {
        this.styleSettings = styleSettings;
        this.imageProcessorSettings = imageProcessorSettings;
        this.textSource = textSource;
        this.imageWrapper = imageWrapper;
        this.engine = engine;
    }

    public ImageWrapper make(){
        BooleanMatrix initialBooleanMatrix = engine.imageToBooleanMatrix(imageWrapper, imageProcessorSettings);
        Tokens tokens = engine.tokenize(textSource);
        boolean succeeded = false;
        double density = .98;
        while (succeeded == false){
            styleSettings.setDensity(density);
            Dimension dimension = engine.getRecommendedDimensions(initialBooleanMatrix, styleSettings, tokens);
            BooleanMatrix scaledMatrix = engine.scaleBooleanMatrix(initialBooleanMatrix, dimension);
            MappedTextList textList = engine.getMappedTextList(scaledMatrix);
            succeeded = engine.mapText(textList, tokens);
        }
        MappedTextList mappedText = engine.retrieveMappedText();
        AsciiMatrix asciiMatrix = engine.convertToMatrix(mappedText);
        return engine.convertToImage(asciiMatrix, styleSettings);
    }
}
