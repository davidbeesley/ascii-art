package provider.character;

import provider.CharSetProvider;

import java.awt.*;
import java.util.Set;

public class CharProvider implements  ICharProvider{



    public char getChar(Color color, int height, int width){
        return 'a';
    }

    public void invert(boolean bool){

    }

    public Set<Character> getCharSet(){
        return CharSetProvider.getFullSet();
    }


}
