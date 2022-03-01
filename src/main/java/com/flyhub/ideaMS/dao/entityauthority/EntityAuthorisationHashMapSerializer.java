package com.flyhub.ideaMS.dao.entityauthority;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * Creates a format for the JWT in the format as shown below:
 *
 * <code>
 * "APP_CONFIG_MODULE.access_mode": true,
 * "APP_CONFIG_MODULE.create_allowed": true,
 * "APP_CONFIG_MODULE.delete_allowed": true,
 * "APP_CONFIG_MODULE.print_allowed": true,
 * "APP_CONFIG_MODULE.read_allowed": true,
 * "APP_CONFIG_MODULE.search_allowed": true,
 * "APP_CONFIG_MODULE.update_allowed": true,
 * "FINANCIAL_MODULE.access_mode": true,
 * "FINANCIAL_MODULE.create_allowed": true,
 * "FINANCIAL_MODULE.delete_allowed": true,
 * "FINANCIAL_MODULE.print_allowed": true,
 * "FINANCIAL_MODULE.read_allowed": true,
 * "FINANCIAL_MODULE.search_allowed": true,
 * "FINANCIAL_MODULE.update_allowed": true
 * </code>
 *
 * @author Benjamin E Ndugga
 */

public class EntityAuthorisationHashMapSerializer extends StdSerializer<EntityAuthorisationHashMap> {

    public EntityAuthorisationHashMapSerializer() {
        this(null);
    }

    public EntityAuthorisationHashMapSerializer(Class<EntityAuthorisationHashMap> t) {
        super(t);
    }

    @Override
    public void serialize(EntityAuthorisationHashMap value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {

        jgen.writeStartObject();

        for (String key : value.keySet()) {
            EntityAuthorisationMappingDto authorisationMappingDto = value.get(key);
            jgen.writeBooleanField(key + ".access_mode", authorisationMappingDto.getAccessMode());
            jgen.writeBooleanField(key + ".create_allowed", authorisationMappingDto.getCreateAllowed());
            jgen.writeBooleanField(key + ".delete_allowed", authorisationMappingDto.getDeleteAllowed());
            jgen.writeBooleanField(key + ".print_allowed", authorisationMappingDto.getPrintAllowed());
            jgen.writeBooleanField(key + ".read_allowed", authorisationMappingDto.getReadAllowed());
            jgen.writeBooleanField(key + ".search_allowed", authorisationMappingDto.getSearchAllowed());
            jgen.writeBooleanField(key + ".update_allowed", authorisationMappingDto.getUpdateAllowed());
        }

        jgen.writeEndObject();
    }
}