package exec;

import art.BooleanCanvas;
import art.Canvas;
import loggerOLD.LogLevel;
import loggerOLD.Logger;
import org.eoti.awt.WebColor;
import picocli.CommandLine;
import picocli.CommandLine.*;
import provider.CharSetProvider;
import provider.character.*;
import provider.color.ColorProvider;
import provider.color.SingleColorProvider;
import provider.font.FontProvider;
import provider.pixel.BooleanPixelProvider;
import provider.pixel.PixelProvider;
import com.github.davidbeesley.asciiart.util.Dimension;
import util.Util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

@Command(description = "Ascii Art generator", name = "AsciiArt", mixinStandardHelpOptions = true, version = "AsciiArt 1.0")
public class AsciiArt implements Callable<Integer> {

    private enum Algorithm{CONSOLE, FONTS, PIXELSWAP, SAMPLE, SILO, DIM, COLOR}

    private enum OutputType{JPG, PNG;}


    @Option(names = {"-a", "--algorithm"}, required = true, description = "Valid values: ${COMPLETION-CANDIDATES}")
    Algorithm alg;

    @Option(names = {"-e", "--ext"}, description = "Output format. Valid values: ${COMPLETION-CANDIDATES}")
    OutputType outputType = OutputType.PNG;
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
        Logger.setLogLevel(logLevel);
        // Build Dimension
        if (dx != -1 && dy != -1){
            dim = new Dimension(dy, dx);
        } else if (dx != -1 || dy != -1){
            Logger.warning("Both -dx and -dy must be specified to supply an output dimension");
            return 0;
        }




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

        // output file
        if (outFile == null && (alg == Algorithm.PIXELSWAP || alg == Algorithm.SILO )){
            outFile =  new File(Util.stripExtension(imageFile.getName()) + "." + outputExtension);
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
                if (imageFile == null){
                    System.out.println("console requires option '--image=<FILE>'");
                    return 0;
                }
                console();
                break;

            case SAMPLE:
                if (imageFile == null){
                    System.out.println("sample requires option '--image=<FILE>'");
                    return 0;
                }
                sample();
                break;

            case SILO:
                if (imageFile == null){
                    System.out.println("silo requires option '--image=<FILE>'");
                    return 0;
                }
                if (textFile == null){
                    System.out.println("silo requires option '--text=<FILE>'");
                    return 0;
                }
                silo();
                break;
            case DIM:
                if (imageFile == null){
                    System.out.println("dim requires option '--image=<FILE>'");
                    return 0;
                }
                dim();
                break;
            case COLOR:
                color();
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
            dim = Canvas.getRecommendedDimension(source);
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
        Util.writeImage(image, outFile, outputExtension);
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


        canvas.setCharProvider(charProvider);
        canvas.setColorProvider(new ColorProvider());
        canvas.printToConsole();
    }

    private void sample(){
        BufferedImage source = Util.readImage(imageFile);
        if (dim == null){
            Dimension dim1 = art.Canvas.getRecommendedDimension(source);
            int h = dim1.getHeight();
            int w = dim1.getWidth();
            while(h > 25 || w > 50){
                h = (int) (h*.9);
                w = (int) (w*.9);
            }
            dim = new Dimension(h,w);
        }

        for (double d : BooleanPixelProvider.getSampleAngles()) {


            System.out.println("\n\n\nImage: " +imageFile.getName() + " angle: " + d);
            BooleanPixelProvider booleanPixelProvider = new BooleanPixelProvider(d);
            booleanPixelProvider.addMatcher(matcherColor);
            BooleanCanvas booleanCanvas = new BooleanCanvas(source, dim);
            Boolean[][] map = booleanCanvas.generateBooleanMap(booleanPixelProvider);
            booleanCanvas.printMap();
        }
    }

    private void silo(){
        BufferedImage source = Util.readImage(imageFile);
        if (dim == null){
            dim = Canvas.getRecommendedDimension(source);
        }


        TextManager textManager = new TextManager(textFile);
        PixelProvider pixelProvider = new PixelProvider(font, textManager.getCharSet());
        Set<Color> matchers = new HashSet<>();
        matchers.add(matcherColor);
        BooleanPixelProvider booleanPixelProvider = new BooleanPixelProvider(matchAngle, invert, matchers);

        Dimension dim = TextShapeProvider.getRecommendedDimension(source, textManager, booleanPixelProvider, pixelProvider.getHeightToWidth());
        BooleanCanvas booleanCanvas = new BooleanCanvas(source, dim);
        Boolean[][] map = booleanCanvas.generateBooleanMap(booleanPixelProvider);
        //booleanCanvas.printDoubles();
        //BooleanCanvas.printMap(map);

        ICharProvider charProvider =  new TextShapeProvider(map, textManager);



        Canvas canvas = new Canvas(source, dim);
        canvas.addBorder(border);
        canvas.setCharProvider(charProvider);
        canvas.setColorProvider(new SingleColorProvider(foregroundColor));
        canvas.setBackground(backgroundColor);
        //canvas.printToConsole();

        BufferedImage image = canvas.generateASCII(pixelProvider);
        Util.writeImage(image, outFile, outputExtension);
    }

    private Color parseColor(String colorString) throws Exception {
        try {
            Field field = Class.forName("java.awt.Color").getField(colorString);
            return (Color) field.get(null);
        } catch (Exception e) {
            Logger.warning("Color " + colorString + " not found.");
            throw e;
        }
    }

    private void dim(){
        BufferedImage source = Util.readImage(imageFile);
        dim = Canvas.getRecommendedDimension(source);
        System.out.println("dx: " + dim.getWidth() + "\tdy: " + dim.getHeight());
    }

    private void color(){
        for (WebColor c: WebColor.values()){
            System.out.println(c);
        }
    }


}


