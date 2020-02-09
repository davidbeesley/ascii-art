package art;

import provider.pixel.BooleanPixelProvider;
import com.github.davidbeesley.asciiart.util.Dimension;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BooleanCanvas {
    private Boolean[][] pixels;
    private Double[][] doubles;

    private BufferedImage originalImage;
    private BufferedImage generatedImage;
    private int pixelHeight;
    private int pixelWidth;


    public BooleanCanvas(BufferedImage image){

        int height = image.getHeight() * 1 / 2;
        int width = image.getWidth();
        while(height > 65){
            height = height * 9 / 10;
            width = width * 9 / 10;
        }
        while(width > 130){
            height = height * 9 / 10;
            width = width * 9 / 10;
        }
        init(image, height, width);

    }

    public BooleanCanvas(BufferedImage image, int height, int width){
        init(image, height, width);
    }

    public BooleanCanvas(BufferedImage image, Dimension dim){
        init(image, dim.getHeight(), dim.getWidth());
    }


    private void init(BufferedImage image, int height, int width){
        //Logger.trace("Height: " + height + " Width: " + width);
        pixels = new Boolean[height][width];
       // doubles = new Double[height][width];
        originalImage = image;
        pixelHeight = height;
        pixelWidth = width;

    }




    public Boolean[][] generateBooleanMap(BooleanPixelProvider booleanProvider){




        populatePixels(booleanProvider);
        return pixels;


    }




    public  void populatePixels(BooleanPixelProvider booleanProvider){

        int heightIncrease = originalImage.getHeight() / (pixelHeight);
        int widthIncrease = originalImage.getWidth() / (pixelWidth);

        if (heightIncrease == 0 || widthIncrease == 0){
           // Logger.trace("Using modified formula");
            double heightRatio = originalImage.getHeight() * 1.0 / pixelHeight;
            double widthRatio =  originalImage.getWidth() * 1.0 / pixelWidth;
            for (int h = 0; h < pixelHeight;  h++) {
                for (int w = 0; w < pixelWidth; w++) {


                    int heightVal = (int) (h * heightRatio);
                    int widthVal =  (int) (w * widthRatio);
                    if (heightVal >= originalImage.getHeight()) heightVal = originalImage.getHeight() -1;
                    if (widthVal >= originalImage.getWidth()) widthVal = originalImage.getWidth() -1;
                    Color current = new Color(originalImage.getRGB(widthVal, heightVal));
                    //System.out.println("widthVal: " + widthVal + " heightval: " + ori);
                    pixels[h][w ] = booleanProvider.getBoolean(current);
                   // doubles[h][w] = booleanProvider.getDouble(current);

                }
            }

            //Logger.trace("Population Complete");
            return;
        }

        for (int h = 0; h < pixelHeight;  h++) {
            for (int w = 0; w < pixelWidth; w++) {

                int red = 0;
                int blue = 0;
                int green = 0;
                for (int k = 0; k < heightIncrease; k++){
                    for (int l = 0; l < widthIncrease; l++){

                        int heightVal = (int) (originalImage.getHeight() * (h/ (1.0 * pixelHeight) ) + k);
                        int widthVal =  (int) (originalImage.getWidth() * (w / (1.0 * pixelWidth) ) + l);
                        Color current = new Color(originalImage.getRGB(widthVal, heightVal));
                        red += current.getRed();
                        blue += current.getBlue();
                        green += current.getGreen();
                    }

                }
                int avgSize = (heightIncrease * widthIncrease);
                red = red / avgSize;
                blue = blue / avgSize;
                green = green / avgSize;
                Color average = new Color(red, green, blue);

                pixels[h][w ] = booleanProvider.getBoolean(average);
                //doubles[h][w] = booleanProvider.getDouble(average);

            }
        }



    }








    public int getPixelHeight() {
        return pixelHeight;
    }

    public int getPixelWidth() {
        return pixelWidth;
    }

    public void printMap(){
    printMap(pixels);
    }

    public void printDoubles() {
        for (Double[] row : doubles){
            for (Double d : row){
                //int out = (int) (d * 1000);
                System.out.printf("%.3f ", d);

            }
            System.out.println();
        }
    }
    public static void printMap(Boolean[][] pixels){
        for (Boolean[] row : pixels){
            for (Boolean b : row){
                String text = b ? "1": "0";
                System.out.print(text);

            }
            System.out.println();
        }
    }



}
