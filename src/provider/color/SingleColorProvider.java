package provider.color;

import java.awt.*;

public class SingleColorProvider extends ColorProvider {

    private Color c;
    public SingleColorProvider(Color c){
        this.c = c;
    }

    @Override
    public Color getColor(Color color, int height, int width) {
        return c;
    }
}
