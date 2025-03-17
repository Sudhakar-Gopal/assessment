package util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.GameConfig;

import java.io.File;
import java.io.IOException;

public class ConfigLoader {
    public static GameConfig loadConfig(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // Ignore unknown fields

        GameConfig config =  objectMapper.readValue(new File(filePath), GameConfig.class);
        //System.out.println(objectMapper.writeValueAsString(config));
        return config;
    }
}
