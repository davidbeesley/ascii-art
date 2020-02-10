package com.github.davidbeesley.asciiart.domain;

import com.github.davidbeesley.asciiart.util.Dimension;
import com.github.davidbeesley.asciiart.util.logger.Logger;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class ImageWrapper {
    private BufferedImage bufferedImage;

    public ImageWrapper(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    public BufferedImage getBufferedImage(){
        return bufferedImage;
    }

    public Dimension getImageDimension(){
        return new Dimension(bufferedImage.getHeight(), bufferedImage.getWidth());
    }

    public Color getColorAt(int x, int y){
        int rgb = bufferedImage.getRGB(x,y);
        return new Color(rgb);
    }


}
