package com.github.davidbeesley.asciiart.domain;

import com.github.davidbeesley.asciiart.util.Dimension;

public class AsciiMatrix {
    private char[][] data;
    private Dimension dim;
    public AsciiMatrix(Dimension dim){
        this.dim = dim;
        data = new char[dim.getWidth()][dim.getHeight()];
        for (int x = 0; x < dim.getWidth(); x++){
            for (int y = 0; y < dim.getHeight(); y++){
                data[x][y] = ' ';
            }
        }
    }

    public void setPoint(int x, int y, char point){
        data[x][y] = point;
    }

    public char getPoint(int x, int y){
        return data[x][y];
    }

    public void setString(int x, int y, String s){
        for (int i = 0; i < s.length(); i++){
            setPoint(x+i, y, s.charAt(i));
        }
    }

    public Dimension getDim() {
        return dim;
    }
}
