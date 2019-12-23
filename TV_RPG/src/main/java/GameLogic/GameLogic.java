package GameLogic;

import Instructions.Instruction;
import UnitedClasses.*;
import UnitedClasses.Character;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class GameLogic {
    private final GameState startGameState;
    private GameState gameState;
    private char[][] field;
    private Character character;
    private GameResult gameResult;

    public GameLogic(int levelNumber) throws IOException {
        this.startGameState = new GameState();
        gameState = new GameState();
        loadLevel(levelNumber);
        this.gameResult = new GameResult();
        this.field = new char[startGameState.width][startGameState.height];
    }
    public void restart() {
        gameState = new GameState(startGameState);
        resetField();
        this.character = gameState.characters.get(0);
        setCharacterStartPosition();
        gameResult.isWin = false;
    }
    private void resetField() {
        for(int i = 0; i < gameState.width; ++i) {
            Arrays.fill(field[i], ' ');
        }
        for(Coordinate coordinate : gameState.borders) {
            field[coordinate.x][coordinate.y] = '*';
        }
        for(Coordinate coordinate : gameState.escapes) {
            field[coordinate.x][coordinate.y] = 'O';
        }
        for(Coordinate coordinate : gameState.deathTraps) {
            field[coordinate.x][coordinate.y] = '^';
        }
    }
    private void setCharacterStartPosition() {
        this.character.position.x = startGameState.characters.get(0).position.x;
        this.character.position.y = startGameState.characters.get(0).position.y;
    }
    public GameState executeInstruction(Instruction instruction) {
        if(!character.isAlive || gameResult.isWin) {
            return new GameState(gameState);
        }
        switch (instruction.instructionId) {
            case 0: {
                moveLeft();
                break;
            }
            case 1: {
                moveRight();
                break;
            }
            case 2: {
                moveUp();
                break;
            }
            case 3: {
                moveDown();
                break;
            }
            default: {
                new Exception("Game Logic: Unknown instruction").printStackTrace();
            }
        }
        return new GameState(gameState);
    }
    public boolean isEnd() {
        return !character.isAlive || gameResult.isWin;
    }
    public GameResult finish() {
        GameResult out = new GameResult();
        out.isWin = gameResult.isWin;
        return out;
    }
    private void loadLevel(int levelNumber) throws IOException {

        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines( Paths.get(R_PATH.res_path + "/levels/level_" + levelNumber + "/level.map"))) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }

        String fileContent = contentBuilder.toString();
        fileContent = fileContent.replaceAll("\\r", "");
        Matcher numberMatcher = Pattern.compile("\\d*\\d").matcher(fileContent);
        if(!(numberMatcher.find() && 0 == numberMatcher.start())) {
            throw new IOException();
        }
        this.startGameState.width = Integer.parseInt(fileContent.substring(numberMatcher.start(), numberMatcher.end()));
        fileContent = fileContent.substring(numberMatcher.end() + 1);
        numberMatcher = Pattern.compile("\\d*\\d").matcher(fileContent);
        if(!(numberMatcher.find() && 0 == numberMatcher.start())) {
            throw new IOException();
        }
        this.startGameState.height = Integer.parseInt(fileContent.substring(numberMatcher.start(), numberMatcher.end()));
        fileContent = fileContent.substring(numberMatcher.end() + 1);
        for(int y = 0; y < startGameState.height; ++y) {
            for(int x = 0; x < startGameState.width; ++x) {
                switch (fileContent.getBytes()[y*(startGameState.width + 1) + x]) {
                    case '*' : {
                        startGameState.borders.add(new Coordinate(x, y));
                        break;
                    }
                    case 'O': {
                        startGameState.escapes.add(new Coordinate(x, y));
                        break;
                    }
                    case 'I': {
                        Character character = new Character();
                        character.position = new Coordinate(x, y);
                        startGameState.characters.add(character);
                        break;
                    }
                    case '^': {
                        startGameState.deathTraps.add(new Coordinate(x, y));
                        break;
                    }
                    case ' ': {
                        break;
                    }
                    default: {
                        throw new IOException();
                    }
                }
            }
        }
        for (Coordinate c: startGameState.borders){
            System.out.println(c.x + " " + c.y);
        }
    }

    public GameState getCurrentGameState() {
        return new GameState(gameState);
    }

    private void moveLeft() {
        Coordinate newCoordinate = new Coordinate(character.position.x - 1, character.position.y);
        assert (newCoordinate.x >= 0 && newCoordinate.x <= gameState.width);
        moveToCoord(newCoordinate);
    }
    private void moveRight() {
        Coordinate newCoordinate = new Coordinate(character.position.x + 1, character.position.y);
        assert (newCoordinate.x >= 0 && newCoordinate.x <= gameState.width);
        moveToCoord(newCoordinate);
    }
    private void moveUp() {
        Coordinate newCoordinate = new Coordinate(character.position.x, character.position.y - 1);
        assert (newCoordinate.y >= 0 && newCoordinate.y <= gameState.width);
        moveToCoord(newCoordinate);
    }
    private void moveDown() {
        Coordinate newCoordinate = new Coordinate(character.position.x, character.position.y + 1);
        assert (newCoordinate.y >= 0 && newCoordinate.y <= gameState.width);
        moveToCoord(newCoordinate);
    }

    public GameState getStartState(){
        return startGameState;
    }

    private void moveToCoord(Coordinate coordinate) {
        switch (field[coordinate.x][coordinate.y]) {
            case ' ': {
                character.position.x = coordinate.x;
                character.position.y = coordinate.y;
                break;
            }
            case '*': {
                break;
            }
            case 'O': {
                gameResult.isWin = true;
                character.position.x = coordinate.x;
                character.position.y = coordinate.y;
                break;
            }
            case '^': {
                character.isAlive = false;
                character.position.x = coordinate.x;
                character.position.y = coordinate.y;
            }
        }
    }
}
