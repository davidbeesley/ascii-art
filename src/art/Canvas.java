package art;

import loggerOLD.Logger;
import provider.character.CharProvider;
import provider.character.ICharProvider;
import provider.color.AsciiColorProvider;
import provider.color.ColorProvider;
import provider.color.IColorProvider;
import provider.pixel.PixelProvider;
import com.github.davidbeesley.asciiart.util.Dimension;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Canvas {


    private BufferedImage originalImage;
    private BufferedImage generatedImage;
    private int pixelHeight;
    private int pixelWidth;
    private int borderWidth = 0;
    private Color background = new Color(0,0,0,0);
    private ICharProvider charProvider;
    private IColorProvider colorProvider;
    private Color borderColor = Color.WHITE; // todo
    public static double SCALE = .9; // TODO?

    public Canvas(BufferedImage image){

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

    public Canvas(BufferedImage image, int height, int width){
        init(image, height, width);
    }

    public Canvas(BufferedImage image, Dimension dim){
        init(image, dim.getHeight(), dim.getWidth());
    }


    private void init(BufferedImage image, int height, int width){
        Logger.trace("Height: " + height + " Width: " + width);
        originalImage = image;
        pixelHeight = height;
        pixelWidth = width;
        charProvider = new CharProvider();
        colorProvider = new ColorProvider();
    }

    public void addBorder(int width){
        borderWidth = width;

    }

    public void printToConsole(){
        AsciiColorProvider cp = new AsciiColorProvider();
        Point[][] points = processImage();
        for (int i = 0; i < points.length; i++){
            for (int j = 0; j < points[0].length; j++){
                Point p = points[i][j];
                System.out.print(cp.getColor(p.color, p.text));
            }
            System.out.println();
        }
    }

    public void setBackground(Color background) {
        this.background = background;
    }

    public void setCharProvider(ICharProvider charProvider) {
        this.charProvider = charProvider;
    }

    public void setColorProvider(IColorProvider colorProvider) {
        this.colorProvider = colorProvider;
    }

    public BufferedImage generateASCII(PixelProvider pixelProvider){

        Point[][] points = processImage();


        //populatePixels(pixelProvider, points);
        return convertToImage(pixelProvider, points);



    }






    public static Color[][] getColorMap(int pixelHeight, int pixelWidth, BufferedImage originalImage){
        Color[][] averages = new Color[pixelHeight][pixelWidth];
        int heightIncrease = originalImage.getHeight() / (pixelHeight);
        int widthIncrease = originalImage.getWidth() / (pixelWidth);

        if (heightIncrease == 0 || widthIncrease == 0){
            Logger.trace("Using modified formula");
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
                    averages[h][w] = current;
                }
            }
            return averages;

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
                averages[h][w] = average;
            }
        }
        return averages;
    }

    private Point[][] processImage() {
        Color[][] averages = getColorMap(pixelHeight, pixelWidth, originalImage);
        Point[][] points = new Point[pixelHeight][pixelWidth];
        int heightIncrease = originalImage.getHeight() / (pixelHeight);
        int widthIncrease = originalImage.getWidth() / (pixelWidth);
        for (int h = 0; h < pixelHeight;  h++) {
            for (int w = 0; w < pixelWidth; w++) {

                Color average = averages[h][w];
                points[h][w] = new Point(colorProvider.getColor(average, h, w), colorProvider.getBackground(average, background, h, w), charProvider.getChar(average, h, w));
                //Logger.info(points[h][w].backgroundColor + "");
            }
        }
        return points;
    }



    private BufferedImage convertToImage(PixelProvider pixelProvider, Point[][] points){
        int imageWidth = pixelProvider.getPixelWidth() * pixelWidth ;
        int scaledPixelHeight = (int) (pixelProvider.getPixelHeight() * SCALE);
        int imageHeight = scaledPixelHeight * pixelHeight + (pixelProvider.getPixelHeight() - scaledPixelHeight);

        BufferedImage result = new BufferedImage(imageWidth + 2 * borderWidth, imageHeight + 2 * borderWidth, BufferedImage.TYPE_INT_RGB);

        //BufferedImage result = new BufferedImage(imageWidth + 2 * borderWidth, imageHeight + 2 * borderWidth, BufferedImage.TYPE_BYTE_GRAY);

        Logger.trace("Space for image was allocated. Hooray.");
        Graphics2D graphics2D = result.createGraphics();
        graphics2D.setColor(borderColor);
        graphics2D.fillRect(0,0, imageWidth + 2 *borderWidth, imageHeight + 2 * borderWidth);
        graphics2D.setColor(background);
        graphics2D.fillRect(borderWidth, borderWidth, imageWidth, imageHeight);
        graphics2D.dispose();

        for (int pixelH = 0; pixelH < pixelHeight; pixelH++){
           if (pixelH % 10 == 0) Logger.trace("Calculating row: " + pixelH + "/" + pixelHeight);

            for (int pixelW = 0; pixelW < pixelWidth; pixelW++){

                Pixel currentPixel = pixelProvider.getPixel(points[pixelH][pixelW]);
                //System.out.print(currentPixel.getChar());
                int h = pixelH * scaledPixelHeight;
                int w = pixelW * pixelProvider.getPixelWidth();
                for (int hOffset = 0; hOffset < pixelProvider.getPixelHeight() ;hOffset++){
                    for (int wOffset = 0; wOffset <pixelProvider.getPixelWidth(); wOffset++){
                        Color color = currentPixel.getColor(wOffset, hOffset);
                        //System.out.println(color.getRed() + " " + color.getBlue() + " " + color.getGreen() + " " + color.getAlpha());
                        if (color.getAlpha() != 0) {
                            //Logger.trace("THIS RAN");
                            result.setRGB(w +wOffset + borderWidth,h + hOffset+ borderWidth,color.getRGB());

                        }
                    }
                }
            }
            //System.out.println();
        }

        /*
        for (int h = 0; h < imageHeight; h++){
            //if (h % 1000 == 0)            Logger.trace("Calculating row: " + h + "/" + imageHeight);
            for (int w = 0; w < imageWidth; w++){
                int pixelH = h / pixelProvider.getPixelHeight();
                int pixelW = w / pixelProvider.getPixelWidth();
                int hOffset = h - (pixelH * pixelProvider.getPixelHeight());
                int wOffset = w - (pixelW * pixelProvider.getPixelWidth());

                Pixel currentPixel = pixelProvider.getPixel(points[pixelH][pixelW]);
                Color color = currentPixel.getColor(wOffset, hOffset);
                result.setRGB(w + borderWidth,h + borderWidth,color.getRGB());

            }
        }
        */
        return result;
    }


    public int getPixelHeight() {
        return pixelHeight;
    }

    public int getPixelWidth() {
        return pixelWidth;
    }

    public static Dimension getRecommendedDimension(BufferedImage img){
        double height = img.getHeight();
        double width = img.getWidth();
        double ratio = width / height * 2;
        Logger.trace("Ratio: " + ratio);



        int pixelHeight = 300;
        int pixelWidth = (int) (pixelHeight * ratio);

        while(img.getHeight() / pixelHeight <= 1 || img.getWidth() / pixelWidth <= 1){
            pixelHeight --;
            pixelWidth = (int) (pixelHeight * ratio);
        }

        Logger.trace("Using dimension: " + pixelHeight+"x"+pixelWidth);
        return new Dimension(pixelHeight, pixelWidth);

    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public Color[][] getColorMap(){
        return getColorMap(pixelHeight, pixelWidth, originalImage);
    }
}
