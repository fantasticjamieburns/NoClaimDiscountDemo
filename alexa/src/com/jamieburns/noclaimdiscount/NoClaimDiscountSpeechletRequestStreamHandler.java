package com.jamieburns.noclaimdiscount;

import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;

import java.util.HashSet;
import java.util.Set;

public final class NoClaimDiscountSpeechletRequestStreamHandler extends SpeechletRequestStreamHandler {

    private static final Set<String> ALLOWED_IDS;

    static {
        ALLOWED_IDS = new HashSet<>();
        ALLOWED_IDS.add(
            "AWS_APPLICATION_ID_GOES_HERE"
        );
    }

    public NoClaimDiscountSpeechletRequestStreamHandler() {
        super(new NoClaimDiscountSpeechlet(), ALLOWED_IDS);
    }
}
