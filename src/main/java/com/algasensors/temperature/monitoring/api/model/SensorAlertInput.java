package com.algasensors.temperature.monitoring.api.model;

public record SensorAlertInput(
        Double maxTemperature,
        Double minTemperature
) {
}
