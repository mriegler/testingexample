package org.example.components;

import freemarker.core.Environment;
import org.example.beans.LocationInformation;
import org.example.services.Location;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.onehippo.cms7.essentials.components.CommonComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component
@Lazy
@Scope("prototype")
public class CoolComponent extends CommonComponent {

    private final static Logger log = LoggerFactory.getLogger(CoolComponent.class);
    private final Location location;

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        super.doBeforeRender(request, response);
        request.setAttribute("cool", true);
        //TODO: only use hardcoded IP when request is coming from local host
        final LocationInformation info = location.fromIp("149.172.110.16");
        request.setAttribute("locationInfo", info);
    }

    @Autowired
    public CoolComponent(Location location) {
        this.location = location;
    }
}
