package com.flyhub.ideaMS.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author Mawanda Joel
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class GenericServiceException extends Exception {

    private final int exceptionCode;
    private final String exceptionMessage;

    public GenericServiceException(int responseCode, String responseMessage) {
        super(responseMessage);
        this.exceptionCode = responseCode;
        this.exceptionMessage = responseMessage;
    }

}
