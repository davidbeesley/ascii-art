package com.github.davidbeesley.asciiart.domain;

import com.github.davidbeesley.asciiart.util.Dimension;
import com.github.davidbeesley.asciiart.util.logger.Logger;

import java.util.ArrayList;
import java.util.LinkedList;

public class MappedSequenceList {
    private ArrayList<Sequence> list;
    private BooleanMatrix mat;
    private double targetDensity;


    public MappedSequenceList(BooleanMatrix matrix, double targetDensity){
        this.mat = matrix;
        list = new ArrayList<>();
        this.targetDensity = targetDensity;
    }

    public void map(){
        int total_allocated = 0;
        Dimension dim = mat.getDim();
        for (int y = 0; y < dim.getHeight(); y++){
            int currentStreak = 0;
            for (int x = 0; x < dim.getWidth(); x ++) {
                if (mat.getPoint(x, y)) {
                    currentStreak += 1;
                } else {
                    if (currentStreak >= 3 ) {
                        list.add(new Sequence(x-currentStreak, y, currentStreak));
                        total_allocated  += currentStreak;
                    }
                    currentStreak = 0;
                }
            }
            if (currentStreak >= 3){
                list.add(new Sequence(dim.getWidth()-currentStreak, y, currentStreak));
                total_allocated  += currentStreak;
            }

        }
        Logger.getInstance().debug("Total Allocated: " + total_allocated);
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

    public void defaultLoadTokens(Tokens tokens){
        tokens.reset();
        for (Sequence s: list){
            while (s.getSimulatedDensity(tokens.peekSize()) < targetDensity+.01){
                s.pushBack(tokens.next());
                if (tokens.finished()){
                    return;
                }
            }
        }
        while(tokens.finished() == false){
            list.get(list.size()-1).pushBack(tokens.next());
        }
    }

    public boolean pushForward(){
        for(int i = 0; i < list.size()-1; i++){
            while(list.get(i).getCapacity() < 0){
                Logger.getInstance().trace("Sent forward");
                sendToNext(i);
            }
        }
        return list.get(list.size()-1).getCapacity() > 0;
    }

    public boolean pushBackward(){
        for(int i = list.size() - 1; i > 0; i--){
            while(list.get(i).getCapacity() < 0){
                Logger.getInstance().trace("Sent backward");
                sendToPrevious(i);
            }
        }
        return list.get(0).getCapacity() > 0;
    }

    public ArrayList<Sequence> getList() {
        return list;
    }

    public int getTotalCapacity(){
        int count = 0;
        for (Sequence s : list){
            count += s.getCapacity() + 1;
        }
        return count;
    }
}
