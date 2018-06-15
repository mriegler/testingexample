package org.example.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.example.beans.WeatherInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class Weather {

    private final static Logger log = LoggerFactory.getLogger(Weather.class);

    private final String api;
    private final ObjectMapper objectMapper;
    private final String apiKey;
    private final ConcurrentHashMap<String, WeatherInformation> cache = new ConcurrentHashMap<>();

    @Autowired
    public Weather(
            @Value("${weather.api}") String api,
            @Value("${weather.apikey}") String apiKey,
            ObjectMapper objectMapper
    ) {
        this.api = api;
        this.apiKey = apiKey;
        this.objectMapper = objectMapper;
    }

    public WeatherInformation fromCoordinates(Float latitude, Float longitude) {
        return cache.computeIfAbsent(latitude.toString() + longitude.toString(), key -> {
            try {
                final URI uri = new URIBuilder(api)
                        .addParameter("lat", latitude.toString())
                        .addParameter("lon", longitude.toString())
                        .addParameter("appId", apiKey)
                        .addParameter("units", "metric")
                        .build();

                log.debug("Getting weather with uri: {}", uri);
                final String jsonString = Request.Get(uri).execute().returnContent().asString();

                return objectMapper.readValue(jsonString, WeatherInformation.class);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        });
    }

}
