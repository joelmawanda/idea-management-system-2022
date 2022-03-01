package com.flyhub.ideaMS.exception;

/**
 *
 * @author Benjamin E Ndugga
 */
public class RecordNotFoundException extends GenericServiceException {

    public RecordNotFoundException(int responseCode, String responseMessage) {
        super(responseCode, responseMessage);
    }

}
