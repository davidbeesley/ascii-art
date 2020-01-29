package provider.character;

import provider.CharSetProvider;

import java.awt.*;
import java.util.Set;

public interface ICharProvider {

    public char getChar(Color color, int height, int width);

    public void invert(boolean bool);

    public Set<Character> getCharSet();
}
