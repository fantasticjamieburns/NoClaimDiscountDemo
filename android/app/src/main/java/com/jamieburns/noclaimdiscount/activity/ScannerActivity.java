package com.jamieburns.noclaimdiscount.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.jamieburns.noclaimdiscount.R;
import com.jamieburns.noclaimdiscount.contract.NoClaimDiscount;
import com.robinhood.ticker.TickerView;
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

public class ScannerActivity extends AppCompatActivity {

    private final static String INFURA_KEY = "INFURA_KEY_GOES_HERE";
    private final static String NETWORK = "rinkeby";

    private final static int REFRESH_INTERVAL_SECONDS = 30;

    private final Web3j web3 = Web3jFactory.build(new InfuraHttpService("https://" + NETWORK + ".infura.io/" + INFURA_KEY));
    private final ClientTransactionManager readOnlyTransactionManager = new ClientTransactionManager(web3, "0x0000000000000000000000000000000000000000");

    private Button scanQrCodeButton;
    private TickerView entitlementTickerView;
    private TextView unitOfTimeView;
    private NoClaimDiscount contract;

    private String accrualFrom = "";
    private String contractAddress = "";

    protected void onCreate(
        Bundle savedInstanceState
    ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        scanQrCodeButton = (Button) findViewById(R.id.scanQrCode);
        unitOfTimeView = (TextView) findViewById(R.id.unitOfTime);
        entitlementTickerView = (TickerView) findViewById(R.id.entitlementTicker);

        scanQrCodeButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(
                View view
            ) {
                IntentIntegrator integrator = new IntentIntegrator(ScannerActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan QR Code for your policy");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });

        refreshEntitlement();
        startEntitlementMonitor();
    }

    protected void onSaveInstanceState(
        Bundle out
    ) {
        super.onSaveInstanceState(out);

        out.putString("accrualFrom", accrualFrom);
        out.putString("contractAddress", contractAddress);
    }

    protected void onRestoreInstanceState(
        Bundle in
    ) {
        super.onRestoreInstanceState(in);

        accrualFrom = in.getString("accrualFrom");
        contractAddress = in.getString("contractAddress");
    }

    protected void onActivityResult(
        int requestCode,
        int resultCode,
        Intent data
    ) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result == null) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }

        if (result.getContents() != null) {

            contractAddress = result.getContents();

            fetchEntitlement();
            refreshEntitlement();
        }
    }

    private synchronized void fetchEntitlement() {

        if (contract == null
            && contractAddress != "") {

            contract = NoClaimDiscount.load(
                contractAddress,
                web3,
                readOnlyTransactionManager,
                BigInteger.ONE,
                BigInteger.ONE
            );
        }

        if (contract != null) {

            try {

                accrualFrom =
                    contract
                        .accrualFrom()
                        .sendAsync()
                        .get(30000, TimeUnit.MILLISECONDS)
                        .toString();

            } catch (ExecutionException | InterruptedException | TimeoutException e) {
                // noop
            }
        }
    }

    private synchronized void refreshEntitlement() {

        runOnUiThread(new Runnable() {
            public void run() {

                if (!accrualFrom.isEmpty()) {

                    long from = Long.parseLong(accrualFrom) * 1000;
                    long now = System.currentTimeMillis();

                    long secondsDifference = (now - from);

                    Log.w("@@", "from = " + from);
                    Log.w("@@", "now = " + now);
                    Log.w("@@", "secondsDifference = " + secondsDifference);

                    String formattedDifference =
                        (new PrettyTime())
                            .format(new Date(from))
                            .replace(" ago", "")
                            .trim();

                    if (!formattedDifference.contains(" ")) {
                        formattedDifference = "1 minute";
                    }

                    Log.w("@@", "formattedDifference = " + formattedDifference);

                    String unit = formattedDifference.substring(0, formattedDifference.indexOf(" "));
                    String period = formattedDifference.substring(formattedDifference.indexOf(" ") + 1);

                    Log.w("@@", "unit = " + unit);
                    Log.w("@@", "period = " + period);

                    entitlementTickerView.setText(unit);
                    unitOfTimeView.setText(period);
                }
            }
        });
    }

    private void startEntitlementMonitor() {

        (new Thread() {

            public void run() {

                try {

                    while (!isInterrupted()) {

                        fetchEntitlement();
                        refreshEntitlement();

                        Thread.sleep(REFRESH_INTERVAL_SECONDS * 1000);
                    }

                } catch (InterruptedException e) {
                    // noop
                }
            }
        }).start();
    }
}
