package com.flyhub.ideaMS.dao.entityauthority;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.io.Serializable;

/**
 *
 * @author Mawanda Joel
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonPropertyOrder(alphabetic = true)
public class EntityAuthorisationMappingDto implements Serializable {

    private Boolean searchAllowed;

    private Boolean createAllowed;

    private Boolean readAllowed;

    private Boolean updateAllowed;

    private Boolean deleteAllowed;

    private Boolean printAllowed;

    private Boolean accessMode;

}
