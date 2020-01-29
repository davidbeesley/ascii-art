package art;

import java.awt.*;

public class Point {
    Color color, backgroundColor;
    char text;

    public Point(Color color, Color backgroundColor, char text) {
        this.color = color;
        this.text = text;
        this.backgroundColor = backgroundColor;
    }

    public Color getColor() {
        return color;
    }

    public char getText() {
        return text;
    }

    public Color getBackgroundColor(){
        return backgroundColor;
    }


}
