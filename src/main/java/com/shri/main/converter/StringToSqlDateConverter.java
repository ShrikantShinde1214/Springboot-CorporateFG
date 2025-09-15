package com.shri.main.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import java.sql.Date;

@Component
public class StringToSqlDateConverter implements Converter<String, Date> {
    @Override
    public Date convert(String source) {
        return (source == null || source.trim().isEmpty()) ? null : Date.valueOf(source);
    }
}
