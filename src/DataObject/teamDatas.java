package DataObject;

public class teamDatas {
    private String Ateam;
    private String Bteam;

    public teamDatas(String Ateam, String Bteam) {
        this.Ateam = Ateam;
        this.Bteam = Bteam;
    }

    public void setAteam(String Ateam) {
        this.Ateam = Ateam;
    }

    public void setBteam(String Bteam) {
        this.Bteam = Bteam;
    }

    public String getAteam() {
        if (Ateam == null) {
            return "A TEAM";
        }
        return Ateam;
    }

    public String getBteam() {
        if (Bteam == null) {
            return "B TEAM";
        }
        return Bteam;
    }
}