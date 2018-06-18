package org.example.components;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.example.beans.*;
import org.hippoecm.hst.container.ModifiableRequestContextProvider;
import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.core.container.ComponentManager;
import org.hippoecm.hst.mock.core.component.MockHstRequest;
import org.hippoecm.hst.mock.core.component.MockHstResponse;
import org.hippoecm.hst.mock.core.request.MockHstRequestContext;
import org.hippoecm.hst.site.HstServices;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitJupiterConfig;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

@SpringJUnitJupiterConfig(locations = {"/META-INF/hst-assembly/overrides/spring-custom.xml"})
@TestPropertySource(properties = {"location.api=http://localhost:8089", "weather.api=http://localhost:8089"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CoolComponentTest {
    private static final Logger log = LoggerFactory.getLogger(CoolComponentTest.class);

    @Inject
    ApplicationContext applicationContext;

    @Inject
    ObjectMapper mapper;

    ComponentManager componentManager = mock(ComponentManager.class);

    MockHstRequestContext requestContext = new MockHstRequestContext();

    WireMockServer wireMockServer;

    @BeforeAll
    void setup() {
        wireMockServer = new WireMockServer(WireMockConfiguration.options().port(8089)); //No-args constructor will start on port 8080, no HTTPS
        wireMockServer.start();

        log.info("Mapper: {}, componentManager", mapper, componentManager);
        when(componentManager.getComponent(any(Class.class))).thenReturn(null);
        ModifiableRequestContextProvider.set(requestContext);
        HstServices.setComponentManager(componentManager);
    }

    @AfterAll
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void testRenderWithRestApi() throws JsonProcessingException {
        List<WeatherInfo> weatherInfos = new ArrayList<>();
        weatherInfos.add(new WeatherInfo(0, "asd", "asd", "asd"));
        MainWeatherInfo mainInfo = new MainWeatherInfo(22.0f, 123, 123);
        final WeatherInformation weatherInformation = new WeatherInformation(
                "asd",
                1,
                weatherInfos,
                mainInfo,
                new RainVolume(3)
        );
        final LocationInformation locationInformation = new LocationInformation(
                "123.123.123.123",
                "Test City",
                "asd",
                22f,
                22f,
                "US"
        );
        wireMockServer.stubFor(get(urlEqualTo("/123.123.123.123/json"))
                .willReturn(
                        okJson(mapper
                                .writeValueAsString(locationInformation))));
        wireMockServer.givenThat(get(urlMatching("/.*lat=.*"))
                .willReturn(okJson(mapper
                        .writeValueAsString(weatherInformation))));

        log.info("request ctx {}", RequestContextProvider.get());
        final CoolComponent bean = applicationContext.getBean(CoolComponent.class);
        final MockHstRequest mockHstRequest = new MockHstRequest();
        mockHstRequest.setRemoteAddr("123.123.123.123");
        mockHstRequest.setRemoteHost("host");
        mockHstRequest.setRemoteUser("user");
        bean.doBeforeRender(mockHstRequest, new MockHstResponse());

        assertTrue(Objects.nonNull(mockHstRequest.getAttribute("cool")));
        assertTrue(Objects.nonNull(mockHstRequest.getAttribute("weatherInformation")));
    }
}