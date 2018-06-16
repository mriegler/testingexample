package org.example.components;

import org.example.beans.LocationInformation;
import org.example.beans.WeatherInformation;
import org.example.services.Location;
import org.example.services.Weather;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.onehippo.cms7.essentials.components.CommonComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Lazy
@Scope("prototype")
public class CoolComponent extends CommonComponent {

    private final static Logger log = LoggerFactory.getLogger(CoolComponent.class);
    private final Location location;
    private final Weather weather;

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        super.doBeforeRender(request, response);
        try {
            request.setAttribute("cool", true);
            //TODO: only use hardcoded IP when request is coming from local host
            final LocationInformation info = location.fromIp("123.123.123.123");
            request.setAttribute("locationInfo", info);
            log.info("Location Info: {}", info);
            final WeatherInformation weatherInformation = weather.fromCoordinates(info.getLatitude(), info.getLongitude());
            log.info("Weather info: {}", weatherInformation);
            request.setAttribute("weatherInformation", weatherInformation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Autowired
    public CoolComponent(Location location, Weather weather) {
        this.location = location;
        this.weather = weather;
    }

}
