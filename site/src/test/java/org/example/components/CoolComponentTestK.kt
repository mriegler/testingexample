package org.example.components

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import org.example.beans.*
import org.hippoecm.hst.container.ModifiableRequestContextProvider
import org.hippoecm.hst.container.RequestContextProvider
import org.hippoecm.hst.core.container.ComponentManager
import org.hippoecm.hst.mock.core.component.MockHstRequest
import org.hippoecm.hst.mock.core.component.MockHstResponse
import org.hippoecm.hst.mock.core.request.MockHstRequestContext
import org.hippoecm.hst.site.HstServices
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringJUnitJupiterConfig

import javax.inject.Inject
import java.util.ArrayList
import java.util.Enumeration
import java.util.Objects

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.nhaarman.mockito_kotlin.whenever
import io.kotlintest.*
import io.kotlintest.extensions.ProjectLevelExtension
import io.kotlintest.specs.WordSpec
import io.kotlintest.spring.SpringAutowireConstructorExtension
import io.kotlintest.spring.SpringListener
import org.junit.jupiter.api.Assertions.assertTrue
import org.mockito.Mockito.*

@SpringJUnitJupiterConfig(locations = arrayOf("/META-INF/hst-assembly/overrides/spring-custom.xml"))
@TestPropertySource(properties = arrayOf("location.api=http://localhost:8089", "weather.api=http://localhost:8089"))
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class CoolComponentTestK(val mapper: ObjectMapper, val applicationContext: ApplicationContext): WordSpec() {

    var componentManager = mock(ComponentManager::class.java)

    var requestContext = MockHstRequestContext()

    lateinit var wireMockServer: WireMockServer

    override fun listeners() = listOf(SpringListener)

    override fun beforeSpec(description: Description, spec: Spec) {
        super.beforeSpec(description, spec)
        wireMockServer = WireMockServer(WireMockConfiguration.options().port(8089)) //No-args constructor will start on port 8080, no HTTPS
        wireMockServer.start()

        log.info("Mapper: {}, componentManager", mapper, componentManager)
        whenever(componentManager.getComponent(any<Class<*>>())).thenReturn(null)
        HstServices.setComponentManager(componentManager)
    }

    override fun afterSpec(description: Description, spec: Spec) {
        super.afterSpec(description, spec)
        wireMockServer.stop()
    }

    init {
        "Given a request, doBeforeRender" should {
            val weatherInformation = WeatherInformation(
                "asd",
                1,
                listOf(WeatherInfo(0, "asd", "asd", "asd")),
                MainWeatherInfo(22f, 123, 123),
                RainVolume(3)
            )
            val locationInformation = LocationInformation(
                "123.123.123.123",
                "Test City",
                "asd",
                22f,
                22f,
                "US"
            )
            wireMockServer.stubFor(get(urlEqualTo("/123.123.123.123/json"))
                .willReturn(
                    okJson(mapper!!
                        .writeValueAsString(locationInformation))))
            wireMockServer.givenThat(
                get(urlMatching("/.*lat=.*"))
                    .willReturn(okJson(mapper!!
                        .writeValueAsString(weatherInformation))))

            ModifiableRequestContextProvider.set(requestContext)
            log.info("request ctx {}", RequestContextProvider.get())
            val bean = applicationContext!!.getBean(CoolComponent::class.java)
            val mockHstRequest = MockHstRequest().apply {
                remoteAddr = "123.123.123.123"
                remoteHost = "host"
                remoteUser = "user"
            }
            log.info("Request: {}, {}", mockHstRequest, mockHstRequest.remoteAddr)
            bean.doBeforeRender(mockHstRequest, MockHstResponse())

            "set the correct attributes" {
                mockHstRequest.getAttribute("cool") shouldNotBe null
                mockHstRequest.getAttribute("weatherInformation").let {
                    shouldNotBe(null)
                    (it as WeatherInformation) shouldBe weatherInformation
                }
            }
        }
    }
    companion object {
        private val log = LoggerFactory.getLogger(CoolComponentTestK::class.java)
    }
}