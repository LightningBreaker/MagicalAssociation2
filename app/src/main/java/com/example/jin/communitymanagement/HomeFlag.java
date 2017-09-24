package com.example.jin.communitymanagement;

/**
 * Created by summe on 2017/8/23.
 */

public class HomeFlag {
    private Boolean isChoosed;
    private String flagName;

    public Boolean getChoosed() {
        return isChoosed;
    }

    public void setChoosed(Boolean choosed) {
        isChoosed = choosed;
    }

    public String getFlagName() {
        return flagName;
    }

    public void setFlagName(String flagName) {
        this.flagName = flagName;
    }

    public HomeFlag(Boolean isChoosed, String flagName) {
        this.isChoosed = isChoosed;
        this.flagName = flagName;
    }
}
