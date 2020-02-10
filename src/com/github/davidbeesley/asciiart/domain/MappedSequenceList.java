package com.github.davidbeesley.asciiart.domain;

import com.github.davidbeesley.asciiart.util.Dimension;
import com.github.davidbeesley.asciiart.util.logger.Logger;

import java.util.ArrayList;
import java.util.LinkedList;

public class MappedSequenceList {
    private ArrayList<Sequence> list;
    private BooleanMatrix mat;


    public MappedSequenceList(BooleanMatrix matrix){
        this.mat = matrix;
        list = new ArrayList<>();
    }

    public void map(){
        Dimension dim = mat.getDim();
        for (int y = 0; y < dim.getHeight(); y++){
            int currentStreak = 0;
            for (int x = 0; x < dim.getWidth(); x ++) {
                if (mat.getPoint(x, y)) {
                    currentStreak += 1;
                } else {
                    if (currentStreak >= 3) {
                        list.add(new Sequence(x-currentStreak-1, y, currentStreak));
                    }
                    currentStreak = 0;
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();

        for (Sequence q : list){
            s.append(" x " + q.getX() + "\ty " + q.getY() + "\t");
            for(int i = 0; i < q.getSize(); i++){
                s.append('X');
            }
            s.append('\n');
        }
        return s.toString();
    }

    public boolean sendToNext(int index){
        if (index == list.size() - 1){
            Logger.getInstance().warning("index should not be last sequence");
            return false;
        }
        if (list.get(index).numWords() == 0){
            Logger.getInstance().trace("List already empty");
            return false;
        }
        String s = list.get(index).popBack();
        list.get(index + 1).pushFront(s);
        return true;
    }

    public boolean sendToPrevious(int index){
        if (index == 0){
            Logger.getInstance().warning("index should not be first sequence");
            return false;
        }
        if (list.get(index).numWords() == 0){
            Logger.getInstance().trace("List already empty");
            return false;
        }
        String s = list.get(index).popFront();
        list.get(index-1).pushBack(s);
        return true;
    }

    public boolean defaultLoadTokens(Tokens tokens){
        for (Sequence s: list){
            while (s.getCapacity() > tokens.peekSize()){
                s.pushBack(tokens.next());
                if (tokens.finished()){
                    return true;
                }
            }
        }
        return false;
    }

    public ArrayList<Sequence> getList() {
        return list;
    }
}
