package com.github.davidbeesley.asciiart.domain;

import com.github.davidbeesley.asciiart.util.logger.Logger;

import java.util.ArrayList;

public class Tokens {
    ArrayList<String> words;
    int count;
    int current;
    public Tokens(ArrayList<String> words){
        this.words = words;
        this.count = countChars();
    }

    public int getCharacterCount(){
        return count;
    }

    private int countChars(){
        int c= 0;
        for (String s: words){
            c += s.length() + 1; // space?
        }
        Logger.getInstance().debug("Total chars: " + c);
        return c;
    }

    public void reset(){
        current = 0;
    }
    public boolean finished(){
        return current == words.size();
    }
    public String next(){
        String s = words.get(current);
        current += 1;
        return  s;
    }

    public int peekSize(){
        return words.get(current).length();
    }
}
