package one.telefon.tele.dto;

import com.facebook.react.bridge.ReadableMap;
import com.google.gson.Gson;

public class CallSettingsDTO {
    //private Integer audioCount;
    private Integer flag;

    /*
    public Integer getAudioCount() {
        return audioCount;
    }
    */


    public Integer getFlag() {
        return flag;
    }

    /*
    public void setAudioCount(Integer audioCount) {
        this.audioCount = audioCount;
    }
    */


    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String toJson () {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static CallSettingsDTO fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, CallSettingsDTO.class);
    }

    public static CallSettingsDTO fromReadableMap(ReadableMap data) {
        CallSettingsDTO result = new CallSettingsDTO();
/*
        if (data.hasKey("audioCount")) {
            result.setAudioCount(data.getInt("audioCount"));
        }
        */
        if (data.hasKey("flag")) {
            result.setFlag(data.getInt("flag"));
        }

        return result;
    }

}
