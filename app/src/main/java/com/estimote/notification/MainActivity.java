package com.estimote.notification;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.estimote.mustard.rx_goodness.rx_requirements_wizard.Requirement;
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory;
import com.estimote.notification.estimote.CommonVars;
import com.estimote.notification.estimote.NotificationActivity;

import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

//
// Running into any issues? Drop us an email to: contact@estimote.com
//

public class MainActivity extends AppCompatActivity {

    public void setButtons() {
        boolean carted = CommonVars.sharedPreferences.getBoolean("cart", false);
        TextView cartText = (TextView)findViewById(R.id.textView3);
        Button cartButton = (Button)findViewById(R.id.ticket);
        if (carted) {
            cartText.setText("carted");
            cartButton.setEnabled(true);
        } else {
            cartText.setText("not carted");
            cartButton.setEnabled(false);
        }
        TextView pclText = findViewById(R.id.textView2);
        String pcl = CommonVars.sharedPreferences.getString("pcl", "none");
        pclText.setText(pcl);
    }

    public void setDefault() {
        CommonVars.sharedPreferences.edit().putBoolean("cart", false).putString("pcl", "none").commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CommonVars.sharedPreferences = getPreferences(MODE_PRIVATE);
        setButtons();

        final MyApplication application = (MyApplication) getApplication();

        RequirementsWizardFactory
                .createEstimoteRequirementsWizard()
                .fulfillRequirements(this,
                        new Function0<Unit>() {
                            @Override
                            public Unit invoke() {
                                Log.d("app", "requirements fulfilled");
                                application.enableBeaconNotifications();
                                return null;
                            }
                        },
                        new Function1<List<? extends Requirement>, Unit>() {
                            @Override
                            public Unit invoke(List<? extends Requirement> requirements) {
                                Log.e("app", "requirements missing: " + requirements);
                                return null;
                            }
                        },
                        new Function1<Throwable, Unit>() {
                            @Override
                            public Unit invoke(Throwable throwable) {
                                Log.e("app", "requirements error: " + throwable);
                                return null;
                            }
                        });
    }

    public void onBuyTicket(View v) {
        Intent intent = new Intent(this, NotificationActivity.class);
        startActivity(intent);
    }

    public void onPclAdd(View v) {
        String pcl = CommonVars.sharedPreferences.getString("pcl", "none");
        switch (pcl) {
            case "none":
                pcl = "expired";
                break;
            case "expired":
                pcl = "active";
                break;
            case "active":
                pcl = "none";
                break;
        }
        CommonVars.sharedPreferences.edit().putString("pcl", pcl).commit();
        setButtons();
    }

    public void onAddCart(View v) {
        boolean carted = CommonVars.sharedPreferences.getBoolean("cart", false);
        CommonVars.sharedPreferences.edit().putBoolean("cart", !carted).commit();
        setButtons();

    }

}
