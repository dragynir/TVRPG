package UnitedClasses;

public class Character {
    public Coordinate position;
    public boolean isAlive;
    public Character() {
        this.position = new Coordinate(0, 0);
        this.isAlive = true;
    }
}