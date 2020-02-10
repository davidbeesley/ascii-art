package com.github.davidbeesley.asciiart.util;


import com.github.davidbeesley.asciiart.util.logger.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Util {

    public static BufferedImage readImage(File filename){
        BufferedImage image = null;
        try {
             image = ImageIO.read(filename);

        } catch (IOException e){
            //Logger.warning("Invalid image: "+ filename.getName());
            //Logger.trace(e.getMessage());
            System.exit(1);
        }
        return image;
    }

    public static void writeImage(BufferedImage image, File outputFile, String extension) {
        String filepath = outputFile.getPath();
        File directory = outputFile.getParentFile();
        if (directory != null && ! directory.exists()){
            directory.mkdirs();
        }
        Logger.getInstance().info("Printing image to: " + filepath);
        try {
            if (ImageIO.write(image, extension, outputFile)== false){
                throw new IOException("Failed to print");
            }
            //Logger.message("Image printed to: " + filepath);
        } catch (IOException e){
            //Logger.warning("Invalid filename: "+ filepath);
            //Logger.trace(e.getMessage());
            System.exit(1);
        }
    }



    public static String stripExtension(String filename){
        return filename.substring(0, filename.lastIndexOf('.'));


    }

    public static String getExtension(String filename){
        return filename.substring(filename.lastIndexOf('.'));


    }

    public static double getImageHeightToWidth(Dimension dim){
        double d = dim.getHeight() * 1.0 / dim.getWidth();
        Logger.getInstance().debug(d + "");
        return d;
    }

    public static int calculateAspectRatioHeight(int width, double imageHeightToWidth, double fontHeightToWidth){
        double heightD = width * imageHeightToWidth / fontHeightToWidth;
        int height = (int) Math.round(heightD);
        return height;
    }

    public static double booleanToDouble(boolean b){
        return b ? 1.0 : 0.0;
    }

    public static boolean doubleToBoolean(double d){
        return (d >= 0.5);
    }

}
