package com.github.davidbeesley.asciiart.domain;

import com.github.davidbeesley.asciiart.util.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class TextSource {

    ArrayList<String> words;

    public TextSource(File inputFile) {

        int max = 0;
        words = new ArrayList<>();
        try {
            if (inputFile == null){
                throw new IOException("Invalid text file provided");
            }
            Scanner scanner = new Scanner(inputFile);
            while (scanner.hasNext()){
                String word = scanner.next();
                words.add(word);
                if (word.length() > max){
                    Logger.getInstance().trace(word + " : " + word.length());
                    max = word.length();
                }
            }
        }
        catch (IOException e){
            Logger.getInstance().warning("Invalid filename: "+ inputFile.getName());
            Logger.getInstance().error(e.getMessage());
            System.exit(1);
        }
    }

    public ArrayList<String> getWords(){
        return words;
    }
}
