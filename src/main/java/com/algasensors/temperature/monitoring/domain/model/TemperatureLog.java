package com.algasensors.temperature.monitoring.domain.model;

import io.hypersistence.tsid.TSID;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TemperatureLog {

    @Id
    @AttributeOverride(name = "value", column = @Column(name = "id", columnDefinition = "UUID"))
    private TemperatureLogId id;

    @Column(name = "\"value\"")
    private Double value;

    private OffsetDateTime registeredAt;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "sensor_id", columnDefinition = "BIGINT"))
    private SensorId sensorId;

    public String getIdAsString() {
        return id.toString();
    }

    public UUID getIdAsUUID() {
        return id.getValue();
    }

    public String getSensorIdAsString() {
        return sensorId.toString();
    }

    public TSID getSensorIdAsTSID() {
        return sensorId.getValue();
    }

    public Long getSensorIdAsLong() {
        return sensorId.toLong();
    }

}
