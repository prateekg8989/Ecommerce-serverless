package com.axis;

import com.axis.handler.HandlerApiGateway;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writeValueAsString(null) instanceof String);
    }
}
