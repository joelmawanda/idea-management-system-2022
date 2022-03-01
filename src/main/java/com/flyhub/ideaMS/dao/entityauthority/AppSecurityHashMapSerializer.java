
package com.flyhub.ideaMS.dao.entityauthority;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.Set;

/**
 *
 * @author Benjamin E Ndugga
 */

public class AppSecurityHashMapSerializer extends StdSerializer<EntityAuthorisationHashMap> {
    
    

    public AppSecurityHashMapSerializer() {
        this(null);
    }

    public AppSecurityHashMapSerializer(Class<EntityAuthorisationHashMap> t) {
        super(t);
    }

    @Override
    public void serialize(EntityAuthorisationHashMap value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {

        //"APP_CONFIG_MODULE.access_mode":true,
        jgen.writeStartObject();
        Set<String> keySet = value.keySet();

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
