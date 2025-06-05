package com.algasensors.temperature.monitoring.api.controller;

import com.algasensors.temperature.monitoring.api.model.SensorAlertInput;
import com.algasensors.temperature.monitoring.api.model.SensorAlertOutput;
import com.algasensors.temperature.monitoring.domain.model.SensorAlert;
import com.algasensors.temperature.monitoring.domain.model.SensorId;
import com.algasensors.temperature.monitoring.domain.repository.SensorAlertRepository;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sensors/{sensorId}/alert")
public class SensorAlertController {

    private final SensorAlertRepository sensorAlertRepository;

    @GetMapping
    public SensorAlertOutput getAlert(@PathVariable("sensorId") TSID sensorId) {
        SensorAlert sensorAlert = findById(sensorId);

        return new SensorAlertOutput(
                sensorAlert.getSensorId().getValue(),
                sensorAlert.getMaxTemperature(),
                sensorAlert.getMinTemperature()
        );
    }

    @PutMapping
    public SensorAlertOutput createOrUpdate(@PathVariable TSID sensorId,
                                            @RequestBody SensorAlertInput input) {
        SensorAlert sensorAlert = findSensorByIdOrThrowNoContent(sensorId);
        sensorAlert.setMinTemperature(input.minTemperature());
        sensorAlert.setMaxTemperature(input.maxTemperature());
        sensorAlertRepository.saveAndFlush(sensorAlert);

        return new SensorAlertOutput(
                sensorAlert.getSensorId().getValue(),
                sensorAlert.getMinTemperature(),
                sensorAlert.getMaxTemperature());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable TSID sensorId) {
        SensorAlert sensorAlert = findById(sensorId);
        sensorAlertRepository.delete(sensorAlert);
    }

    private SensorAlert findById(TSID sensorId) {
        return sensorAlertRepository.findById(new SensorId(sensorId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private SensorAlert findSensorByIdOrThrowNoContent(TSID sensorId) {
        return sensorAlertRepository.findById(new SensorId(sensorId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT));
    }
}
