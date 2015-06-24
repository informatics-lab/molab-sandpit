package uk.co.informaticslab.sandpit.io;

import uk.co.informaticslab.sandpit.exceptions.TokenizedRuntimeException;

/**
 * Created by tom on 24/06/2015.
 */
public class Camera3DException extends TokenizedRuntimeException {

    /**
     * Constructor
     *
     * @param format  A tokenised message format (e.g. "There are {} failures.")
     * @param varargs The arguments to use to replace the tokens ("{}") one per token
     */
    public Camera3DException(String format, Object... varargs) {
        super(format, varargs);
    }
}
