package DataObject;

import java.time.Duration;

public class TimelineDataObject {

    private String startTimeCode;
    private String endTimeCode;
    private Duration videoTime;
    private String actionName;
    private String actionQualifier;
    public TimelineDataObject(String timeCode, String actionName) {
        this.startTimeCode = timeCode;
        this.actionName = actionName;
    }
    public TimelineDataObject(String timeCode, String endTime, String actionName, String actionQualifier) {
        this.startTimeCode = timeCode;
        this.endTimeCode = endTime;
        this.actionName = actionName;
        this.actionQualifier = actionQualifier;
    }
    public TimelineDataObject(String actionName) {
        this.actionName = actionName;
    }

    public String getStartTimeCode() {
        return startTimeCode;
    }

    public void setStartTimeCode(String timeCode) {
        this.startTimeCode = timeCode;
    }

    public String getEndTimeCode() {
        return endTimeCode;
    }

    public void setEndTimeCode(String timeCode) {
        this.endTimeCode = timeCode;
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
