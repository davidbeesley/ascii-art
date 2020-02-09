package com.github.davidbeesley.asciiart.task;

import com.github.davidbeesley.asciiart.domain.*;
import com.github.davidbeesley.asciiart.util.Dimension;

public interface EngineInterface {
    BooleanMatrix imageToBooleanMatrix(ImageWrapper imageWrapper, ImageProcessorSettings settings);
    Dimension getRecommendedDimensions(BooleanMatrix booleanMatrix, StyleSettings settings, Tokens tokens);
    BooleanMatrix scaleBooleanMatrix(BooleanMatrix  booleanMatrix, Dimension newDimension);
    Tokens tokenize(TextSource text);
    MappedTextList getMappedTextList(BooleanMatrix booleanMatrix);
    boolean mapText(MappedTextList textList, Tokens tokens);
    MappedTextList retrieveMappedText();
    AsciiMatrix convertToMatrix(MappedTextList textList);
    ImageWrapper convertToImage(AsciiMatrix asciiMatrix, StyleSettings settings);

}
