package Logic;

import DataObject.DataObject;

import java.time.Duration;

public class Logic {
    public void setTime(String timeCode, Duration videoTime, String actionName, String actionQualifier) {
        DataObject dataObject = new DataObject(timeCode, videoTime, actionName, actionQualifier);
   xmlWriter(dataObject);
    }

//    XML形式で書き出し
    public void xmlWriter(DataObject dataObject){
        String timeCode = dataObject.getTimeCode();
        String action = dataObject.getActionName();
        String qualifier = dataObject.getActionQualifier();
    }
}
