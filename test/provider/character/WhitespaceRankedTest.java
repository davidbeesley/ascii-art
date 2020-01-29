package provider.character;

import logger.Logger;
import org.junit.Test;
import provider.font.FontProvider;

public class WhitespaceRankedTest {

    @Test
    public void construct(){
        WhitespaceRanked whitespaceRanked = new WhitespaceRanked(FontProvider.getMono());
        Logger.trace(whitespaceRanked.toString());
    }

}