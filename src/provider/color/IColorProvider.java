package provider.color;

import java.awt.*;

public interface IColorProvider {

    public Color getColor(Color color, int height, int width);

    public Color getBackground(Color color, Color current, int height, int width);


}
