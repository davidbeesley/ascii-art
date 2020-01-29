package provider.color;

import java.awt.*;

public class ColorProvider implements IColorProvider {

    public Color getColor(Color color, int height, int width){
        return color;
    }

    public Color getBackground(Color color, Color currentBackground, int height, int width){
        return currentBackground;
    }
}
