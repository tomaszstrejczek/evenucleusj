package com.evenucleus.evenucleus;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evenucleus.client.Pilot;
import com.koushikdutta.ion.Ion;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by tomeks on 2015-01-04.
 */
@EViewGroup(R.layout.pilot_list_row)
public class PilotItemView extends LinearLayout {
    @ViewById
    TextView pilotName;
    @ViewById
    TextView skillInTraining;
    @ViewById
    ImageView pilotImage;
    @ViewById
    TextView currentTrainingDuration;
    @ViewById
    TextView queueLength;

    Context _context;

    public PilotItemView(Context context) {
        super(context);
        _context = context;
    }

    public void bind(Pilot pilot) {
        Ion.with(pilotImage)
           .placeholder(R.drawable.person_placeholder_icon)
           .error(R.drawable.noimage)
           .load(pilot.getUrl());

        pilotName.setText(pilot.Name);

        // Not all data may be retrieved
        if (pilot.CurrentTrainingNameAndLevel == null) {
            skillInTraining.setText("");
            currentTrainingDuration.setText("");
            queueLength.setText("");
            return;
        }
        skillInTraining.setText(pilot.CurrentTrainingNameAndLevel);

        // currentTrainingDuration
        DateTime now = new DateTime();
        DateTime end = new DateTime(pilot.CurrentTrainingEnd);
        Period per = new Period(now, end, PeriodType.dayTime());
        if (!pilot.TrainingActive)
        {
            currentTrainingDuration.setText("Not training");
            currentTrainingDuration.setTextColor(Color.rgb(255, 127, 39));
        }
        else
        {
            if (!end.isAfter(now))
            {
                currentTrainingDuration.setText("finished");
                currentTrainingDuration.setTextColor(Color.RED);
            }
            else
            {
                currentTrainingDuration.setTextColor(Color.parseColor("#10bcc9"));
                if (per.getDays() > 0)
                    currentTrainingDuration.setText(String.format("%dd %dh %dm", per.getDays(), per.getHours(), per.getMinutes()));
                else
                    currentTrainingDuration.setText(String.format("%dh %dm", per.getHours(), per.getMinutes()));
            }

        }

        // queue Length
        end = new DateTime(pilot.TrainingQueueEnd);
        per = new Period(now, end, PeriodType.dayTime());
        if (!end.isAfter(now))
            currentTrainingDuration.setText("finished");
        else
        {
            if (per.getDays() > 0)
                queueLength.setText(String.format("Queue length: %dd %dh %dm", per.getDays(), per.getHours(), per.getMinutes()));
            else
                queueLength.setText(String.format("Queue length: %dh %dm", per.getHours(), per.getMinutes()));
        }

    }

}
