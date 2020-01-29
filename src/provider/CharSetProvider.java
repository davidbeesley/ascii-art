package provider;

import java.util.Set;
import java.util.TreeSet;

public class CharSetProvider {

    public static Set<Character> getAlphaNumeric(){
        Set<Character> result = new TreeSet<>();

        for (char c = 'a'; c <= 'z'; c++){
            result.add(c);
        }
        for (char c = 'A'; c <= 'Z'; c++){
            result.add(c);
        }
        for (char c = '0'; c <= '9'; c++){
            result.add(c);
        }
        return result;
    }

    public static Set<Character> getFullSet(){
        Set<Character> result = new TreeSet<>();

        for (char c = ' '; c <= '~'; c++){
            result.add(c);
        }

        return result;
    }

    public static Set<Character> getSimpleSet(){
        Set<Character> result = new TreeSet<>();
        String chars = " \':!?xSK8@";
        for (char c : chars.toCharArray()){
            result.add(c);
        }
        return result;
    }

    public static Set<Character> getLargeSet(){
        Set<Character> result = new TreeSet<>();
        String chars = "     #24XZPe3kaHAE5wKhSUD6mG9ObRdp&q8%NBWgQ$@";
        for (char c : chars.toCharArray()){
            result.add(c);
        }
        return result;

    }

    public static void printSet(Set<Character> charSet){
        for (char c: charSet){
            System.out.print(c);
        }
        System.out.println();
    }


}
