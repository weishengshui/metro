package com.chinarewards.metro.core.common;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

public class DateSerializer extends JsonSerializer<Date>{
	
	@Override
	public void serialize(Date date, JsonGenerator jgen, SerializerProvider provider)
			throws IOException, JsonProcessingException {
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDate = formatter.format(date);
		jgen.writeString(formattedDate);
	}
}
