package com.flyhub.ideaMS.exception;

/**
 *
 * @author Joel Mawanda
 */
public class DuplicateDataException extends GenericServiceException {

    public DuplicateDataException(int responseCode, String responseMessage) {
        super(responseCode, responseMessage);
    }

}
