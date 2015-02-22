package com.evenucleus.evenucleus;

import android.app.AlertDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.evenucleus.client.ISettingsRepo;
import com.evenucleus.client.SettingsRepo;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;

@EActivity(R.layout.activity_settings)
public class SettingsActivity extends MyActivityBase {
    @Bean(SettingsRepo.class)
    ISettingsRepo _settingsRepo;

    @ViewById(R.id.SynchronizedOn)
    TextView SynchronizedOn;

    @ViewById(R.id.NextSynchronization)
    TextView NextSynchronization;

    @ViewById(R.id.freq1h)
    TextView Freq1h;
    @ViewById(R.id.freq2h)
    TextView Freq2h;
    @ViewById(R.id.freq4h)
    TextView Freq4h;
    @ViewById(R.id.freq12h)
    TextView Freq12h;
    @ViewById(R.id.freq24h)
    TextView Freq24h;
    @ViewById(R.id.freqnever)
    TextView FreqNever;

    @AfterViews
    void afterInject() {
        try {
            Date d = _settingsRepo.getLatestAlert();
            SynchronizedOn.setText(DateFormat.getDateTimeInstance().format(d));

            d = _settingsRepo.getNextAlert();
            NextSynchronization.setText(DateFormat.getDateTimeInstance().format(d));

            int freq = _settingsRepo.getFrequencyinMinutes();
            updateStylesFromFrequency(freq);
        }
        catch (SQLException e) {
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage(e.toString())
                    .show();
        }
    }

    void updateStylesFromFrequency(int freq) {
        Freq1h.setTextAppearance(getBaseContext(), freq==60?R.style.FrequencyBoxSelected:R.style.FrequencyBox);
        Freq1h.setBackground(getResources().getDrawable(freq==60?R.drawable.background_frequency_selected:R.drawable.background_frequency));

        Freq2h.setTextAppearance(getBaseContext(), freq==120?R.style.FrequencyBoxSelected:R.style.FrequencyBox);
        Freq2h.setBackground(getResources().getDrawable(freq == 120 ? R.drawable.background_frequency_selected : R.drawable.background_frequency));

        Freq4h.setTextAppearance(getBaseContext(), freq==240?R.style.FrequencyBoxSelected:R.style.FrequencyBox);
        Freq4h.setBackground(getResources().getDrawable(freq == 240 ? R.drawable.background_frequency_selected : R.drawable.background_frequency));

        Freq12h.setTextAppearance(getBaseContext(), freq==12*60?R.style.FrequencyBoxSelected:R.style.FrequencyBox);
        Freq12h.setBackground(getResources().getDrawable(freq == 12 * 60 ? R.drawable.background_frequency_selected : R.drawable.background_frequency));

        Freq24h.setTextAppearance(getBaseContext(), freq==24*60?R.style.FrequencyBoxSelected:R.style.FrequencyBox);
        Freq24h.setBackground(getResources().getDrawable(freq == 24 * 60 ? R.drawable.background_frequency_selected : R.drawable.background_frequency));

        FreqNever.setTextAppearance(getBaseContext(), freq==0?R.style.FrequencyBoxSelected:R.style.FrequencyBox);
        FreqNever.setBackground(getResources().getDrawable(freq == 0 ? R.drawable.background_frequency_selected : R.drawable.background_frequency));

    }

    public void setFrequency(int minutes) {
        try {
            _settingsRepo.setFrequencyinMinutes(minutes);
            updateStylesFromFrequency(minutes);
        }
        catch (SQLException e) {
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage(e.toString())
                    .show();
        }
    }

    @Click(R.id.freq1h)
    void clickFreq1h(){
        setFrequency(60);
    }
    @Click(R.id.freq2h)
    void clickFreq2h(){
        setFrequency(120);
    }
    @Click(R.id.freq4h)
    void clickFreq4h(){
        setFrequency(4*60);
    }
    @Click(R.id.freq12h)
    void clickFreq12h(){
        setFrequency(12*60);
    }
    @Click(R.id.freq24h)
    void clickFreq24h(){
        setFrequency(24*60);
    }
    @Click(R.id.freqnever)
    void clickFreqNever(){
        setFrequency(0);
    }
}
