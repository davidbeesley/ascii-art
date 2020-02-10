package com.github.davidbeesley.asciiart.task;

import com.github.davidbeesley.asciiart.domain.MappedSequenceList;
import com.github.davidbeesley.asciiart.domain.Tokens;
import com.github.davidbeesley.asciiart.util.logger.Logger;

public class TextMapper {

    public static boolean mapText(MappedSequenceList list, Tokens tokens){
        list.defaultLoadTokens(tokens);
        if (list.pushForward() == false){
            if (list.pushBackward() == false){
                return false;
            }
        }
        Logger.getInstance().debug("Mapping succeeded");
        return true;
    }
}
