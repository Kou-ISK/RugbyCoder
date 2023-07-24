package DataObject;

public class TimelineDataObject {

    private String startTimeCode;
    private String endTimeCode;
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

    public String getStartTimeCode() {
        return startTimeCode;
    }

    public String getEndTimeCode() {
        return endTimeCode;
    }

    public void setEndTimeCode(String timeCode) {
        this.endTimeCode = timeCode;
    }

    public String getActionName() {
        return actionName;
    }

    public String getActionQualifier() {
        return actionQualifier;
    }

    public void setActionQualifier(String actionQualifier) {
        this.actionQualifier = actionQualifier;
    }


}
