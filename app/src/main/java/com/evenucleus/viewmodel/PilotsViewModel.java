package com.evenucleus.viewmodel;

import org.robobinding.annotation.PresentationModel;
import org.robobinding.presentationmodel.HasPresentationModelChangeSupport;
import org.robobinding.presentationmodel.PresentationModelChangeSupport;

/**
 * Created by tomeks on 2015-01-03.
 */
@PresentationModel
public class PilotsViewModel implements HasPresentationModelChangeSupport {
    private PresentationModelChangeSupport changeSupport;

    public PilotsViewModel(){
        changeSupport = new PresentationModelChangeSupport(this);
    }


    public String getTitle() {
        return "ala ma kota";
    }

    @Override
    public PresentationModelChangeSupport getPresentationModelChangeSupport() {
        return changeSupport;
    }
}
