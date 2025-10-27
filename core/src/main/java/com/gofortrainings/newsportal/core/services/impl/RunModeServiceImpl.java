package com.gofortrainings.newsportal.core.services.impl;

import com.gofortrainings.newsportal.core.configuration.RunModeTest;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;

@Component(service = RunModeServiceImpl.class)
@Designate(ocd = RunModeTest.class)
public class RunModeServiceImpl {

    @Activate
    @Modified
    public void activate(RunModeTest config){
        config.runModeValue();
    }
}
