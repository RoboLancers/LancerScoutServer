package main.models.pit;

public enum ProgrammingLanguage {
    JAVA ("Java"),
    C_PLUS_PLUS ("C++"),
    LABVIEW ("Labview"),
    OTHER ("Other");

    private final String name;

    ProgrammingLanguage(String name){
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
