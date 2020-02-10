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
        Logger.getInstance().trace("Mapping\n"+engine.createMappedTextList(debugMatrix, 1.0));
        Tokens tokens = engine.tokenize(textSource);
        boolean succeeded = false;
        double density = .995;
        Dimension dimension = null;
        while (succeeded == false){
            Logger.getInstance().info("Attempting to map with density " + density);
            styleSettings.setDensity(density);
            dimension = engine.getRecommendedDimensions(initialBooleanMatrix, styleSettings, tokens);
            BooleanMatrix scaledMatrix = engine.scaleBooleanMatrix(initialBooleanMatrix, dimension);
            //Logger.getInstance().debug("\n" + scaledMatrix);

            MappedSequenceList textList = engine.createMappedTextList(scaledMatrix, density);
            //Logger.getInstance().debug("\n" + textList);
            Logger.getInstance().debug("Necessary chars " + tokens.getCharacterCount() + " available chars " + textList.getTotalCapacity());
            succeeded = engine.mapText(textList, tokens);
            density -= .005;
            if (density < .5){
                Logger.getInstance().error("Mapping failed.");
                System.exit(1);
            }
        }
        Logger.getInstance().info("Mapping succeeded with density " + density);
        MappedSequenceList mappedText = engine.retrieveMappedText();
        AsciiMatrix asciiMatrix = engine.convertToMatrix(mappedText, dimension);
        return engine.convertToImage(asciiMatrix, styleSettings);
    }
}
