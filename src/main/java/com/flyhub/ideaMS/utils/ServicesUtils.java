package com.flyhub.ideaMS.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.beans.PropertyDescriptor;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class has methods that are shared across the service classes
 *
 * @author Mawanda Joel
 * @version 1.0
 * @since 8/07/2021
 *
 */
@Component
public class ServicesUtils {

    private static final Logger log = Logger.getLogger(ServicesUtils.class);

    /**
     *
     * @param src
     * @param target
     */
    public void copyNonNullProperties(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }

    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();

        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                log.debug("empty-field: " + pd.getName());
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    /**
     *
     * @param classType
     * @return
     */
    public String[] getAllStringBasedPropertyNames(Class classType) {
        final BeanWrapper beanWrapper = new BeanWrapperImpl(classType);
        PropertyDescriptor[] propertyDescriptors = beanWrapper.getPropertyDescriptors();

        List<String> string_based_property_names = new ArrayList<>();

        for (PropertyDescriptor pd : propertyDescriptors) {
            if (pd.getPropertyType().getName().equals("java.lang.String")) {
                log.debug("adding string type property name: " + pd.getName());
                string_based_property_names.add(pd.getName());
            }
        }

        return string_based_property_names.toArray(new String[string_based_property_names.size()]);

    }

    public String[] getAllStringBasedPropertyNamesWithNullValues(Object obj) {
        final BeanWrapper beanWrapper = new BeanWrapperImpl(obj);

        PropertyDescriptor[] propertyDescriptors = beanWrapper.getPropertyDescriptors();

        List<String> string_based_properties_with_null_values = new ArrayList<>();

        for (PropertyDescriptor pd : propertyDescriptors) {
            if (pd.getPropertyType().getName().equals("java.lang.String") && beanWrapper.getPropertyValue(pd.getName()) == null) {
                log.info("adding string type property name with a null value: " + pd.getName());
                string_based_properties_with_null_values.add(pd.getName());
            }
        }

        return string_based_properties_with_null_values.toArray(new String[string_based_properties_with_null_values.size()]);
    }

    public List<String> getAllStringBasedPropertyNamesWithNullValuesAsAnArray(Object obj) {
        final BeanWrapper beanWrapper = new BeanWrapperImpl(obj);

        PropertyDescriptor[] propertyDescriptors = beanWrapper.getPropertyDescriptors();

        List<String> string_based_properties_with_null_values = new ArrayList<>();

        for (PropertyDescriptor pd : propertyDescriptors) {
            if (pd.getPropertyType().getName().equals("java.lang.String") && beanWrapper.getPropertyValue(pd.getName()) == null) {
                log.info("adding string type property name with a null value: " + pd.getName());
                string_based_properties_with_null_values.add(pd.getName());
            }
        }

        return string_based_properties_with_null_values;
    }

}
