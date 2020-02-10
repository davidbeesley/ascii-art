package com.github.davidbeesley.asciiart.domain;

import com.github.davidbeesley.asciiart.util.ColorVectorUtil;
import com.github.davidbeesley.asciiart.util.Dimension;

public class BooleanMatrix {
    private boolean[][] data;
    private Dimension dim;
    public BooleanMatrix(Dimension dim){
        this.dim = dim;
        data = new boolean[dim.getWidth()][dim.getHeight()];
    }

    public void setPoint(int x, int y, boolean point){
        data[x][y] = point;
    }

    public boolean getPoint(int x, int y){
        return data[x][y];
    }

    public double getDensity(){
        double sum = 0;
        int total = dim.getHeight() * dim.getWidth();
        for (int x = 0; x < dim.getWidth(); x ++){
            for (int y = 0; y < dim.getHeight(); y++){
                if (getPoint(x,y)){
                    sum += 1;
                }
            }
        }
        return sum / total;
    }

    public Dimension getDim() {
        return dim;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int y = 0; y < dim.getHeight(); y++){
           for (int x = 0; x < dim.getWidth(); x ++){
                if (getPoint(x,y)){
                    s.append('X');
                } else {
                    s.append(' ');
                }
            }
            s.append('\n');
        }
        return s.toString();
    }

    public String toNoSpaceRepresentation() {
        StringBuilder s = new StringBuilder();
        for (int y = 0; y < dim.getHeight(); y++){
            for (int x = 0; x < dim.getWidth(); x ++){
                if (getPoint(x,y)){
                    s.append('X');
                } else {
                    s.append('_');
                }
            }
            s.append('\n');
        }
        return s.toString();
    }
}
