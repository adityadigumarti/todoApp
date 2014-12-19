package com.mashable.assignment.twilo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.twilio.sdk.resource.instance.Account;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class TwiloClient {

    private final static Logger LOG = LoggerFactory.getLogger(TwiloClient.class);

    private static final String TWILIO_BODY = "Body";
    private static final String TWILIO_FROM = "From";
    private static final String TWILIO_TO = "To";

    // TODO - Put these in a resource file.
    private static final String ACCOUNT_SID = "ACd03e4f7946af6ad4c9a9e17c679bca01";
    private static final String AUTH_TOKEN = "f9c2f08f9557e33bdb4efddfa6dc8173";
    private static final String MY_TWILIO_NUMBER = "+14242874460";
    private static String TO_NUMBER = "+15626733141";

    private synchronized static void updateToNumber(String number) {
        LOG.info(String.format("Updating the Phone Number for sending SMS Messages from %s to %s ", TO_NUMBER, number));

        TO_NUMBER = "+1" + number;
    }

    public static void sendText(String message, String phoneNumber) {
        TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);

        Account account = client.getAccount();
        MessageFactory messageFactory = account.getMessageFactory();
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(TWILIO_TO, phoneNumber));
        params.add(new BasicNameValuePair(TWILIO_FROM, MY_TWILIO_NUMBER));
        params.add(new BasicNameValuePair(TWILIO_BODY, message));

        try {
            messageFactory.create(params);
        } catch (TwilioRestException tre) {
            // Since sending SMS is the final step, i am not rolling back the whole transaction because of an exception
            // here. Queuing the SMS messages and having a scheduler retry failures would be the right way to go.
            LOG.error("Exception sending SMS message to " + phoneNumber, tre);
        }
    }
}
