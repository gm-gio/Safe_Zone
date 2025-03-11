package com.george.notification.config.twilio;

import com.george.clients.user.UserResponse;
import com.george.notification.dto.NotificationRequest;
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
    public void sendSms(String message, UserResponse userResponse) {
        if (isPhoneNumberValid(userResponse.getPhone())) {

            PhoneNumber to = new PhoneNumber(userResponse.getPhone());
            PhoneNumber from = new PhoneNumber(twilioConfig.getPhoneNumber());
            MessageCreator creator = Message.creator(to, from, message);
            creator.create();
            log.info("Send sms to: {}", userResponse.getPhone());

        } else {
            throw new IllegalArgumentException(
                    "Phone number [" + userResponse.getPhone() + "] is not valid"
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

