package provider.character;

import loggerOLD.Logger;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

import art.Canvas;
import provider.font.FontProvider;

public class TextProvider extends CharProvider  {

    String text;
    int currentIndex;
    Set<Character> charSet;
    Canvas canvas;
    Map<Integer, Queue<Boolean>> useColorMap;
    boolean isCalibrated = false;
    boolean invert = false;
    CharProvider backup = new CharProvider();

    public TextProvider(String filename, Canvas canvas){

        currentIndex = 0;
        this.canvas = canvas;
        text = readText(filename);
        charSet = new TreeSet<>();
        for (char c : text.toCharArray()){
            charSet.add(c);
        }
        Set<Character> backupSet = new TreeSet<>();
        backupSet.add(' ');
        backup = new WhitespaceRanked(FontProvider.getMono(), backupSet);
        calibrate();
    }


    public Set<Character> getCharSet() {
        return charSet;
    }

    private void calibrate(){
        calibrate(false);
    }

    private void calibrate(boolean invert){
        this.invert = invert;
        useColorMap = new TreeMap<>();
        if (canvas.getPixelWidth() * canvas.getPixelHeight() < text.length()){
            Logger.error("Image is too small for text");
            System.exit(1);
        }
        Logger.info("Ratio is " + text.length() +":" + canvas.getPixelWidth() * canvas.getPixelHeight());

        Color[][] colorMap = canvas.getColorMap();
        Map<Integer, Integer> colorCountMap = new TreeMap<>();
        for (int h = 0; h < canvas.getPixelHeight(); h++){
            for (int w = 0; w < canvas.getPixelWidth(); w++){
                Color color = colorMap[h][w];
                int score = color.getRed() + color.getBlue() + color.getGreen();
                if (invert == false) score *= -1;
                Integer count = colorCountMap.get(score);
                if (count == null) count = 0;
                count ++;

                colorCountMap.put(score, count);
            }
        }


        for (int i = 0; i < text.length(); i++){

            int key = colorCountMap.keySet().iterator().next();
            int count = colorCountMap.get(key);
            if (count== 0) {
                Logger.error("Logic Error");
                System.exit(1);
            }

            if (useColorMap.get(key) == null) useColorMap.put(key, new LinkedList<>());
            Queue<Boolean> queue = useColorMap.get(key);
            queue.add(true);
            count--;
            if (count == 0){
                colorCountMap.remove(key);
            } else {
                colorCountMap.put(key, count);
            }
        }

        for (Integer key : colorCountMap.keySet()){
            for (int i = 0; i < colorCountMap.get(key); i++){
                if (useColorMap.get(key) == null) useColorMap.put(key, new LinkedList<>());
                Queue<Boolean> queue = useColorMap.get(key);
                queue.add(false);
            }
        }


        isCalibrated = true;





    }

    @Override
    public char getChar(Color color, int height, int width){
        if (isCalibrated == false){
            Logger.error("Please calibrate before use.");
            System.exit(1);
        }
        int key = color.getRed() + color.getGreen() + color.getBlue();
        if (invert == false) key *= -1;

        boolean useChar = useColorMap.get(key).remove();
        if (useChar){
            char c = text.charAt(currentIndex);
            currentIndex++;
            return c;
        } else {

            return backup.getChar(color, height, width);
        }
    }

    public void setBackup(CharProvider backup) {
        this.backup = backup;
    }




    private static String readText(String filename){
        StringBuilder builder = new StringBuilder();
        File file = new File(filename);
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()){
                builder.append(scanner.nextLine());
                builder.append(" ");
            }
        }
        catch (IOException e){
            Logger.warning("Invalid filename: "+ filename);
            Logger.trace(e.getMessage());
            System.exit(1);
        }
        return builder.toString();
    }

    @Override
    public void invert(boolean bool){
        calibrate(invert);
    }


    /*
    @Override
    public Color getColor(Color color, int height, int width){
        return Color.BLACK;
    }

    @Override
    public Color getBackground(Color color, Color current, int height, int width){
        return current;
    }
    */
}
