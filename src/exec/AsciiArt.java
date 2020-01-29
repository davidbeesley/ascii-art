package exec;

import art.Canvas;
import logger.Logger;
import picocli.CommandLine;
import picocli.CommandLine.*;
import provider.CharSetProvider;
import provider.character.ICharProvider;
import provider.character.WhitespaceRanked;
import provider.color.ColorProvider;
import provider.pixel.PixelProvider;
import util.Dimension;
import util.Util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Set;
import java.util.concurrent.Callable;

@Command(description = "Ascii Art generator", name = "AsciiArt", mixinStandardHelpOptions = true, version = "AsciiArt 1.0")
public class AsciiArt implements Callable<Integer> {


    @Option(names = {"-i", "--image"}, required = true, description = "source image", paramLabel = "<FILE>")
    File imageFile;

    @Option(names = {"-dx", "--dimensionX"}, description = "desired output dimension x") int dx = -1;
    @Option(names = {"-dy", "--dimensionY"}, description = "desired output dimension y") int dy = -1;






    @Override
    public Integer call() {
        Dimension dim = null;
        if (dx != -1 && dy != -1){
            dim = new Dimension(dx, dy);
        } else if (dx != -1 || dy != -1){
            Logger.warning("Both -dx and -dy must be specified to supply an output dimension");
            return 0;
        }

        process(imageFile, Color.DARK_GRAY, false);
        return 0; // exit code
    }


    public static void main(String... args) { // bootstrap the application
        System.exit(new CommandLine(new AsciiArt()).execute(args));
    }

    public void process(File imageFile, Color background, Dimension dim, boolean invert){
        BufferedImage source = Util.readImage(imageFile);
        if (dim == null){
            dim = art.Canvas.getRecommendedDimension(source);
        }
        Logger.info(dim.getHeight()+ " " + dim.getWidth());
        art.Canvas canvas = new Canvas(source, 200,300);
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
        Util.writeImage(image,  Util.stripExtension(imageFile.getName()));
    }

}
