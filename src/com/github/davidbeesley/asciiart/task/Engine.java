package com.github.davidbeesley.asciiart.task;

import com.github.davidbeesley.asciiart.domain.*;
import com.github.davidbeesley.asciiart.util.ColorVectorUtil;
import com.github.davidbeesley.asciiart.util.Dimension;
import com.github.davidbeesley.asciiart.util.Util;
import com.github.davidbeesley.asciiart.util.logger.Logger;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Engine implements EngineInterface {
    private MappedSequenceList list;
    @Override
    public BooleanMatrix imageToBooleanMatrix(ImageWrapper imageWrapper, ImageProcessorSettings settings) {
        Dimension dim = imageWrapper.getImageDimension();
        BooleanMatrix mat = new BooleanMatrix(dim);
        for (int x = 0; x < dim.getWidth(); x ++){
            for (int y = 0; y < dim.getHeight(); y++){
                double angle = ColorVectorUtil.getColorSimilarity(settings.getMatchColor(), imageWrapper.getColorAt(x,y));
                boolean matched = angle < settings.getMatchAngle();
                if (settings.isInverse()) matched = !matched;
                mat.setPoint(x, y, matched);
            }
        }
        Logger.getInstance().info("BooleanMatrix Density is " + mat.getDensity());
        return mat;
    }

    @Override
    public Dimension getRecommendedDimensions(BooleanMatrix booleanMatrix, StyleSettings settings, Tokens tokens) {
        int chars = tokens.getCharacterCount();
        double imageDensity = booleanMatrix.getDensity();
        double fontHeightToWidth = settings.getFontHeightToWidth();
        double imageHeightToWidth = Util.getImageHeightToWidth(booleanMatrix.getDim());

        int width = 1;
        int height = 1;
        while(width*height*imageDensity*settings.getDensity() < chars){
            width += 1;
            height = Util.calculateAspectRatioHeight(width, imageHeightToWidth, fontHeightToWidth);
        }
        Logger.getInstance().debug("height " + height + " width " + width);
        return new Dimension(height, width);
    }

    @Override
    public BooleanMatrix scaleBooleanMatrix(BooleanMatrix oldMatrix, Dimension newDimension) {
        BooleanMatrix newMatrix = new BooleanMatrix(newDimension);

        // bicubic interpolation
        for (int x = 0; x < newDimension.getWidth(); x++){
            for (int y = 0; y < newDimension.getHeight(); y++){
                double oldX = x * 1.0 / newDimension.getWidth() * oldMatrix.getDim().getWidth();
                double oldY = y * 1.0 / newDimension.getHeight() * oldMatrix.getDim().getHeight();
                int x1 = (int) oldX;
                int x2 = x1 + 1;
                int y1 = (int) oldY;
                int y2 = y1 + 1;

                if (x2 >= oldMatrix.getDim().getWidth()){
                    x2 = x1;
                }
                if (y2 >= oldMatrix.getDim().getHeight()){
                    y2 = y1;
                }

                double pTopLeft = Util.booleanToDouble(oldMatrix.getPoint(x1, y1));
                double pTopRight = Util.booleanToDouble(oldMatrix.getPoint(x2, y1));
                double pBottomLeft = Util.booleanToDouble(oldMatrix.getPoint(x1, y2));
                double pBottomRight = Util.booleanToDouble(oldMatrix.getPoint(x2, y2));

                double dX = oldX - x1;
                double dY = oldY - y1;

                double top = dX * pTopRight + (1-dX) * pTopLeft;
                double bottom = dX * pBottomRight + (1-dX) * pBottomLeft;

                double point = dY * bottom + (1-dY) * top;
                newMatrix.setPoint(x, y, Util.doubleToBoolean(point));
            }
        }
        return newMatrix;
    }

    @Override
    public Tokens tokenize(TextSource text) {
        return new Tokens(text.getWords());
    }

    @Override
    public MappedSequenceList createMappedTextList(BooleanMatrix booleanMatrix) {
        MappedSequenceList list = new MappedSequenceList(booleanMatrix);
        list.map();
        return list;
    }

    @Override
    public boolean mapText(MappedSequenceList textList, Tokens tokens) {
        list = textList;
        return TextMapper.mapText(list, tokens);
    }

    @Override
    public MappedSequenceList retrieveMappedText() {
        Logger.getInstance().debug("List retrieved");
        return list;
    }

    @Override
    public AsciiMatrix convertToMatrix(MappedSequenceList textList, Dimension dim) {
        AsciiMatrix matrix = new AsciiMatrix(dim);
        for (Sequence s: textList.getList()){
            matrix.setString(s.getX(), s.getY(), s.build());
        }
        return matrix;
    }

    @Override
    public ImageWrapper convertToImage(AsciiMatrix asciiMatrix, StyleSettings settings) {
        Logger.getInstance().info("Building image");
        final HashMap<RenderingHints.Key, Object> RenderingProperties = new HashMap<>();

        RenderingProperties.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        RenderingProperties.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        RenderingProperties.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

        Dimension asciiDim = asciiMatrix.getDim();

        int imageHeight = asciiDim.getHeight() * settings.getFont().getHeight();
        int imageWidth = asciiDim.getWidth() * settings.getFont().getWidth();
        int pad = settings.getPadding();

        BufferedImage img = new BufferedImage(2 * pad + imageWidth, 2 * pad + imageHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();

        g2d.setRenderingHints(RenderingProperties);

        g2d.setBackground(settings.getBackgroundColor());
        g2d.setColor(settings.getForegroundColor());

        g2d.clearRect(0, 0, img.getWidth(), img.getHeight());

        g2d.setFont(settings.getFont().getFont());

        for(int x = 0; x < asciiDim.getWidth(); x++){
            for (int y = 0; y < asciiDim.getHeight(); y++){
                g2d.drawString(asciiMatrix.getPoint(x,y)+"", pad + x * settings.getFont().getWidth(), pad + (y+1) * settings.getFont().getHeight());
            }
        }

        g2d.dispose();
        Logger.getInstance().info("Building Complete");
        return new ImageWrapper(img);
    }
}
