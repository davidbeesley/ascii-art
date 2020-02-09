package util;

import loggerOLD.Logger;

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
            Logger.warning("Invalid image: "+ filename.getName());
            Logger.trace(e.getMessage());
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
        Logger.debug("Printing image to: " + filepath);
        try {
            if (ImageIO.write(image, extension, outputFile)== false){
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

    public static String getExtension(String filename){
        return filename.substring(filename.lastIndexOf('.'));


    }



}
