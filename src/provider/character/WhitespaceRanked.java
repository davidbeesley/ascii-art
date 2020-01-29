package provider.character;

import art.Pixel;
import provider.CharSetProvider;
import provider.pixel.PixelProvider;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class WhitespaceRanked extends CharProvider {

    private ArrayList<Character> rankedChars;
    private boolean invert;
    private Set<Character> charSet;
    private Font font;

    public WhitespaceRanked(Font font){
        this.invert = false;
        this.font = font;
        this.charSet= CharSetProvider.getFullSet();
        init(font, false, CharSetProvider.getFullSet());

    }

    public WhitespaceRanked(Font font, boolean invert){
        this.invert = invert;
        this.charSet= CharSetProvider.getFullSet();
        this.font = font;

        init(font, invert, CharSetProvider.getFullSet());
    }

    public WhitespaceRanked(Font font, Set<Character> charSet){
        this.charSet = charSet;
        this.invert = false;
        this.font = font;

        init(font, false, charSet);

    }

    public WhitespaceRanked(Font font, Set<Character> charSet, boolean invert){
        init(font, invert, charSet);
    }

    private void init(Font font, boolean invert, Set<Character> charSet){
        int red1 = invert ? 0 : 255;
        int red2 = invert ? 255 : 0;
        PixelProvider provider = new PixelProvider(font);
        Map<Integer, Character> charMap = new TreeMap<>();
        for (char c : charSet){
            Pixel pixel = provider.getPixel(c, new Color(red1,0,0), new Color(red2,0,0));

            charMap.put(getUsed(pixel), c);

        }
        //System.out.println(charMap);
        rankedChars = new ArrayList<>(charMap.values());
    }


    @Override
    public char getChar(Color color, int height, int width){
        double score = color.getRed() + color.getBlue() + color.getGreen();
        double maxScore = 255 * 3;
        double arraySize = rankedChars.size();
        double scaled = score / maxScore * arraySize;
        int index = (int) Math.round(scaled);
        if (index < 0 ) index = 0;
        if (index >= rankedChars.size()) index = rankedChars.size() -1;
        return rankedChars.get(index);
    }


    private int getUsed(Pixel pixel){
        int total = 0;
        final int spacer = 1;
        for (int w = 0; w < pixel.getWidth(); w += spacer){
            for (int h = 0; h < pixel.getHeight(); h += spacer){
                Color color = pixel.getColor(w,h);
                total += color.getRed();
            }
        }

        return total;
    }

    @Override
    public void invert(boolean bool) {
        init(font, bool, charSet);
    }

    @Override
    public String toString() {
        String output = "Length:" + rankedChars.size() + " ";
        for (Character c : rankedChars){
            output += c;
        }
        return output;
    }
}
