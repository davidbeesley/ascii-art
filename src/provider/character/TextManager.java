package provider.character;

import loggerOLD.Logger;
import com.github.davidbeesley.asciiart.util.Dimension;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class TextManager {

    private ArrayList<String> words;
    private int length = 0;
    private int index = 0;

    public TextManager(File file){
            words = new ArrayList<>();
            try {
                if (file == null){
                    throw new IOException("Invalid text file provided");
                }
                Scanner scanner = new Scanner(file);
                while (scanner.hasNext()){
                    String word = scanner.next();
                    word += " ";
                    words.add(word);
                }
            }
            catch (IOException e){
                //Logger.warning("Invalid filename: "+ file.getName());
                Logger.error(e.getMessage());
                System.exit(1);
            }

        for (String word : words){
            length += word.length();
        }
    }


    public TextManager(){
        words = new ArrayList<>();
    }


    public int getLength(){

        return length; // Accounting for a space between each word.
    }

    /**
     *
     * @param word
     * @return New length of all text.
     */
    public int addWord(String word){
        words.add(word);
        length += word.length();

        return getLength();
    }

    public String peek(){
        if (words.size() == 0){
            Logger.error("Logic error");
            System.exit(1);
        }
        String result = words.get(0);
        return result;
    }

    public String removeWord(){
        if (words.size() == 0){
            Logger.error("Logic error");
            System.exit(1);
        }
        String result = words.get(0);
        words.remove(0);
        length -= result.length();
        if (result.charAt(result.length()-1) != ' ') {
            Logger.error("Logic error... word must end with space");
            System.exit(1);
        }
        return result;
    }

    public void fitToLength(int newLength){
        if (words.size() == 0){
            words.add(new String(new char[newLength]).replace('\0', ' '));
            length = newLength;
            return;
        }
        int mustAdd = newLength - getLength();

        if (mustAdd < 0) {
            Logger.error("Attempted increase to a shorter length." + newLength + " < " + getLength());
            System.exit(1);
        }


        if (words.size() == 1){
            while(mustAdd > 1) {

                String newWord =" " + words.get(index) + " ";
                words.set(index, newWord);
                mustAdd -= 2;

            }
            if (mustAdd > 0){
                String newWord = words.get(index) + " ";
                words.set(index, newWord);
                length = newLength;
                return;
            }
        }

        boolean permitMultiples = false;
        if (mustAdd >= words.size()){
            //Logger.info("Permitting multiple spaces");
            permitMultiples = true;
        }
        Random rand = new Random();
        Set<Integer> used = new TreeSet<>();
        while(mustAdd > 0) {
           int index = rand.nextInt(words.size() -1);
           if (permitMultiples == false && used.contains(index)) continue;

           used.add(index);
           mustAdd--;
           String newWord = words.get(index) + " ";
           words.set(index, newWord);

        }

        length = newLength; // Account for adding spaces later.
    }

    @Override
    public String toString() {
        if (words.size() == 1){
            return words.get(0);
        }
        StringBuilder builder = new StringBuilder();
        boolean isFirst = true;
        for (String word : words){
            builder.append(word);
        }
        String result = builder.toString();
        if (result.length() != length ){
            Logger.error("Logic error. Length was incorrect. " + result.length() + " expected was: " + (length));
            System.exit(1);
        }
        return result;
    }


    public Dimension getRecommendedDimension(BufferedImage img, double heightToWidth){
        double height = img.getHeight();
        double width = img.getWidth();
        double ratio = width / height * heightToWidth;
        Logger.trace("Ratio: " + ratio);

        int chars = getLength();

        int pixelHeight = 1;
        int pixelWidth = (int) (pixelHeight * ratio);

        while(pixelHeight * pixelWidth < chars){
            pixelHeight++;
            pixelWidth =(int) (pixelHeight * ratio);
        }

        return new Dimension(pixelHeight, pixelWidth);


    }


    public Set<Character> getCharSet(){
        Set<Character> charSet = new TreeSet<>();
        for (char c : toString().toCharArray()){
            charSet.add(c);
        }
        return charSet;
    }

    public boolean notEmpty(){
        return words.size() != 0;
    }
}
