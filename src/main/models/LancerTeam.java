package main.models;

import java.util.Objects;

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

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }

        if (this.getClass() != other.getClass()) {
            return false;
        }

        return this.teamNumber == ((LancerTeam) other).teamNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamNumber);
    }
}
