package com.evenucleus.evenucleus;

import android.app.ProgressDialog;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.evenucleus.client.Configuration;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;


@EActivity(R.layout.activity_add_key)
public class AddKey extends ActionBarActivity {

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
            validData = KeyEdit.getText().length()>0 && VCodeEdit.getText().length()>0 && Long.parseLong(KeyEdit.getText().toString())>0;
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
            Thread.sleep(3000,0);
        }
        catch (Exception e) {
        }
        hideProgress(dlg);
    }

    @UiThread
    void hideProgress(ProgressDialog dlg) {
        dlg.dismiss();
    }

    @Click(R.id.CancelButton)
    void OnCancelButton() {
        NavUtils.navigateUpFromSameTask(this);
    }


    private static int _testData = 0;
    @AfterViews
    void afterUpdate() {
        Log.d(AddKey.class.getName(), "afterupdate");
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
    }
}
