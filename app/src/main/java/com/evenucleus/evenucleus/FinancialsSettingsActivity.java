package com.evenucleus.evenucleus;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;

import com.evenucleus.client.ISettingsRepo;
import com.evenucleus.client.SettingsRepo;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


@EActivity(R.layout.activity_financials_settings)
public class FinancialsSettingsActivity extends ActionBarActivity {
    @ViewById(R.id.laterThan)
    Button laterThan;
    Date laterThanValue;

    @Bean(SettingsRepo.class)
    ISettingsRepo _settingsRepo;

    @AfterViews
    public void afterViews() {
        try {
            laterThanValue = _settingsRepo.getFinancialsLaterThan();
            if (laterThanValue == null)
                laterThan.setText("None");
            else
                laterThan.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(laterThanValue));
        }
        catch (Exception e) {
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage(e.toString())
                    .show();
        }
    }

    @Click(R.id.laterThan)
    public void onLaterThan() {
        Calendar cal = new GregorianCalendar();
        if (laterThanValue != null)
            cal.setTime(laterThanValue);
        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox
                        Calendar cal = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                        laterThanValue = cal.getTime();

                        laterThan.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(laterThanValue));
                        try {
                            _settingsRepo.setFinancialsLaterThan(laterThanValue);
                        }
                        catch (Exception e) {
                            new AlertDialog.Builder(getApplicationContext())
                                    .setTitle("Error")
                                    .setMessage(e.toString())
                                    .show();
                        }
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        dpd.show();
    }
}
