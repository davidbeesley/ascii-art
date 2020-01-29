package provider.character;

import org.junit.Test;

import static org.junit.Assert.*;

public class TextManagerTest {

    @Test
    public void getLength() {
        TextManager tm = new TextManager();
        tm.addWord("a");
        tm.addWord("b");
        assert tm.getLength() == 3;
    }

    @Test
    public void addWord() {
    }

    @Test
    public void peek() {
    }

    @Test
    public void removeWord() {
    }

    @Test
    public void fitToLength() {
        TextManager tm = new TextManager();
        tm.addWord("a");
        tm.addWord("b");
        tm.fitToLength(4);
        System.out.println(tm.toString());
    }

    @Test
    public void toStringTest() {
    }

    @Test
    public void getRecommendedDimension() {
    }

    @Test
    public void getCharSet() {
    }

    @Test
    public void notEmpty() {
    }
}