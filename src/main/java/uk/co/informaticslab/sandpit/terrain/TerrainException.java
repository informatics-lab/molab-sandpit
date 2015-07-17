package uk.co.informaticslab.sandpit.terrain;

import uk.co.informaticslab.sandpit.exceptions.TokenizedRuntimeException;

/**
 * Created by tom on 16/07/2015.
 */
public class TerrainException extends TokenizedRuntimeException {
    /**
     * Constructor
     *
     * @param format  A tokenised message format (e.g. "There are {} failures.")
     * @param varargs The arguments to use to replace the tokens ("{}") one per token
     */
    public TerrainException(String format, Object... varargs) {
        super(format, varargs);
    }
}
