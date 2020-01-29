package exec;

import art.Canvas;
import logger.LogLevel;
import logger.Logger;
import picocli.CommandLine;
import picocli.CommandLine.*;
import provider.CharSetProvider;
import provider.character.ICharProvider;
import provider.character.WhitespaceRanked;
import provider.color.ColorProvider;
import provider.font.FontProvider;
import provider.pixel.PixelProvider;
import util.Dimension;
import util.Util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Field;
import java.rmi.server.ExportException;
import java.util.Set;
import java.util.concurrent.Callable;

@Command(description = "Ascii Art generator", name = "AsciiArt", mixinStandardHelpOptions = true, version = "AsciiArt 1.0")
public class AsciiArt implements Callable<Integer> {

    private enum Algorithm{CONSOLE, FONTS, PIXELSWAP;}

    private enum OutputType{JPG, PNG;}


    @Option(names = {"-a", "--algorithm"}, required = true, description = "Valid values: ${COMPLETION-CANDIDATES}")
    Algorithm alg;

    @Option(names = {"-t", "--type"}, description = "Output format. Valid values: ${COMPLETION-CANDIDATES}")
    OutputType outputType = OutputType.PNG;
    String outputExtension;



    @Option(names = {"-i", "--image"}, description = "source image", paramLabel = "<FILE>")
    File imageFile;

    @Option(names = {"--dx"}, description = "desired output dimension x") int dx = -1;
    @Option(names = {"--dy"}, description = "desired output dimension y") int dy = -1;
    Dimension dim;

    @Option(names = {"-b", "--border"}, description = "border width") int border = 30;

    @Option(names = {"--bgc"}, description = "background color", paramLabel = "<COLOR>") String backgroundColorString = "WHITE";
    Color backgroundColor;

    @Option(names = {"--fgc"}, description = "foreground color", paramLabel = "<COLOR>") String foregroundColorString = "BLACK";
    Color foregroundColor;

    @Option(names = {"--invert"}, description = "inverts image (i.e. black to white or character large to small")
    boolean invert = false;

    @Option(names = {"-f", "--font"}, description = "font", paramLabel = "<FONT>") String fontString = Font.MONOSPACED;
    Font font;

    @Option(names = {"-s", "--size"}, description = "font size", paramLabel = "<SIZE>")
    int fontSize = 12;

    @Option(names = {"--logging"},  description = "Valid values: ${COMPLETION-CANDIDATES}")
    LogLevel logLevel = LogLevel.WARNING;

    @Option(names = {"--bold"}, description = "make font bold")
    boolean bold = false;

    @Option(names = {"--italic"}, description = "make font italic")
    boolean italic = false;





    @Override
    public Integer call() {
        Logger.setLogLevel(logLevel);
        // Build Dimension
        if (dx != -1 && dy != -1){
            dim = new Dimension(dx, dy);
        } else if (dx != -1 || dy != -1){
            Logger.warning("Both -dx and -dy must be specified to supply an output dimension");
            return 0;
        }


        // Colors
        Color color;
        try {
            backgroundColor = parseColor(backgroundColorString);
            Logger.info("Background color set to " + backgroundColorString);
            foregroundColor = parseColor(foregroundColorString);
            Logger.info("Foreground color set to " + foregroundColorString);

        } catch (Exception e) {
            return 0;
        }

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
            case PIXELSWAP:
                if (imageFile == null){
                    System.out.println("pixelswap requires option '--image=<FILE>'");
                    return 0;
                }
                pixelswap();
                break;
            case FONTS:
                FontProvider.printFonts();
                break;
            case CONSOLE:
                console();
                break;
        }


        return 0; // exit code
    }


    public static void main(String... args) { // bootstrap the application
        System.exit(new CommandLine(new AsciiArt()).setCaseInsensitiveEnumValuesAllowed(true).execute(args));
    }

    private void pixelswap(){
        BufferedImage source = Util.readImage(imageFile);
        if (dim == null){
            dim = art.Canvas.getRecommendedDimension(source);
        }
        Logger.info("Using height and width: " + dim.getHeight()+ " " + dim.getWidth());
        art.Canvas canvas = new Canvas(source, dim);
        canvas.addBorder(border);

        Set<Character> charSet = CharSetProvider.getLargeSet();
        ICharProvider charProvider = new WhitespaceRanked(font, charSet, invert);
        PixelProvider pixelProvider = new PixelProvider(font, charSet);


        canvas.setCharProvider(charProvider);
        canvas.setColorProvider(new ColorProvider());
        canvas.setBackground(backgroundColor);
        BufferedImage image = canvas.generateASCII(pixelProvider);
        Util.writeImage(image,  Util.stripExtension(imageFile.getName()), outputExtension);
    }

    private void console(){
        BufferedImage source = Util.readImage(imageFile);
        if (dim == null){
            Dimension dim1 = art.Canvas.getRecommendedDimension(source);
            int h = dim1.getHeight();
            int w = dim1.getWidth();
            while(h > 30 || w > 60){
                h = (int) (h*.9);
                w = (int) (w*.9);
            }
            dim = new Dimension(h,w);
        }
        Logger.info("Using height and width: " + dim.getHeight()+ " " + dim.getWidth());
        art.Canvas canvas = new Canvas(source, dim);
        canvas.addBorder(border);

        Set<Character> charSet = CharSetProvider.getLargeSet();
        ICharProvider charProvider = new WhitespaceRanked(font, charSet, invert);
        PixelProvider pixelProvider = new PixelProvider(font, charSet);


        canvas.setCharProvider(charProvider);
        canvas.setColorProvider(new ColorProvider());
        canvas.printToConsole();
    }
    private Color parseColor(String colorString) throws Exception {
        try {
            Field field = Class.forName("java.awt.Color").getField(backgroundColorString);
            return (Color) field.get(null);
        } catch (Exception e) {
            Logger.warning("Color " + backgroundColorString + " not found.");
            throw e;
        }
    }



}


