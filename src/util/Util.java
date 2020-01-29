package util;

import logger.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

public class Util {

    public static BufferedImage readImage(File filename){
        BufferedImage image = null;
        try {
             image = ImageIO.read(filename);

        } catch (IOException e){
            Logger.warning("Invalid image: "+ filename.getName());
            Logger.trace(e.getMessage());
            System.exit(1);
        }
        return image;
    }

    public static void writeImage(BufferedImage image, String filename, String extension) {
        String filepath = filename + "." + extension;
        Logger.debug("Printing image to: " + filepath);
        try {
            if (ImageIO.write(image, extension, new File(filepath))== false){
                throw new IOException("Failed to print");
            }
            Logger.message("Image printed to: " + filepath);
        } catch (IOException e){
            Logger.warning("Invalid filename: "+ filepath);
            Logger.trace(e.getMessage());
            System.exit(1);
        }
    }



    public static String stripExtension(String filename){
        return filename.substring(0, filename.lastIndexOf('.'));


    }



}
