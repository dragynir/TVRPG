package UnitedClasses;

public class Character {
    public Coordinate position;
    public boolean isAlive;

    public Character(Character o){
        this.position = new Coordinate(o.position.x, o.position.y);
        this.isAlive = o.isAlive;
    }

    public Character() {
        this.position = new Coordinate(0, 0);
        this.isAlive = true;
    }
}