package com.github.davidbeesley.asciiart.task;

import com.github.davidbeesley.asciiart.domain.MappedSequenceList;
import com.github.davidbeesley.asciiart.domain.Tokens;
import com.github.davidbeesley.asciiart.util.logger.Logger;

public class TextMapper {

    public static boolean mapText(MappedSequenceList list, Tokens tokens){
        if (list.defaultLoadTokens(tokens) == false){
            return false; // Couldn't map at all.
        }
        // todo
        Logger.getInstance().info("Mapping succeeded");
        return true;
    }
}
