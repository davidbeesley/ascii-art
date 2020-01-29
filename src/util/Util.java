package util;

import logger.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

public class Util {

    public static BufferedImage readImage(String filename){
        BufferedImage image = null;
        try {
             image = ImageIO.read(new File(filename));

        } catch (IOException e){
            Logger.warning("Invalid filename: "+ filename);
            Logger.trace(e.getMessage());
            System.exit(1);
        }
        return image;
    }

    public static void writeImage(BufferedImage image, String filename) {

        Logger.debug("Printing image to: " + filename + ".jpg");
        try {
            if (ImageIO.write(image, "jpg", new File(filename + ".jpg"))== false){
                throw new IOException("Failed to print");
            }
            Logger.trace("Image finished printing");
        } catch (IOException e){
            Logger.warning("Invalid filename: "+ filename);
            Logger.trace(e.getMessage());
            System.exit(1);
        }
    }


    public static void writeImagePNG(BufferedImage image, String filename) {

        Logger.debug("Printing image to: " + filename + ".png");
        try {
            if (ImageIO.write(image, "png", new File(filename + ".png"))== false){
                throw new IOException("Failed to print");
            }
            Logger.trace("Image finished printing");
        } catch (IOException e){
            Logger.warning("Invalid filename: "+ filename);
            Logger.trace(e.getMessage());
            System.exit(1);
        }
    }

    public static String stripExtension(String filename){
        return filename.substring(0, filename.lastIndexOf('.'));


    }



}
