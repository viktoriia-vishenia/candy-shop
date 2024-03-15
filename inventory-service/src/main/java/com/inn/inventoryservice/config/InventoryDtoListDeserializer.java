package com.inn.inventoryservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inn.inventoryservice.dto.InventoryDto;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.util.ArrayList;

public class InventoryDtoListDeserializer implements  Deserializer<ArrayList<InventoryDto>> {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public ArrayList<InventoryDto> deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }
        try {
            return objectMapper.readValue(data, objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, InventoryDto.class));
        } catch (IOException e) {
            throw new SerializationException("Error deserializing byte[] to ArrayList<InventoryDto>", e);
        }
    }

    @Override
    public void close() {
    }
}


