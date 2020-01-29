package provider.pixel;

import art.Canvas;
import art.Pixel;
import art.Point;
import logger.Logger;
import provider.CharSetProvider;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Set;

public class PixelProvider {

    private Font font;
    private int pixelWidth = 0, pixelHeight = 0;
    private BufferedImage defaultImg;
    //private static final String validChars = "abcdefghijklmnopqrstuvwyzABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890";
    public static final Color TRANSPARENT = new Color(0,0,0,0);

    public PixelProvider(Font font){
        this.font = font;
        defaultImg = new BufferedImage(1,1, BufferedImage.TYPE_INT_ARGB);

        setDimensions(CharSetProvider.getFullSet());
    }

    public PixelProvider(Font font, Set<Character> charSet){
        this.font = font;
        defaultImg = new BufferedImage(1,1, BufferedImage.TYPE_INT_ARGB);

        setDimensions(charSet);

    }

    public void setDimensions(Set<Character> charSet){
        pixelHeight = getCharHeight();
        for (Character c : charSet){
            if (getCharWidth(c) > pixelWidth) {
                pixelWidth =getCharWidth(c);
               // Logger.trace("Updated width to: " + pixelWidth + " for char: " + c);

            }
        }
        pixelWidth += pixelWidth/8 + 1;
        //pixelHeight = pixelHeight * 9 / 10;

        if (pixelHeight == 0) pixelHeight++;

        Logger.trace("Max height: " + pixelHeight + " Max width: " + pixelWidth);
    }

    public void setPixelWidth(char c) {
        this.pixelWidth = getCharWidth(c);
    }

    public void setPixelHeight(char c) {
        this.pixelHeight = getCharHeight();
    }

    public int getPixelWidth() {
        return pixelWidth;
    }

    public int getPixelHeight() {
        return pixelHeight;
    }

    public double getHeightToWidth() {
        double temp = pixelHeight * 1.0 / (pixelWidth);
        Logger.trace(temp + " " + (temp * Canvas.SCALE));
        return temp * Canvas.SCALE;
    }

    public Pixel getPixel(Point p){
        return getPixel(p.getText(), p.getColor(), p.getBackgroundColor());
    }


    public Pixel getPixel(char c, Color color, Color background){
        BufferedImage img = new BufferedImage(pixelWidth, pixelHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();


        //g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        //g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        //g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        //g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        //g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        //g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        //g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();

        g2d.setColor(background); //TRANSPARENT
        g2d.fillRect(0,0, pixelWidth, pixelHeight); // THIS WAS COMMENTED OUT
        g2d.setColor(color);
        if (font.canDisplay(c) == false){
            c = ' ';
            Logger.warning("Received invalid character.");
        }
        g2d.drawString(Character.toString(c), 0, fm.getAscent());
        g2d.dispose();
        //Util.writeImage(img, "example");
        return new Pixel(img, c);
    }

    private int getCharHeight(){
        Graphics2D g2d = defaultImg.createGraphics();
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        int height = fm.getAscent()+fm.getDescent();
        g2d.dispose();
        return height;
    }

    private int getCharWidth(char c){
        Graphics2D g2d = defaultImg.createGraphics();
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        int width = fm.stringWidth(Character.toString(c));
        g2d.dispose();
        return width;
    }


}
