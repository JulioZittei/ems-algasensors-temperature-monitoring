package com.algasensors.temperature.monitoring.api.controller;


import com.algasensors.temperature.monitoring.api.model.SensorMonitoringOutput;
import com.algasensors.temperature.monitoring.domain.model.SensorId;
import com.algasensors.temperature.monitoring.domain.model.SensorMonitoring;
import com.algasensors.temperature.monitoring.domain.repository.SensorMonitoringRepository;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sensors/{sensorId}/monitoring")
public class SensorMonitoringController {

    private final SensorMonitoringRepository sensorMonitoringRepository;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public SensorMonitoringOutput getDetail(@PathVariable("sensorId") TSID sensorId, @RequestHeader Map<String, Object> headers) {
        var sensorMonitoring = findSensorMonitoringByIdOrThrowNoContent(sensorId);

        return new SensorMonitoringOutput(
                sensorId,
                sensorMonitoring.getLastTemperature(),
                sensorMonitoring.getUpdatedAt(),
                sensorMonitoring.getEnabled()
        );
    }

    @PutMapping("/enable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enable(@PathVariable("sensorId") TSID sensorId, @RequestHeader Map<String, Object> headers) {
        var sensorMonitoring = findSensorMonitoringByIdOrThrowNoContent(sensorId);

        if (Boolean.TRUE.equals(sensorMonitoring.getEnabled())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        sensorMonitoring.setEnabled(true);
        sensorMonitoringRepository.save(sensorMonitoring);
    }

    @DeleteMapping("/enable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void disable(@PathVariable("sensorId") TSID sensorId, @RequestHeader Map<String, Object> headers) {
        var sensorMonitoring = findSensorMonitoringByIdOrThrowNoContent(sensorId);

        if (Boolean.FALSE.equals(sensorMonitoring.getEnabled())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        sensorMonitoring.setEnabled(false);
        sensorMonitoringRepository.save(sensorMonitoring);
    }

    private SensorMonitoring findSensorMonitoringByIdOrThrowNoContent(TSID sensorId) {
        return sensorMonitoringRepository.findById(new SensorId(sensorId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT));
    }
}
