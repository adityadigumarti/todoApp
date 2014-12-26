package com.mashable.assignment.sms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twilio.sdk.resource.instance.Account;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

/**
 * 
 * 
 * @author Adi
 * 
 */
@Service("twiloClientService")
public class TwiloClientServiceImpl implements SmsClientService {

    private final static Logger LOG = LoggerFactory.getLogger(TwiloClientServiceImpl.class);

    private static final String TWILIO_BODY = "Body";
    private static final String TWILIO_FROM = "From";
    private static final String TWILIO_TO = "To";

    @Value("${twilo.account.sid}")
    private String accountSID;

    @Value("${twilo.auth.token}")
    private String authToken;

    @Value("${twilo.account.number}")
    private String fromNumber;

    public void sendText(String message, String phoneNumber) {

        // Should this be cached??
        TwilioRestClient client = new TwilioRestClient(accountSID, authToken);

        Account account = client.getAccount();
        MessageFactory messageFactory = account.getMessageFactory();
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(TWILIO_TO, phoneNumber));
        params.add(new BasicNameValuePair(TWILIO_FROM, fromNumber));
        params.add(new BasicNameValuePair(TWILIO_BODY, message));

        try {
            messageFactory.create(params);
        } catch (TwilioRestException tre) {
            // Since sending SMS is the final step, i am not rolling back the whole transaction because of an exception
            // here. Queuing the SMS messages and having a scheduler retry failures would be the right way to go.
            LOG.error("Exception sending SMS message to " + phoneNumber, tre);
        }
    }

    public String getAccountSID() {
        return accountSID;
    }

    public void setAccountSID(String accountSID) {
        this.accountSID = accountSID;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getFromNumber() {
        return fromNumber;
    }

    public void setFromNumber(String fromNumber) {
        this.fromNumber = fromNumber;
    }

}
