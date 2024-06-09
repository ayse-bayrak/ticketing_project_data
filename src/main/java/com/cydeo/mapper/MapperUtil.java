package com.cydeo.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
public class MapperUtil {

    private final ModelMapper modelMapper;

    public MapperUtil(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

   // I use generic method for the mapper
    public <T> T convert(Object objectToBeConverted, T convertedObject) {
        return  modelMapper.map(objectToBeConverted, (Type) convertedObject.getClass());
    }

//    public <T> T convert(Object objectToBeConverted, Class<T> convertedObject) {
//        return  modelMapper.map(objectToBeConverted, convertedObject);
//    }
}

// maybe we can use directly ModelMapper in our imp class, but util means helps something us, and no one knows what is Model Mapper
// we can directly inject ModelMapper in services, but the thing is, ModelMapper is not the only mapper library out there.
//There are bunch of different mapper libraries, and the thing is, if you later decide to change the mapper library you are using from ModelMapper to something else,
// you would need to update all the services you have to inject the new mapper library and then also update all the method calls related to ModelMapper.
// But since we have a middle layer(MapperUtil), only thing we need to update is MapperUtil class,
// and all the services can still continue functioning with no issue.