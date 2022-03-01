package com.flyhub.ideaMS.dao.entityauthority;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.HashMap;

/**
 *
 * this class extends the Custom HashMap Type for the Entity Module Access
 * Authorities
 *
 * @author Benjamin E Ndugga
 */
@JsonSerialize(using = EntityAuthorisationHashMapSerializer.class)
public class EntityAuthorisationHashMap extends HashMap<String, EntityAuthorisationMappingDto> {

}
