package DataObject;

import java.time.Duration;

public class DataObject {

    public DataObject(String timeCode, String actionName) {
        this.startTimeCode = timeCode;
        this.videoTime = videoTime;
        this.actionName = actionName;
        this.actionQualifier = actionQualifier;
    }

    public DataObject(String actionName) {
        this.actionName = actionName;
    }

    private String startTimeCode;
    private String endTimeCode;
    private Duration videoTime;
    private String actionName;
    private String actionQualifier;

    public String getStartTimeCode() {
        return startTimeCode;
    }

    public String getEndTimeCode() {
        return endTimeCode;
    }

    public void setEndTimeCode(String timeCode) {
        this.endTimeCode = timeCode;
    }

    public void setStartTimeCode(String timeCode) {
        this.startTimeCode = timeCode;
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
