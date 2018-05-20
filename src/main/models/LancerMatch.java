package main.models;

public class LancerMatch {

    private int matchNumber;
    private int teamNumber;
    private AllianceColor color;
    private StartingConfiguration startingConfiguration;

    private boolean crossedAutoLine;
    private AutonomousAttempt autonomousAttempt;
    private boolean wrongSideAuto;

    private int allianceSwitch;
    private int centerScale;
    private int opponentSwitch;
    private int exchange;

    private EndGameAttempt endGameAttempt;
    private boolean brokeDown;

    private String comment;

    public LancerMatch(int matchNumber, int teamNumber, AllianceColor color, StartingConfiguration startingConfiguration, boolean crossedAutoLine, AutonomousAttempt autonomousAttempt, boolean wrongSideAuto, int allianceSwitch, int centerScale, int opponentSwitch, int exchange, EndGameAttempt endGameAttempt, boolean brokeDown, String comment) {
        this.matchNumber = matchNumber;
        this.teamNumber = teamNumber;
        this.color = color;
        this.startingConfiguration = startingConfiguration;
        this.crossedAutoLine = crossedAutoLine;
        this.autonomousAttempt = autonomousAttempt;
        this.wrongSideAuto = wrongSideAuto;
        this.allianceSwitch = allianceSwitch;
        this.centerScale = centerScale;
        this.opponentSwitch = opponentSwitch;
        this.exchange = exchange;
        this.endGameAttempt = endGameAttempt;
        this.brokeDown = brokeDown;
        this.comment = comment;
    }

    public int getMatchNumber() {
        return matchNumber;
    }

    public int getTeamNumber() {
        return teamNumber;
    }

    public AllianceColor getColor() {
        return color;
    }

    public StartingConfiguration getStartingConfiguration() {
        return startingConfiguration;
    }

    public boolean isCrossedAutoLine() {
        return crossedAutoLine;
    }

    public AutonomousAttempt getAutonomousAttempt() {
        return autonomousAttempt;
    }

    public boolean isWrongSideAuto() {
        return wrongSideAuto;
    }

    public int getAllianceSwitch() {
        return allianceSwitch;
    }

    public int getCenterScale() {
        return centerScale;
    }

    public int getOpponentSwitch() {
        return opponentSwitch;
    }

    public int getExchange() {
        return exchange;
    }

    public EndGameAttempt getEndGameAttempt() {
        return endGameAttempt;
    }

    public boolean isBrokeDown() {
        return brokeDown;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public String toString(){
        return "Match " + getMatchNumber();
    }
}
