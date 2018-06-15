package org.example.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.kotlin.KotlinModule;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.example.beans.LocationInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class Location {
    private final String api;
    private final static Logger log = LoggerFactory.getLogger(Location.class);
    private final ObjectMapper objectMapper;

    public Location(@Value("${location.api}") String api, @Autowired ObjectMapper objectMapper) {
        this.api = api;
        this.objectMapper = objectMapper;
    }

    public LocationInformation fromIp(final String ip) {
        final String url = new StringBuilder(api)
                .append("/")
                .append(ip)
                .append("/json")
                .toString();

        log.debug("Getting location for ip {}, resulting in url {}", ip, url);

        try {
            final String jsonString = Request.Get(url).execute().returnContent().asString();

            final LocationInformation result = objectMapper
                    .readValue(jsonString, LocationInformation.class);

            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
