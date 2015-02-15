package com.evenucleus.evenucleus;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.evenucleus.client.CategoryRepo;
import com.evenucleus.client.ICategoryRepo;
import com.evenucleus.client.ISettingsRepo;
import com.evenucleus.client.SettingsRepo;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


@EActivity(R.layout.activity_financials_settings)
public class FinancialsSettingsActivity extends MyActivityBase {
    public static final String NOFILTER = "no filter";
    public static final String NOCATEGORY = "no category";

    List<CharSequence> _values;

    @ViewById(R.id.laterThan)
    Button laterThan;
    Date laterThanValue;

    @ViewById(R.id.bycategory)
    Spinner byCategory;

    @ViewById(R.id.onlysuggested)
    CheckBox onlySuggested;

    @Bean(SettingsRepo.class)
    ISettingsRepo _settingsRepo;

    @Bean(CategoryRepo.class)
    ICategoryRepo _categoryRepo;

    @AfterViews
    public void afterViews() {
        try {
            laterThanValue = _settingsRepo.getFinancialsLaterThan();
            if (laterThanValue == null)
                laterThan.setText("None");
            else
                laterThan.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(laterThanValue));

            _values = new ArrayList<CharSequence>();
            _values.add(NOFILTER);
            _values.add(NOCATEGORY);
            for(String s: _categoryRepo.Get())
                _values.add(s);

            ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this,  android.R.layout.simple_spinner_item, _values);
            byCategory.setAdapter(adapter);

            String filterby = _settingsRepo.getFilterBy();
            if (filterby == null || !_values.contains(filterby))
                filterby = NOFILTER;

            int spinnerPos = adapter.getPosition(filterby);
            byCategory.setSelection(spinnerPos);

            byCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        _settingsRepo.setFilterBy(parent.getItemAtPosition(position).toString());
                    } catch (Exception e) {
                        new AlertDialog.Builder(getApplicationContext())
                                .setTitle("Error")
                                .setMessage(e.toString())
                                .show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            onlySuggested.setChecked(_settingsRepo.getOnlySuggested());
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

    @CheckedChange(R.id.onlysuggested)
    void onOnlySuggestedChange(CompoundButton hello, boolean isChecked) {
        try {
            _settingsRepo.setOnlySuggested(isChecked);
        } catch (Exception e) {
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage(e.toString())
                    .show();
        }
    }
}
