package main.models.pit;

public class LancerPit {

    private int teamNumber;
    private Drivetrain drivetrain;
    private CubeIntake cubeIntake;
    private Climb climb;
    private int robotWeight;
    private ProgrammingLanguage programmingLanguage;

    public LancerPit(int teamNumber, Drivetrain drivetrain, CubeIntake cubeIntake, Climb climb, int robotWeight, ProgrammingLanguage programmingLanguage) {
        this.teamNumber = teamNumber;
        this.drivetrain = drivetrain;
        this.cubeIntake = cubeIntake;
        this.climb = climb;
        this.robotWeight = robotWeight;
        this.programmingLanguage = programmingLanguage;
    }

    public int getTeamNumber() {
        return teamNumber;
    }

    public Drivetrain getDrivetrain() {
        return drivetrain;
    }

    public CubeIntake getCubeIntake() {
        return cubeIntake;
    }

    public Climb getClimb() {
        return climb;
    }

    public int getRobotWeight() {
        return robotWeight;
    }

    public ProgrammingLanguage getProgrammingLanguage() {
        return programmingLanguage;
    }
}
