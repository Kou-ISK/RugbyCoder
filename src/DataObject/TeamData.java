package DataObject;

public class TeamData {
    private String Ateam;
    private String Bteam;

    public TeamData(String Ateam, String Bteam) {
        this.Ateam = Ateam;
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