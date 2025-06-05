package com.algasensors.temperature.monitoring.api.model;

import io.hypersistence.tsid.TSID;

import java.time.OffsetDateTime;

public record SensorMonitoringOutput(
        TSID sensorId,
        Double lastTemperature,
        OffsetDateTime updatedAt,
        Boolean enabled
) {
}
