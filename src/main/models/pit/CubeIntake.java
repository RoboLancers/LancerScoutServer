package main.models.pit;

public enum CubeIntake {

    FLOOR_INTAKE ("Floor Intake"),
    HUMAN_INTAKE ("Portal/Exchange Intake"),
    BOTH_INTAKES ("Both Intakes"),
    NONE_INTAKE ("None");

    private final String name;

    CubeIntake(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
