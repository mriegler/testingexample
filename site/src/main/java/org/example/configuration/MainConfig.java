package org.example.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import sun.applet.Main;

@Configuration
@ComponentScan(basePackages = {"org.example"})
@PropertySource("classpath:org/example/main.properties")
public class MainConfig implements InitializingBean {
    private static final Logger log = LoggerFactory.getLogger(MainConfig.class);
    @Value("${location.api}")
    String api;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("API is {}", api);
    }
}
