package com.george.notification.config.twilio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties("twilio")
public class TwilioConfig {

    private String accountSid;
    private String authToken;
    private String phoneNumber;
}
