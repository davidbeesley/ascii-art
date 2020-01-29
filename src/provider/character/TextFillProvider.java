package provider.character;

import art.Canvas;
import logger.Logger;

import java.awt.*;
import java.util.Set;
import java.util.TreeSet;

public class TextFillProvider implements  ICharProvider{

    private int currentIndex = 0;
    private String text;
    private int pixelWidth, pixelHeight;


    public TextFillProvider(TextManager textManager, Canvas canvas){

        currentIndex = 0;
        pixelHeight = canvas.getPixelHeight();
        pixelWidth = canvas.getPixelWidth();

        textManager.fitToLength(pixelHeight * pixelWidth);

        text = textManager.toString();


    }


    @Override
    public char getChar(Color color, int height, int width) {
        int index = height * pixelWidth + width;
        if (index >= text.length()){
            Logger.error("Logic error");
            System.exit(1);
        }
        return text.charAt(index);
    }




    @Override
    public void invert(boolean bool) {

    }

    @Override
    public Set<Character> getCharSet() {
        Set<Character> result = new TreeSet<>();
        for (char c : text.toCharArray()){
            result.add(c);
        }
        return result;
    }
}
