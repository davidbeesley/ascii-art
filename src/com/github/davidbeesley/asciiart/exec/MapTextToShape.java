package com.github.davidbeesley.asciiart.exec;

import com.github.davidbeesley.asciiart.domain.*;
import com.github.davidbeesley.asciiart.task.EngineInterface;
import com.github.davidbeesley.asciiart.util.Dimension;
import com.github.davidbeesley.asciiart.util.logger.Logger;

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
        BooleanMatrix debugMatrix = engine.scaleBooleanMatrix(initialBooleanMatrix, new Dimension(20,40));
        Logger.getInstance().debug("Boolean Silhouette:\n"+debugMatrix);
        Logger.getInstance().trace("Mapping\n"+engine.createMappedTextList(debugMatrix));
        Tokens tokens = engine.tokenize(textSource);
        boolean succeeded = false;
        double density = .98;
        Dimension dimension = null;
        while (succeeded == false){
            styleSettings.setDensity(density);
            dimension = engine.getRecommendedDimensions(initialBooleanMatrix, styleSettings, tokens);
            BooleanMatrix scaledMatrix = engine.scaleBooleanMatrix(initialBooleanMatrix, dimension);
            MappedSequenceList textList = engine.createMappedTextList(scaledMatrix);
            succeeded = engine.mapText(textList, tokens);
            density -= .01;
            if (density < .5){
                Logger.getInstance().error("Mapping failed.");
                System.exit(1);
            }
        }
        MappedSequenceList mappedText = engine.retrieveMappedText();
        AsciiMatrix asciiMatrix = engine.convertToMatrix(mappedText, dimension);
        return engine.convertToImage(asciiMatrix, styleSettings);
    }
}
