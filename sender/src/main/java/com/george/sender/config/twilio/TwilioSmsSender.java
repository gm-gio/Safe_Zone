package com.george.sender.config.twilio;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class TwilioSmsSender implements SmsSender {

    private final TwilioConfig twilioConfig;

    @Override
    public void sendSms(String message, String phone) {

        if (isPhoneNumberValid(phone)) {

            PhoneNumber to = new PhoneNumber(phone);
            PhoneNumber from = new PhoneNumber(twilioConfig.getPhoneNumber());

            MessageCreator creator = Message.creator(to, from, message);
            creator.create();

            log.info("Send sms to: {}", phone);

        } else {
            throw new IllegalArgumentException(
                    "Phone number [" + phone + "] is not valid"
            );
        }
    }

    private boolean isPhoneNumberValid(String phone) {
        if (phone == null || phone.isEmpty()) {
            return false;
        }
        String regex = "^\\+?[1-9]\\d{1,14}$";
        return phone.matches(regex);
    }
}

