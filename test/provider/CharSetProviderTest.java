package provider;

import org.junit.Test;

import static org.junit.Assert.*;

public class CharSetProviderTest {

    @Test
    public void print(){
        CharSetProvider.printSet(CharSetProvider.getFullSet());
    }
}