package exec;

import art.BooleanCanvas;
import art.Canvas;
import files.Images;
import files.Texts;
import logger.Logger;
import provider.CharSetProvider;
import provider.character.*;
import provider.color.ColorProvider;
import provider.color.SingleColorProvider;
import provider.font.FontProvider;
import provider.font.Fonts;
import provider.pixel.BooleanPixelProvider;
import provider.pixel.PixelProvider;
import util.Dimension;
import util.Util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class ConvertToAscii {



    public static void main(String[] args){
        ConvertToAscii convertToAscii = new ConvertToAscii();







        //convertToAscii.processText(Images.TEMPLE_SILHOUETTE2, Texts.PROCLAMATION);

        //convertToAscii.processBooleanText(Images.TEMPLE_SILHOUETTE2, Texts.TESTER, 0.03, new Color(0,0,0,255), true);


        //convertToAscii.sampleBoolean(Images.TEMPLE_SILHOUETTE2, Color.BLACK);

        convertToAscii.processBooleanText(Images.TEMPLE, Texts.BOOK_OF_MORMON, 0.15, Color.RED, false);

        //convertToAscii.processBooleanText("salt-lake-temple-a-glimpse-of-heaven_red.jpg", "text/bookofmormon.txt",.15, colors, true);


        //convertToAscii.process(color_smooth, Color.DARK_GRAY, false);

        //convertToAscii.sampleBoolean("color_smooth.jpg", Color.BLACK);
        //convertToAscii.sampleBoolean("dave.png", Color.BLACK);
        //convertToAscii.sampleBoolean(Images.TEMPLE_SILHOUETTE, Color.BLACK);
        //convertToAscii.sampleBoolean("sinia.png", Color.BLUE);


        //convertToAscii.example();
    }


    public void process(String filename, Color background, boolean invert){
        BufferedImage source = Util.readImage(new File("originals/"+filename));
        Dimension dim = Canvas.getRecommendedDimension(source);
        Canvas canvas = new Canvas(source, 600,900);
        canvas.addBorder(30);

        Set<Character> charSet = CharSetProvider.getLargeSet();
        Font font =  new Font("Liberation Mono", Font.PLAIN, 18);
        ICharProvider charProvider = new WhitespaceRanked(font, charSet);
        PixelProvider pixelProvider = new PixelProvider(font, charSet);
        charProvider.invert(invert);


        canvas.setCharProvider(charProvider);
        canvas.setColorProvider(new ColorProvider());
        canvas.setBackground(background);
        BufferedImage image = canvas.generateASCII(pixelProvider);
        Util.writeImage(image, "generated/" + Util.stripExtension(filename));
    }

    public void processText(String filename, String sourceText){
        BufferedImage source = Util.readImage("originals/"+filename);
        TextManager textManager = new TextManager(sourceText);
        Font font =  Fonts.LIBERATIONSANS_18;
        //font = Fonts.DEJAVUSANSCONDENSED_9;
        font = Fonts.LATO_9;
        //font = Fonts.COURIER_9;

        PixelProvider pixelProvider = new PixelProvider(font, textManager.getCharSet());
        Dimension dim = textManager.getRecommendedDimension(source, pixelProvider.getHeightToWidth());
        Canvas canvas = new Canvas(source, dim);
        canvas.addBorder(30);

        //font = Fonts.ANI_18;
        ICharProvider charProvider= new TextFillProvider(textManager, canvas);
        Set<Character> charSet = charProvider.getCharSet();
        //CharProvider charProvider = new WhitespaceRanked(FontProvider.getMono(), charSet);




        canvas.setCharProvider(charProvider);
        canvas.setColorProvider(new SingleColorProvider(Color.BLACK));
        //canvas.setBackground(new Color(0,0,0,0));
        canvas.setBackground(Color.WHITE);
        //canvas.setBorderColor(Color.WHITE);
        BufferedImage image = canvas.generateASCII(pixelProvider);
        Util.writeImage(image, "generated/" + Util.stripExtension(filename));
    }

    public void processBooleanText(String filename, String textFile, double angle, Color color, boolean invert){

        BufferedImage source = Util.readImage("originals/"+filename);
        TextManager textManager = new TextManager(textFile);
        Font font = Fonts.LIBERATIONMONO_9;
        PixelProvider pixelProvider = new PixelProvider(font, textManager.getCharSet());
        Set<Color> matchers = new HashSet<>();
        matchers.add(color);
        BooleanPixelProvider booleanPixelProvider = new BooleanPixelProvider(angle, invert, matchers);

        Dimension dim = TextShapeProvider.getRecommendedDimension(source, textManager, booleanPixelProvider, pixelProvider.getHeightToWidth());
        BooleanCanvas booleanCanvas = new BooleanCanvas(source, dim);
        Boolean[][] map = booleanCanvas.generateBooleanMap(booleanPixelProvider);
        //booleanCanvas.printDoubles();
        //BooleanCanvas.printMap(map);

        ICharProvider charProvider =  new TextShapeProvider(map, textManager);



        Canvas canvas = new Canvas(source, dim);
        canvas.addBorder(1000);
        canvas.setCharProvider(charProvider);
        canvas.setColorProvider(new SingleColorProvider(Color.BLACK));
        canvas.setBackground(Color.WHITE);

        BufferedImage image = canvas.generateASCII(pixelProvider);
        Util.writeImage(image, "generated/" + Util.stripExtension(filename));
    }

    public void sampleBoolean(String filename, Color c) {
        BufferedImage source = Util.readImage("originals/" + filename);
        Dimension dim = new Dimension(23, 60);


        for (double d : BooleanPixelProvider.getSampleAngles()) {


            System.out.println("\n\n\nImage: " +filename + " angle: " + d);
            BooleanPixelProvider booleanPixelProvider = new BooleanPixelProvider(d);
            booleanPixelProvider.addMatcher(c);
            BooleanCanvas booleanCanvas = new BooleanCanvas(source, dim);
            Boolean[][] map = booleanCanvas.generateBooleanMap(booleanPixelProvider);
            booleanCanvas.printMap();
        }
    }


    public void processBoolean(String filename, double angle, Set<Color> matchers, boolean invert){
        BufferedImage source = Util.readImage("originals/"+filename);
        Dimension dim = Canvas.getRecommendedDimension(source);
        //dim = new Dimension(20,40);



        BooleanPixelProvider booleanPixelProvider = new BooleanPixelProvider(angle, invert);
        for (Color c : matchers) {
            booleanPixelProvider.addMatcher(c);
        }
        BooleanCanvas booleanCanvas = new BooleanCanvas(source, dim);

        Boolean[][] map = booleanCanvas.generateBooleanMap(booleanPixelProvider);
        //booleanCanvas.printMap();

        BooleanProvider booleanProvider = new BooleanProvider(map, '.');

        ICharProvider charProvider= booleanProvider;
        Set<Character> charSet = charProvider.getCharSet();
        //CharProvider charProvider = new WhitespaceRanked(FontProvider.getMono(), charSet);
        PixelProvider pixelProvider = new PixelProvider(FontProvider.getMonoSmall(), charSet);


        Canvas canvas = new Canvas(source, dim);
        canvas.addBorder(30);
        canvas.setCharProvider(charProvider);
        canvas.setColorProvider(new SingleColorProvider(Color.BLACK));
        canvas.setBackground(Color.WHITE);

        BufferedImage image = canvas.generateASCII(pixelProvider);
        Util.writeImage(image, "generated/" + Util.stripExtension(filename));
    }


    public void processBoolean(String filename, double angle, Set<Color> matchers){
      processBoolean(filename, angle, matchers, false);
    }

    public void process(String filename, Color background){
        process(filename, background, false);
    }

}
