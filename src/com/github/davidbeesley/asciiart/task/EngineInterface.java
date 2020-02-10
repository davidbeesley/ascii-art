package com.github.davidbeesley.asciiart.task;

import com.github.davidbeesley.asciiart.domain.*;
import com.github.davidbeesley.asciiart.util.Dimension;

public interface EngineInterface {
    BooleanMatrix imageToBooleanMatrix(ImageWrapper imageWrapper, ImageProcessorSettings settings);
    Dimension getRecommendedDimensions(BooleanMatrix booleanMatrix, StyleSettings settings, Tokens tokens);
    BooleanMatrix scaleBooleanMatrix(BooleanMatrix  booleanMatrix, Dimension newDimension);
    Tokens tokenize(TextSource text);
    MappedSequenceList createMappedTextList(BooleanMatrix booleanMatrix);
    boolean mapText(MappedSequenceList textList, Tokens tokens);
    MappedSequenceList retrieveMappedText();
    AsciiMatrix convertToMatrix(MappedSequenceList textList, Dimension dim);
    ImageWrapper convertToImage(AsciiMatrix asciiMatrix, StyleSettings settings);

}
