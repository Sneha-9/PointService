package com.sneha;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigProperties {

    @Bean
    @ConfigurationProperties(prefix = "userservice")
    public UserServiceConfig getUserServiceConfig(){
        return new UserServiceConfig();
    }

    @Getter
    @Setter
   public class UserServiceConfig{
        private String host;
        private String path;
    }
}
