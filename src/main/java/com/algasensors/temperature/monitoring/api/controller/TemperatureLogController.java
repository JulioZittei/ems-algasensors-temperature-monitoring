package com.algasensors.temperature.monitoring.api.controller;

import com.algasensors.temperature.monitoring.api.model.TemperatureLogOutput;
import com.algasensors.temperature.monitoring.domain.model.SensorId;
import com.algasensors.temperature.monitoring.domain.model.TemperatureLog;
import com.algasensors.temperature.monitoring.domain.repository.TemperatureLogRepository;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sensors/{sensorId}/temperatures")
public class TemperatureLogController {

    private final TemperatureLogRepository temperatureLogRepository;

    @GetMapping
    public PagedModel<TemperatureLogOutput> search(@PathVariable("sensorId") TSID sensorId, @PageableDefault Pageable pageable, @RequestHeader Map<String, Object> headers) {
        var temperatureLogs = temperatureLogRepository.findAllBySensorId(new SensorId(sensorId), pageable);

        return new PagedModel<>(temperatureLogs.map(this::convertToOutput));
    }

    private TemperatureLogOutput convertToOutput(TemperatureLog temperatureLog) {
        return new TemperatureLogOutput(
                temperatureLog.getIdAsUUID(),
                temperatureLog.getSensorIdAsTSID(),
                temperatureLog.getRegisteredAt(),
                temperatureLog.getValue()
        );
    }
}
