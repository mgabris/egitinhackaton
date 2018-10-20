package com.estimote.notification.estimote;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.estimote.mustard.rx_goodness.rx_requirements_wizard.Requirement;
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory;
import com.estimote.notification.MyApplication;
import com.estimote.notification.R;


import android.content.Intent;

import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
    }

    public void buyPclOnClick(View v) {
        Log.d("Clooney", "Buy PCL Click");

        CommonVars.pclValidTimestampMs = System.currentTimeMillis();
        finishAndRemoveTask();
    }

    public void buyClOnClick(View v) {
        Log.d("Clooney", "Buy CL Click");

        CommonVars.clValidTimestampMs = System.currentTimeMillis();
        finishAndRemoveTask();
    }

    public void ignoreOnClick(View v) {
        Log.d("Clooney", "Ignore Click");

        finishAndRemoveTask();
    }
}
