package com.flyhub.ideaMS.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;
import com.flyhub.ideaMS.dao.suggestion.Suggestion;
import com.flyhub.ideaMS.models.views.View;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

/**
 *
 * @author Mawanda Joel
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

    public DataObjectResponse(int totalElements, int operationResult, String operationDescription, Object data) {
        super(totalElements, operationResult, operationDescription);
        this.data = data;
    }

    public DataObjectResponse(int totalElements, int noOfElementsOnPage, int operationResult, String operationDescription, Object data) {
        super(totalElements, noOfElementsOnPage, operationResult, operationDescription);
        this.data = data;
    }
}
