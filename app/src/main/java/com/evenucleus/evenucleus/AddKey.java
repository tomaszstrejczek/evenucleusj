package com.evenucleus.evenucleus;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.beimin.eveapi.exception.ApiException;
import com.evenucleus.client.Configuration;
import com.evenucleus.client.EveApiCaller;
import com.evenucleus.client.IEveApiCaller;
import com.evenucleus.client.IKeyInfoRepo;
import com.evenucleus.client.IPilotRepo;
import com.evenucleus.client.KeyInfoRepo;
import com.evenucleus.client.Pilot;
import com.evenucleus.client.PilotRepo;
import com.evenucleus.client.UserException;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


@EActivity(R.layout.activity_add_key)
public class AddKey extends ActionBarActivity {
    final Logger logger = LoggerFactory.getLogger(AddKey.class);

    @Bean(EveApiCaller.class)
    IEveApiCaller eveApiCaller;

    @Bean(PilotRepo.class)
    IPilotRepo pilotRepo;

    @Bean(KeyInfoRepo.class)
    IKeyInfoRepo keyInfoRepo;

    @ViewById
    EditText KeyEdit;

    @ViewById
    EditText VCodeEdit;

    @ViewById
    Button OkButton;

    @TextChange({R.id.KeyEdit, R.id.VCodeEdit})
    void OnEditChange() {
        boolean validData = false;
        try {
            validData = KeyEdit.getText().length()>0 && VCodeEdit.getText().length()>0 && Integer.parseInt(KeyEdit.getText().toString())>0;
        }
        catch (Exception e) {}
        OkButton.setEnabled(validData);
    }

    @Click(R.id.OkButton)
    void OnOkButton() {
        ProgressDialog dlg = new ProgressDialog(this);
        dlg.setTitle("Adding key");
        dlg.setMessage("Validating...");
        dlg.show();

        addKeyApi(dlg);
    }

    @Background
    void addKeyApi(ProgressDialog dlg) {
        try {
            int keyid = Integer.parseInt(KeyEdit.getText().toString());
            String vcode = VCodeEdit.getText().toString();
            boolean result = eveApiCaller.CheckKey(keyid, vcode);
            if (result) {
                keyInfoRepo.AddKey(keyid, vcode);
                pilotRepo.SimpleUpdateFromKey(keyid, vcode);
            }

            List<Pilot> ps = pilotRepo.GetAll();

            hideProgress(dlg);
            if (!result)
                displayError("Invalid key");

            // all done - may return to parent activity
            OnCancelButton();
        }
        catch (Exception e) {
            hideProgress(dlg);
            displayError(e.getMessage());
        }
    }

    @UiThread
    void displayError(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .show();
    }


    @UiThread
    void hideProgress(ProgressDialog dlg) {
        dlg.dismiss();
    }

    @Click(R.id.CancelButton)
    void OnCancelButton() {
        KeyManagementActivity_.intent(this).start();
        //NavUtils.navigateUpFromSameTask(this);
    }

    @Click(R.id.InstallButton)
    void OnInstallButton() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://support.eveonline.com/api/Key/ActivateInstallLinks"));
        startActivity(browserIntent);
    }

    @Click(R.id.CreateButton)
    void OnCreateButton() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://community.eveonline.com/support/api-key/CreatePredefined?accessMask=23527561"));
        startActivity(browserIntent);
    }

    private static int _testData = 0;
    @AfterViews
    void afterUpdate() {
        logger.debug("afterupdate");

        OkButton.setEnabled(false);

        if (Configuration.ProvideSampleData())
        {
            OkButton.setEnabled(true);
            if (_testData == 0)
            {
                KeyEdit.setText("3692329");
                VCodeEdit.setText("aPQOKWEr0r9bp7yVNVgtx9O9xSPDOgTEXY9FhM93ArndOcE3ZTTV1xGnTDHDoeii");
                ++_testData;
            }
            else
            if (_testData == 1)
            {
                KeyEdit.setText("3231405");
                VCodeEdit.setText("UZDkcXJAQYdDXu8ItoX7ICXT914ephxHX2n07CFjgKwkYhP2XE6PerFGzTWYfgL6");
                ++_testData;
            }
            else
            {
                KeyEdit.setText("2812727");
                VCodeEdit.setText("Qw5OES3cKXnLh14qLNJ1BqWa2YvbowvR5lMtHgG3wkqnExToTsraIURLHApLlavC");
            }
        }

        Intent intent = getIntent();
        if (intent!=null && intent.getAction() != null && intent.getAction().equals(Intent.ACTION_VIEW)) {
            Uri uri = intent.getData();
            if (uri.getScheme().equals("eve")) {
                String keyID = uri.getQueryParameter("keyID");
                String vcode = uri.getQueryParameter("vCode");
                if (keyID!=null && vcode != null && !keyID.isEmpty() && !vcode.isEmpty()) {
                    KeyEdit.setText(keyID);
                    VCodeEdit.setText(vcode);
                    OnOkButton();
                }
            }
        }
    }
}
