package com.jamieburns.noclaimdiscount;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.speechlet.SpeechletV2;
import com.amazon.speech.ui.OutputSpeech;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import org.ocpsoft.prettytime.PrettyTime;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.infura.InfuraHttpService;
import org.web3j.tx.ClientTransactionManager;

import java.math.BigInteger;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class NoClaimDiscountSpeechlet implements SpeechletV2 {

    private final static String CONTRACT_ADDRESS = "CONTRACT_ADDRESS_GOES_HERE";
    private final static String INFURA_KEY = "INFURA_KEY_GOES_HERE";
    private final static String NETWORK = "rinkeby";

    private final String intent = "NoClaimDiscountIntent";
    private final String cardTitle = "Insurer";

    private final Web3j web3 = Web3jFactory.build(new InfuraHttpService("https://" + NETWORK + ".infura.io/" + INFURA_KEY));
    private final ClientTransactionManager readOnlyTransactionManager = new ClientTransactionManager(web3, "0x0000000000000000000000000000000000000000");

    private synchronized String fetchReply() {

        NoClaimDiscount contract =
            NoClaimDiscount.load(
                CONTRACT_ADDRESS,
                web3,
                readOnlyTransactionManager,
                BigInteger.ONE,
                BigInteger.ONE
            );

        if (contract != null) {

            try {

                BigInteger accrualFrom =
                    contract
                        .accrualFrom()
                        .sendAsync()
                        .get(5000, TimeUnit.MILLISECONDS);

                long from = Long.parseLong(accrualFrom.toString()) * 1000;

                String formattedDifference =
                    (new PrettyTime())
                        .format(new Date(from))
                        .replace(" ago", "")
                        .trim();

                if (!formattedDifference.contains(" ")) {
                    formattedDifference = "1 minute";
                }

                return "Your no claims discount is: " + formattedDifference;

            } catch (ExecutionException | InterruptedException | TimeoutException e) {
                return "Sorry, the block chain took too long to query this time.";
            }
        }

        return "Sorry, the block chain contract could not be found this time.";
    }

    private SpeechletResponse getWelcomeResponse() {

        return getAskResponse(
            cardTitle,
            "Welcome to the No Claims Discount demo, you can say hello"
        );
    }

    private SpeechletResponse getHelpResponse() {

        return getAskResponse(
            cardTitle,
            "You can say hello to me!"
        );
    }

    // ---------------------------------------------------------------------------
    // event handlers ...
    // ---------------------------------------------------------------------------

    public SpeechletResponse onLaunch(
        SpeechletRequestEnvelope<LaunchRequest> requestEnvelope
    ) {
        return getWelcomeResponse();
    }

    public SpeechletResponse onIntent(
        SpeechletRequestEnvelope<IntentRequest> requestEnvelope
    ) {
        IntentRequest request = requestEnvelope.getRequest();
        Intent intent = request.getIntent();
        String intentName = (intent != null) ? intent.getName() : null;

        if (this.intent.equals(intentName)
            || "HelloWorldIntent".equals(intentName)) {

            return getSpeechResponse(
                cardTitle,
                fetchReply()
            );

        } else if ("AMAZON.HelpIntent".equals(intentName)) {
            return getHelpResponse();
        } else {
            return getAskResponse(
                cardTitle,
                "This is unsupported.  Please try something else."
            );
        }
    }

    public void onSessionStarted(
        SpeechletRequestEnvelope<SessionStartedRequest> requestEnvelope
    ) {
        // noop
    }

    public void onSessionEnded(
        SpeechletRequestEnvelope<SessionEndedRequest> requestEnvelope
    ) {
        // noop
    }

    // ---------------------------------------------------------------------------
    // helper methods ...
    // ---------------------------------------------------------------------------

    private SpeechletResponse getAskResponse(
        String cardTitle,
        String speechText
    ) {
        SimpleCard card = getSimpleCard(cardTitle, speechText);
        PlainTextOutputSpeech speech = getPlainTextOutputSpeech(speechText);
        Reprompt reprompt = getReprompt(speech);
        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }

    private SpeechletResponse getSpeechResponse(
        String cardTitle,
        String speechText
    ) {
        SimpleCard card = getSimpleCard(cardTitle, speechText);
        PlainTextOutputSpeech speech = getPlainTextOutputSpeech(speechText);
        return SpeechletResponse.newTellResponse(speech, card);
    }

    private PlainTextOutputSpeech getPlainTextOutputSpeech(
        String speechText
    ) {
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);
        return speech;
    }

    private Reprompt getReprompt(
        OutputSpeech outputSpeech
    ) {
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(outputSpeech);
        return reprompt;
    }

    private SimpleCard getSimpleCard(
        String title,
        String content
    ) {
        SimpleCard card = new SimpleCard();
        card.setTitle(title);
        card.setContent(content);
        return card;
    }

    // ---------------------------------------------------------------------------
}
