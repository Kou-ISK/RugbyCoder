package DataObject;

import java.time.Duration;

public class DataObject {

    public DataObject(String timeCode, String actionName) {
        this.timeCode = timeCode;
        this.videoTime = videoTime;
        this.actionName = actionName;
        this.actionQualifier = actionQualifier;
    }

    private String timeCode;
    private Duration videoTime;
    private String actionName;
    private String actionQualifier;

    public String getTimeCode() {
        return timeCode;
    }

    public void setTimeCode(String timeCode) {
        this.timeCode = timeCode;
    }

    public Duration getVideoTime() {
        return videoTime;
    }

    public void setVideoTime(Duration videoTime) {
        this.videoTime = videoTime;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getActionQualifier() {
        return actionQualifier;
    }

    public void setActionQualifier(String actionQualifier) {
        this.actionQualifier = actionQualifier;
    }


}
