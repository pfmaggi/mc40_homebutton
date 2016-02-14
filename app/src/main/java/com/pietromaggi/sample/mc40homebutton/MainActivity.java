package com.pietromaggi.sample.mc40homebutton;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String KEY_STATE = "key_state";
    private static final int STATE_DISABLED = 0;
    private static final int STATE_ENABLED = 1;
    private static final String MANUFACTURER_ZEBRA = "Zebra Technologies";
    private static final String MANUFACTURER_MSI = "Motorola Solutions";
    private static final String MODEL_MC40 = "MC40N0";
    private static final String INTENT_ZEBRA = "com.symbol.intent.action.HOMEKEY_MODE";
    private static final String INTENT_MSI = "com.motorolasolutions.intent.action.HOMEKEY_MODE";
    private static final String INTENT_EXTRA = "state";
    private String mStrIntent;
    private TextView mTvwStatus;
    private int mState = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String StrManufacturer = Build.MANUFACTURER;
        String strModel = Build.MODEL;
        boolean bSupported = true;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvwStatus = (TextView) findViewById(R.id.tvwHomeBtnStatus);

        if (strModel.equalsIgnoreCase(MODEL_MC40)) {
            // We're running on an MC40
            if (StrManufacturer.equalsIgnoreCase(MANUFACTURER_ZEBRA)) {
                // This is a Zebra Device
                mStrIntent = INTENT_ZEBRA;
            } else if (StrManufacturer.equalsIgnoreCase(MANUFACTURER_MSI)) {
                // This is a Motorola Solution Device
                // just double OS version, only Jelly Bean is supported
                mStrIntent = INTENT_MSI;
                if (Build.VERSION_CODES.JELLY_BEAN != Build.VERSION.SDK_INT) {
                    bSupported = false;
                }

            }
        } else {
            // This is something else, not supported
            bSupported = false;
        }

        if (savedInstanceState != null) {
            mState = savedInstanceState.getInt(KEY_STATE, STATE_DISABLED);
        }
        Button btnToggle = (Button)findViewById(R.id.btnToggleStatus);
        if (bSupported) {
            if (STATE_DISABLED == mState) {
                mTvwStatus.setText(R.string.home_btn_status_off);
            } else {
                mTvwStatus.setText(R.string.home_btn_status_on);
            }
            Intent i = new Intent(mStrIntent);
            i.putExtra(INTENT_EXTRA, mState);
            sendBroadcast(i);

            btnToggle.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 //Toggle Current Status
                                                 if (STATE_DISABLED == mState) {
                                                     mState = STATE_ENABLED;
                                                     mTvwStatus.setText(R.string.home_btn_status_on);
                                                 } else {
                                                     mState = STATE_DISABLED;
                                                     mTvwStatus.setText(R.string.home_btn_status_off);
                                                 }
                                                 Intent i = new Intent(mStrIntent);
                                                 i.putExtra(INTENT_EXTRA, mState);
                                                 sendBroadcast(i);

                                             }
                                         }
            );
        } else {
            mTvwStatus.setText(R.string.device_not_supported);
            btnToggle.setEnabled(false);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(KEY_STATE, mState);
    }
}
