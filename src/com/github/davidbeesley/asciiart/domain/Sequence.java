package com.github.davidbeesley.asciiart.domain;

import com.github.davidbeesley.asciiart.util.logger.Logger;

import java.util.LinkedList;

public class Sequence{
    private int x, y, size;
    private char[] data;
    private LinkedList<String> assignedWords;

    public Sequence(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
        data = new char[size];
        for (int i = 0; i < size; i++){
            data[i] = ' '; // todo
        }
        assignedWords = new LinkedList<>();

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSize() {
        return size;
    }

    public int getCapacity(){
        return size - getUsed();
    }

    public boolean valid(){
        return getCapacity() >= 0;
    }
    public void pushFront(String word){
        assignedWords.addFirst(word);
    }
    public void pushBack(String word){
        assignedWords.addLast(word);
    }
    public String popFront(){
        return assignedWords.removeFirst();
    }
    public String popBack(){
        return assignedWords.removeLast();
    }

    public double getDensity(){
        return getUsed() * 1.0 / size;
    }

    public double getSimulatedDensity(int additional){
        return (getUsed() * 1.0 + additional) / size;
    }
    private int getUsed(){
        if (assignedWords.size() == 0){
            return 0;
        } else {
            int total = assignedWords.size() - 1; //No space required after last word.
            for (String s : assignedWords){
                total += s.length();
            }
            return total;
        }
    }
    private void place(String s, int index){
        for (int i = 0; i < s.length(); i++){
            data[index + i] = s.charAt(i);
        }
    }

    public int numWords(){
        return assignedWords.size();
    }
    public String build(){
        if (getCapacity() < 0){
            Logger.getInstance().error("Impossible sequence");
            System.exit(1);
        }
        if (assignedWords.size() == 0){
            Logger.getInstance().trace("Empty sequence :(");
            return new String(data);
        }
        place(assignedWords.get(0), 0);
        if (assignedWords.size() == 1){
            Logger.getInstance().trace("Single word sequence");
            return new String(data);
        }
        place(assignedWords.getLast(), size - assignedWords.getLast().length());
        if (assignedWords.size() == 2){
            return new String(data);
        }

        int extraSpaces = getCapacity();
        int modifiableWords = assignedWords.size() - 1;

        int spacesPerWords = extraSpaces / modifiableWords;
        int remainder = extraSpaces % modifiableWords > 0 ? 1 : 0;
        spacesPerWords += remainder;

        int currentIndex = assignedWords.get(0).length() + 1 + spacesPerWords;
        extraSpaces -= spacesPerWords;
        for (int i = 1; i < assignedWords.size() - 1; i++){
            place(assignedWords.get(i), currentIndex);
            currentIndex += assignedWords.get(i).length() + 1;
            if (extraSpaces >= spacesPerWords){
                currentIndex += spacesPerWords;
                extraSpaces -= spacesPerWords;
            }
        }
        return new String(data);

    }
}