package main.models.match;

public class LancerTeam {
    private int teamNumber;

    public LancerTeam(int teamNumber){
        this.teamNumber = teamNumber;
    }

    public int getTeamNumber() {
        return teamNumber;
    }

    @Override
    public String toString(){
        return String.valueOf(teamNumber);
    }

}
