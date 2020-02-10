package provider.character;

import art.BooleanCanvas;
import provider.pixel.BooleanPixelProvider;
import com.github.davidbeesley.asciiart.util.Dimension;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class TextShapeProvider implements  ICharProvider {

    private TextManager textManager;
    private Boolean[][] charMap;
    private static final double START_DENSITY = .96;
    private double density;
    private Set<Character> characterSet;
    private Queue<Character> nextChars;

    public TextShapeProvider(Boolean[][] map, TextManager textManager){
        this.textManager = textManager;
        characterSet = textManager.getCharSet();
        nextChars = new LinkedList<>();
        density = START_DENSITY;
        ////Logger.getInstance().message("Beginning mapping. Please be patient.");
        while (attemptToMap(map, density) == false){
            if (density < .5){
                //Logger.getInstance().error("Could not map.");
                System.exit(1);
            }
            else {
                //Logger.getInstance().info("Attempt with density " + density + " failed. Trying again");
                density -= .01;
            }

        }
        charMap = map;
        //BooleanCanvas.printMap(map);




    }

    public static Dimension getRecommendedDimension(BufferedImage img, TextManager textManager, BooleanPixelProvider booleanPixelProvider, double heightToWidth){
        double height = img.getHeight();
        double width = img.getWidth();
        double ratio = width / height * heightToWidth;
        //Logger.trace("Ratio: " + ratio);

        int chars = textManager.getLength();

        int pixelHeight = 10;
        int pixelWidth = (int) (pixelHeight * ratio);
        int count = getBooleanCount(img, booleanPixelProvider, pixelHeight, pixelWidth);
        if (count == 0) {
            //Logger.error("getBooleanCount failed");
            System.exit(1);
        }
        while( count * START_DENSITY < chars){
            ////Logger.trace("ph: " + pixelHeight + " pw: " + pixelWidth + " count:" + count + " count*density: " + (count * density) + " totalChars: " + chars);
            if (pixelHeight > 120) {
                double countPerPixel = count * START_DENSITY / (pixelHeight * pixelWidth);
                //Logger.trace(" " + countPerPixel);
                double totalPixels = chars / countPerPixel;
                //Logger.trace("Total pixels needed:" + totalPixels);
                while(pixelHeight *pixelWidth < totalPixels){
                    pixelHeight++;
                    pixelWidth =(int) (pixelHeight * ratio);

                }


            } else {
                pixelHeight++;
                pixelWidth =(int) (pixelHeight * ratio);

            }
            count = getBooleanCount(img, booleanPixelProvider, pixelHeight, pixelWidth);
        }


        //Logger.trace("Recommended dimension is: " + pixelHeight + "x" + pixelWidth);

        return new Dimension(pixelHeight, pixelWidth);


    }

    public static int getBooleanCount(BufferedImage img, BooleanPixelProvider booleanPixelProvider, int pH, int pW){
        BooleanCanvas bc = new BooleanCanvas(img, pH, pW);
        Boolean[][] map = bc.generateBooleanMap(booleanPixelProvider);

        int count = 0;
        for (Boolean[] row : map){
            for (Boolean b : row){
                count += b ? 1:0;
            }
        }

        return count;



    }




    @Override
    public char getChar(Color color, int height, int width) {
        //return '-';
        if (charMap[height][width]) return nextChars.remove();
        else return ' ';
    }

    @Override
    public void invert(boolean bool) {

    }

    @Override
    public Set<Character> getCharSet() {
        return characterSet;
    }

    public boolean attemptToMap(Boolean[][] map,  double density){
        //Logger.info("Attempting to map");
        TextManager tm = textManager;




        ArrayList<Mapping> mappings = convertToMappings(map);
        ////Logger.trace(mappings.size() + " Mappings found");
        double placed = 0;
        double spaceUsed = 0;
        int totalSpace = 0;
        int totalText = tm.getLength();
        for (Mapping mapping : mappings){
            totalSpace += mapping.getSize();
        }
        for (Mapping mapping : mappings){
            double totalTextLeft = totalText - placed;
            spaceUsed += mapping.getSize();
            double totalSpaceLeft = totalSpace - spaceUsed;
            TextManager thisSpaceManager = new TextManager();
            int remainingSpace = mapping.getSize();
            ////Logger.trace("New mapping: " + placed + " / " + spaceUsed + "=" + (placed/spaceUsed) + ". Remaining space:" + remainingSpace + ". Remaining ratio: " + totalTextLeft/totalSpaceLeft  + " TotalSpaceLeft: " + totalSpaceLeft + " TotalTextLeft " + totalTextLeft + " islast:" +mapping.isLast() + " " + textManager.peek());
            //System.out.printf("ROW: %.3f/%.3f",placed/spaceUsed, totalTextLeft/totalSpaceLeft);

            while ( ((placed / (spaceUsed) < (totalTextLeft/totalSpaceLeft) ) || mapping.isLast()) && textManager.notEmpty() && thisSpaceManager.getLength() < mapping.getSize() -1 ){
                //System.out.printf("%.3f/%.3f",placed/spaceUsed, totalTextLeft/totalSpaceLeft);
                //while (placed / spaceUsed < density && textManager.notEmpty() && remainingSpace > 0 && ((totalTextLeft/totalSpaceLeft > (density -.02) ) || totalTextLeft < totalText * .05 )){
                ////Logger.trace(placed + " / " + spaceUsed + "=" + (placed/spaceUsed) + ". Remaining space:" + remainingSpace + ". Remaining ratio: " + totalTextLeft/totalSpaceLeft  + " TotalSpaceLeft: " + totalSpaceLeft + " TotalTextLeft " + totalTextLeft + " islast:" +mapping.isLast() + " " + textManager.peek());

                //if (totalTextLeft > totalSpaceLeft) return false;

                // Try to add to the TextManager.
                String nextWord = textManager.peek();
                if (thisSpaceManager.getLength() + nextWord.length() - 1> mapping.getSize()) {
                    //System.out.printf(" -> %.3f/%.3f",placed/spaceUsed, totalTextLeft/totalSpaceLeft);
                    break; // Break if next word is too big. Discount last space.
                }

                // We can add a word.
                thisSpaceManager.addWord(textManager.removeWord());
                placed += nextWord.length();
                totalTextLeft = totalText - placed;

                //totalSpaceLeft -= nextWord.length();

                remainingSpace -= nextWord.length();
                //unplacedSpaceInRow -= nextWord.length();
                //System.out.printf(" -> %.3f/%.3f",placed/spaceUsed, totalTextLeft/totalSpaceLeft);

            }
            //System.out.println();
            Boolean bool = (placed / (spaceUsed) < (totalTextLeft/totalSpaceLeft) );
            ////Logger.trace("Ended trying to map current: " + Boolean.toString(bool) + " " + mapping.isLast() + " " + textManager.notEmpty() + " " + (remainingSpace > 0));

            // Space the words properly.
            thisSpaceManager.fitToLength(mapping.getSize()+1);
            String result = thisSpaceManager.toString();
            if (result.length() > 0){
                result = result.substring(0, result.length()-1);
            }
            mapping.setText(result.toCharArray());

            // Add characters to queue.
            for (Character c : mapping.getText()){
                nextChars.add(c);
            }
        }



        if (textManager.notEmpty()) {
            //Logger.debug("MAPPING FAILED with density " + density);
            return false;
        }
        //Logger.info("Mapping succeeded");
        return true;
    }


    public static ArrayList<Mapping> convertToMappings(Boolean[][] map){
        ArrayList<Mapping> mappings = new ArrayList<>();

        for (int h = 0; h < map.length; h++){
            for (int w = 0; w < map[0].length;){

                // Skip blanks
                if (map[h][w] == false) {
                    w += 1;
                    continue;
                }
                // Read True until blank or end of row.
                int size = 0;
                int offset = 0;
                while(offset + w < map[0].length && map[h][w+offset]){
                    size++;
                    offset++;
                }
                if (offset + w < (map[0].length)) {
                    //map[h][w+offset] = true;
                    //size++;
                    //offset++;

                }
                mappings.add(new Mapping(h,w,size)); // Size + 1 because all words end in a space.
                w += offset;

            }
        }
        mappings.get(mappings.size()-1).setLast();
        return mappings;
    }


    private static class Mapping{
        private int w;
        private int h;
        private int size;
        private char[] text;
        private boolean isLast;

        public Mapping(int w, int h, int size) {
            this.w = w;
            this.h = h;
            this.size = size;
            this.isLast = false;
        }

        public void setText(char[] text) {
            this.text = text;
        }

        public int getW() {
            return w;
        }

        public int getH() {
            return h;
        }

        public int getSize() {
            return size;
        }

        public char[] getText() {
            return text;
        }


        public void setLast(){
            isLast = true;
        }
        public boolean isLast(){
            return isLast;
        }
    }
}
