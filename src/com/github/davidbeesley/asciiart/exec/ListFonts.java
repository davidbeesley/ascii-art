package com.github.davidbeesley.asciiart.exec;

import java.awt.*;
import java.util.Set;
import java.util.TreeSet;

public class ListFonts {


    public static void printFonts(){
        String fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

        for ( int i = 0; i < fonts.length; i++ )
        {
            System.out.println(fonts[i]);
        }
    }



}
