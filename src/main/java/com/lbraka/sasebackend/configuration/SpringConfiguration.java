package com.lbraka.sasebackend.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;

/**
 * Created by Lauris on 02/06/2017.
 */
@Configuration
@ComponentScan
public class SpringConfiguration {

    @Bean(initMethod="start",destroyMethod="stop")
    public org.h2.tools.Server h2WebConsonleServer () throws SQLException {
        return org.h2.tools.Server.createWebServer("-web","-webAllowOthers","-webDaemon","-webPort", "8082");
    }
}
