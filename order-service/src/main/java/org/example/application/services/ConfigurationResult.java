package org.example.application.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.dataAccess.models.Component;
import org.example.dataAccess.models.ComponentType;

import java.util.Map;

@Data
@AllArgsConstructor
public class ConfigurationResult {
    private boolean isValid;
    private String errorMessage;
    private double totalPrice;
    private Map<ComponentType, Component> validatedComponents;

    public static ConfigurationResult valid(double totalPrice, Map<ComponentType, Component> components) {
        return new ConfigurationResult(true, null, totalPrice, components);
    }

    public static ConfigurationResult invalid(String errorMessage) {
        return new ConfigurationResult(false, errorMessage, 0, null);
    }
}