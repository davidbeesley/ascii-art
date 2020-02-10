package com.github.davidbeesley.asciiart.exec;


import com.github.davidbeesley.asciiart.domain.ImageProcessorSettings;
import com.github.davidbeesley.asciiart.domain.ImageWrapper;
import com.github.davidbeesley.asciiart.domain.StyleSettings;
import com.github.davidbeesley.asciiart.domain.TextSource;
import com.github.davidbeesley.asciiart.task.Engine;
import com.github.davidbeesley.asciiart.util.Dimension;
import com.github.davidbeesley.asciiart.util.FontWrapper;
import com.github.davidbeesley.asciiart.util.ImageType;
import com.github.davidbeesley.asciiart.util.logger.LogLevel;
import com.github.davidbeesley.asciiart.util.logger.Logger;
import org.eoti.awt.WebColor;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import com.github.davidbeesley.asciiart.util.Util;

import java.awt.*;
import java.io.File;
import java.util.concurrent.Callable;

@Command(description = "Ascii Art generator", name = "AsciiArt", mixinStandardHelpOptions = true, version = "AsciiArt 2.0")
public class AsciiArt implements Callable<Integer> {



    private enum Algorithm{
        map, sample, fonts, colors;
    }




    @Option(names = {"-a", "--algorithm"}, description = "Valid values: ${COMPLETION-CANDIDATES}")
    Algorithm alg = Algorithm.map;

    @Option(names = {"-e", "--ext"}, description = "Output format. Valid values: ${COMPLETION-CANDIDATES}")
    ImageType outputType = ImageType.PNG;
    String outputExtension;



    @Option(names = {"-i", "--image"}, description = "Source image", paramLabel = "<FILE>")
    File imageFile;

    @Option(names = {"-o", "--out"}, description = "Output filename", paramLabel = "<FILE>")
    File outFile;

    @Option(names = {"-t", "--text"}, description = "Source text", paramLabel = "<FILE>")
    File textFile;

    @Option(names = {"-x","--dx"}, description = "Desired output dimension x") int dx = -1;
    @Option(names = {"-y", "--dy"}, description = "Desired output dimension y") int dy = -1;
    Dimension dim;

    @Option(names = {"-b", "--border"}, description = "Border width") int border = 30;

    @Option(names = {"--bgc"}, description = "Background color.", paramLabel = "<COLOR>")
    WebColor backgroundWebColor = WebColor.White;
    Color backgroundColor;

    @Option(names = {"--fgc"}, description = "Foreground color.", paramLabel = "<COLOR>")
    WebColor foregroundWebColor = WebColor.Black;
    Color foregroundColor;

    @Option(names = {"-m", "--matcher"}, description = "Silhouette color to match.", paramLabel = "<COLOR>")
    WebColor matcherWebColor= WebColor.Black;
    Color matcherColor;

    @Option(names = {"--angle"}, description = "Matching angle", paramLabel = "<ANGLE>")
    double matchAngle = .5;

    @Option(names = {"--invert"}, description = "Inverts image (silo) or characters matching (console, pixelswap).")
    boolean invert = false;

    @Option(names = {"-f", "--font"}, description = "Font", paramLabel = "<FONT>") String fontString = Font.MONOSPACED;
    Font font;

    @Option(names = {"-s", "--size"}, description = "Font size", paramLabel = "<SIZE>")
    int fontSize = 12;

    @Option(names = {"--logging"},  description = "Valid values: ${COMPLETION-CANDIDATES}")
    LogLevel logLevel = LogLevel.WARNING;

    @Option(names = {"--bold"}, description = "Make font bold")
    boolean bold = false;

    @Option(names = {"--italic"}, description = "Make font italic")
    boolean italic = false;





    @Override
    public Integer call() {
        Logger.getInstance().setLogLevel(logLevel);
        Logger.getInstance().trace("Started");


        // Colors
        backgroundColor = backgroundWebColor.getColor();
        foregroundColor = foregroundWebColor.getColor();
        matcherColor = matcherWebColor.getColor();




        int fontType = Font.PLAIN;
        if (bold && italic){
            fontType = Font.BOLD + Font.ITALIC;
        } else if (bold){
            fontType = Font.BOLD;
        } else if (italic){
            fontType = Font.ITALIC;
        }
        // Font
        font =  new Font(fontString, fontType, fontSize);
        switch (outputType){
            case PNG:
                outputExtension = "png";
                break;
            case JPG:
                outputExtension = "jpg";
                break;
        }


        switch (alg){

            case fonts:
                ListFonts.printFonts();
                return 0;


            case sample:
                if (imageFile == null){
                    System.out.println("sample requires option '--image=<FILE>'");
                    return 0;
                }

                return sample();


            case map:
                return map();

            case colors:
                return colors();

        }


        return 0; // exit code
    }


