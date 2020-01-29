package provider.character;

import java.awt.*;
import java.util.Set;
import java.util.TreeSet;

public class BooleanProvider implements ICharProvider{


    Boolean[][] map;
    char c;
    char empty = ' ';
    Set<Character> charSet;
    public BooleanProvider(Boolean[][] map, char c){
        this.map = map;
        this.c = c;
        charSet = new TreeSet<>();
        charSet.add(c);
        charSet.add(empty);
    }

    public void replaceEmpty(char c){
        empty = c;
        charSet = new TreeSet<>();
        charSet.add(c);
        charSet.add(empty);
    }

    @Override
    public char getChar(Color color, int height, int width) {
        if (map[height][width]) return c;
        return empty;
    }

    @Override
    public void invert(boolean bool) {
        if (bool) {
            char temp = c;
            c = empty;
            empty = temp;
        }
    }

    @Override
    public Set<Character> getCharSet() {
        return charSet;
    }
}
