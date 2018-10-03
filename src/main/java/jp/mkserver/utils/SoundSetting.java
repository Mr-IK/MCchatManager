package jp.mkserver.utils;

import java.io.Serializable;

public class SoundSetting implements Serializable {

    private float vol;
    private double vold;
    private boolean on;

    public SoundSetting(float vol,boolean on,double vold){
        this.vol = vol;
        this.on = on;
        this.vold = vold;
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public float getVol() {
        return vol;
    }

    public void setVol(float vol) {
        this.vol = vol;
    }

    public double getVold() {
        return vold;
    }

    public void setVold(double vold) {
        this.vold = vold;
    }
}
