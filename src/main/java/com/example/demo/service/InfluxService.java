package com.example.demo.service;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.write.Point;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class InfluxService {

    private final InfluxDBClient influxDBClient;

    public InfluxService() {
        String url = "http://localhost:8086";
        String token = "0Ire3dlHNc-PLubBjQegfFa4Bxky__EQjxRds8DeU7zsakV1RA-gVPvukpms7yD3hraL6rf1hCnjyGFBFxSr-Q=="; // isko change krna muhae
        String org = "weather-org";
        String bucket = "weather-data";

        this.influxDBClient = InfluxDBClientFactory.create(
                url,
                token.toCharArray(),
                org,
                bucket
        );
    }

    public void saveWeather(String city, double temp) {
        Point point = Point.measurement("weather")
                .addTag("city", city)
                .addField("temperature", temp)
                .time(Instant.now(), com.influxdb.client.domain.WritePrecision.NS);

        influxDBClient.getWriteApiBlocking().writePoint(point);
    }
}