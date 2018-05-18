package main.models;

public class LancerTeam {

    private String teamName;
    private int teamNumber;

    public LancerTeam(String teamName, int teamNumber){
        this.teamName = teamName;
        this.teamNumber = teamNumber;
    }

    public LancerTeam(int teamNumber){
        this.teamName = "";
        this.teamNumber = teamNumber;
    }

    public String getTeamName() {
        return teamName;
    }

    public int getTeamNumber() {
        return teamNumber;
    }

    @Override
    public String toString(){
        return teamName + " " + teamNumber;
    }
}