    public static void main(String... args) { // bootstrap the application
        System.exit(new CommandLine(new AsciiArt()).setCaseInsensitiveEnumValuesAllowed(true).execute(args));
    }



    private int sample(){
        Logger.getInstance().trace("Sampling");
        if (imageFile == null){
            System.out.println("sample requires option '--image=<FILE>'");
            return 0;
        }
        ImageWrapper imageWrapper = new ImageWrapper(Util.readImage(imageFile));
        SampleAngleSizes sampleAngleSizes = new SampleAngleSizes(new Engine(), matcherColor, imageWrapper, invert);
        sampleAngleSizes.make();
        return 0;
    }

    private int map(){
        Logger.getInstance().trace("Mapping");
        if (imageFile == null){
            System.out.println("map requires option '--image=<FILE>'");
            return 0;
        }
        if (textFile == null){
            System.out.println("map requires option '--text=<FILE>'");
            return 0;
        }
        // output file
        if (outFile == null){
            outFile =  new File(Util.stripExtension(imageFile.getName()) + "." + outputExtension);
        }
        FontWrapper fontWrapper = new FontWrapper(font);
        StyleSettings styleSettings = new StyleSettings(fontWrapper, foregroundColor, backgroundColor, border, outputType);
        ImageWrapper imageWrapper = new ImageWrapper(Util.readImage(imageFile));
        ImageProcessorSettings imageProcessorSettings = new ImageProcessorSettings(matcherColor, matchAngle, invert);
        TextSource textSource = new TextSource(textFile);
        MapTextToShape mapTextToShape = new MapTextToShape(new Engine(), styleSettings, imageProcessorSettings, textSource, imageWrapper);
        ImageWrapper output = mapTextToShape.make();
        Util.writeImage(output.getBufferedImage(), outFile, outputType.toString());
        return 0;
    }

    private int colors(){
        for (WebColor c: WebColor.values()){
            System.out.println(c);
        }
        return 0;
    }



}

/* Potential algorithms

     private void pixelswap(){
        BufferedImage source = Util.readImage(imageFile);
        if (dim == null){
            dim = Canvas.getRecommendedDimension(source);
        }
        Logger.info("Using height and width: " + dim.getHeight()+ " " + dim.getWidth());
        Canvas canvas = new Canvas(source, dim);
        canvas.addBorder(border);

        Set<Character> charSet = CharSetProvider.getLargeSet();
        ICharProvider charProvider = new WhitespaceRanked(font, charSet, invert);
        PixelProvider pixelProvider = new PixelProvider(font, charSet);


        canvas.setCharProvider(charProvider);
        canvas.setColorProvider(new ColorProvider());
        canvas.setBackground(backgroundColor);
        BufferedImage image = canvas.generateASCII(pixelProvider);
        Util.writeImage(image, outFile, outputExtension);
    }

    private void console(){
        BufferedImage source = Util.readImage(imageFile);
        if (dim == null){
            Dimension dim1 = Canvas.getRecommendedDimension(source);
            int h = dim1.getHeight();
            int w = dim1.getWidth();
            while(h > 30 || w > 60){
                h = (int) (h*.9);
                w = (int) (w*.9);
            }
            dim = new Dimension(h,w);
        }
        Logger.info("Using height and width: " + dim.getHeight()+ " " + dim.getWidth());
        Canvas canvas = new Canvas(source, dim);
        canvas.addBorder(border);

        Set<Character> charSet = CharSetProvider.getLargeSet();
        ICharProvider charProvider = new WhitespaceRanked(font, charSet, invert);


        canvas.setCharProvider(charProvider);
        canvas.setColorProvider(new ColorProvider());
        canvas.printToConsole();
    }
     private void dim(){
        BufferedImage source = Util.readImage(imageFile);
        dim = Canvas.getRecommendedDimension(source);
        System.out.println("dx: " + dim.getWidth() + "\tdy: " + dim.getHeight());
    }
 */

