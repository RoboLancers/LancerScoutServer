package main.models.pit;

public class LancerPitBuilder {
    private int teamNumber;
    private Drivetrain drivetrain;
    private CubeIntake cubeIntake;
    private Climb climb;
    private int robotWeight;
    private ProgrammingLanguage programmingLanguage;

    public LancerPitBuilder setTeamNumber(int teamNumber) {
        this.teamNumber = teamNumber;
        return this;
    }

    public LancerPitBuilder setDrivetrain(Drivetrain drivetrain) {
        this.drivetrain = drivetrain;
        return this;
    }

    public LancerPitBuilder setCubeIntake(CubeIntake cubeIntake) {
        this.cubeIntake = cubeIntake;
        return this;
    }

    public LancerPitBuilder setClimb(Climb climb) {
        this.climb = climb;
        return this;
    }

    public LancerPitBuilder setRobotWeight(int robotWeight) {
        this.robotWeight = robotWeight;
        return this;
    }

    public LancerPitBuilder setProgrammingLanguage(ProgrammingLanguage programmingLanguage) {
        this.programmingLanguage = programmingLanguage;
        return this;
    }

    public LancerPit createLancerPit() {
        return new LancerPit(teamNumber, drivetrain, cubeIntake, climb, robotWeight, programmingLanguage);
    }
}
