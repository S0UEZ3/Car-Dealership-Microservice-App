package org.example.application.services;

import lombok.NoArgsConstructor;
import org.example.dataAccess.models.CarModel;
import org.example.dataAccess.models.Component;
import org.example.dataAccess.models.ComponentType;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@NoArgsConstructor
public class Configurator {

    public ConfigurationResult validateConfiguration(CarModel carModel, Map<ComponentType, Component> components) {
        if (!hasAllRequiredComponents(components)) {
            return ConfigurationResult.invalid("Missing required components");
        }

        for (Map.Entry<ComponentType, Component> entry : components.entrySet()) {
            if (!isComponentCompatible(entry.getValue(), carModel)) {
                return ConfigurationResult.invalid("Component " + entry.getValue().getName() + " is not compatible with " + carModel.getModel());
            }
        }

        double totalPrice = calculateTotalPrice(carModel, components);
        return ConfigurationResult.valid(totalPrice, components);
    }

    private boolean hasAllRequiredComponents(Map<ComponentType, Component> components) {
        return components.containsKey(ComponentType.RIMS) &&
                components.containsKey(ComponentType.TRANSMISSION) &&
                components.containsKey(ComponentType.STEERING_WHEEL) &&
                components.containsKey(ComponentType.INTERIOR);
    }

    private boolean isComponentCompatible(Component component, CarModel carModel) {
        return component.getCompatibleModels().contains(carModel);
    }

    private double calculateTotalPrice(CarModel carModel, Map<ComponentType, Component> components) {
        double basePrice = carModel.getBasePrice();
        double extraCost = components.values().stream().mapToDouble(Component::getPrice).sum();
        return basePrice + extraCost;
    }
}