package com.accommodation.system.config;

import com.twilio.Twilio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwilioInitializer {
    @Autowired
    public TwilioInitializer() {
        Twilio.init(
                "AC0a36e2096653fbe2f32d250e790b3f00",
                "ca41d08e93d935f7eacc6416f86604ab"
        );
    }
}
