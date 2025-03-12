package com.rtd.TempMail.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.StandardEnvironment;
import java.util.Properties;

@Configuration
public class EnvConfig {
    @Bean
    public Properties environmentProperties(Environment env) {
        Properties props = new Properties();
        
        try {
            Dotenv dotenv = Dotenv.configure()
                .directory("src/main/resources")
                .ignoreIfMissing()
                .load();
                
            props.put("MAIL_USERNAME", dotenv.get("MAIL_USERNAME", "howuga12@gmail.com"));
            props.put("MAIL_APP_PASSWORD", dotenv.get("MAIL_APP_PASSWORD", "uuwwcqxdiccqvxpt"));
        } catch (Exception e) {
            // Fallback to default values if .env file is not found
            props.put("MAIL_USERNAME", "howuga12@gmail.com");
            props.put("MAIL_APP_PASSWORD", "uuwwcqxdiccqvxpt");
        }
        
        ((StandardEnvironment) env).getPropertySources()
            .addFirst(new PropertiesPropertySource("dotenv", props));
            
        return props;
    }
} 