package UnitedClasses;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    public int width;
    public int height;
    public List<Character> characters;
    public List<Coordinate> borders;
    public List<Coordinate> escapes;
    public List<Coordinate> deathTraps;

    public GameState() {
        this.width = -1;
        this.height = -1;
        this.characters = new ArrayList<>();
        this.borders = new ArrayList<>();
        this.escapes = new ArrayList<>();
        this.deathTraps = new ArrayList<>();
    }

    public GameState(GameState o) {
        this.width = o.width;
        this.height = o.height;

        this.characters = new ArrayList<>();
        for(Character c : o.characters){
            this.characters.add(new Character(c));
        }

        this.borders = new ArrayList<>();
        for(Coordinate c : o.borders){
            this.borders.add(new Coordinate(c.x, c.y));
        }

        this.escapes = new ArrayList<>();
        for(Coordinate c : o.escapes){
            this.escapes.add(new Coordinate(c.x, c.y));
        }

        this.deathTraps = new ArrayList<>();
        for(Coordinate c : o.deathTraps){
            this.deathTraps.add(new Coordinate(c.x, c.y));
        }

    }
}
