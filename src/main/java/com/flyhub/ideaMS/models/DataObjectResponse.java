package com.flyhub.ideaMS.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.flyhub.ideaMS.models.views.View;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 *
 * @author Benjamin E Ndugga
 */
//@JsonView(View.MerchantView.class)
@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
public class DataObjectResponse extends OperationResponse {

    @JsonProperty("data")
    private Object data;

    public DataObjectResponse(int operationResult, String operationDescription) {
        super(operationResult, operationDescription);
    }

    public DataObjectResponse(int operationResult, String operationDescription, Object data) {
        super(operationResult, operationDescription);
        this.data = data;
    }

}
