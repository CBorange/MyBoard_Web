package com.ltj.myboard.domain;

import javax.persistence.AttributeConverter;

public class UserGradeLevelConverter implements AttributeConverter<UserGradeLevel, Integer> {
    @Override
    public Integer convertToDatabaseColumn(UserGradeLevel attribute) {
        return attribute.getValue();
    }

    @Override
    public UserGradeLevel convertToEntityAttribute(Integer dbData) {
        return UserGradeLevel.fromInteger(dbData);
    }
}
