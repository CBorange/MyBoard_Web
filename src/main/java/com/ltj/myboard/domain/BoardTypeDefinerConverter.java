package com.ltj.myboard.domain;

import javax.persistence.AttributeConverter;

public class BoardTypeDefinerConverter implements AttributeConverter<BoardTypeDefiner, Integer>
{

    @Override
    public Integer convertToDatabaseColumn(BoardTypeDefiner attribute) {
        return attribute.getValue();
    }

    @Override
    public BoardTypeDefiner convertToEntityAttribute(Integer dbData) {
        return BoardTypeDefiner.fromInteger(dbData);
    }
}
