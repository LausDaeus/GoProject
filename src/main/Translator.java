package main;
import java.util.ArrayList;

//A class containing translation operations for the FileIO and TextUI.
public class Translator {
    
    //Translates raw lines into GameEngine Object components.
    //It is required to have enough data for a board. It is optional to have any data for the Objective or AI.
    public static GameEngine translateGameInstructions(ArrayList<String> contents) throws BoardFormatException, NumberFormatException{
        
        Objective objective;
        Board board;
        int[] searchValues;
            board = translateToBoard(contents);
            if(contents.size() > 0)
                objective = translateToObjective(contents.remove(0), board);
            else{
                System.out.println("NOTE: No objective has been specified. Competitive play disabled.");
                objective = null;
            }
            if(contents.size() > 0)
                searchValues = translateToSearchValues(contents.remove(0), board);
            else{
                System.out.println("NOTE: No AI search space has been specified. This can lead to performance issues with large boards.");
                searchValues = null;
            }
        return new GameEngine(board, objective, searchValues);
    }
    
    //Checks the integrity of an inputted board data and translates it to a Board object. Throws an exception when encountering an error. 
    public static Objective translateToObjective(String raw, Board board) throws BoardFormatException, NumberFormatException{
        String[] parts = raw.split(" ");
        int colour;
        String action;
        Coordinate coord;
        
        if(parts.length != 4)
            throw new BoardFormatException("ERROR: The given objective is not formatted correctly.");
        
        switch(parts[0]){
            case "black": colour = Board.BLACK; break;
            case "white": colour = Board.WHITE; break;
            default: throw new BoardFormatException("ERROR: The given objective colour \'"+parts[0]+"\' is not white or black.");
        }
        switch(parts[1]){
            case "kill": action = parts[1]; break;
            case "defend": action = parts[1]; break;
            default: throw new BoardFormatException("ERROR: The given objective action \'"+parts[0]+"\' is undefined.");
        }
        
        int x = Integer.parseInt(parts[2]);
        int y = Integer.parseInt(parts[3]);
        
        if(x >= 0 && x < board.getWidth() && y >= 0 && y < board.getHeight())
            coord = new Coordinate(x,y);
        else
            throw new BoardFormatException("ERROR:  The given objective coordinates are not within the board's dimensions.");
        
        return new Objective(action, colour, coord);
    }
    
    //Translates search values for AI and verifies that they are within the given board's length.
    public static int[] translateToSearchValues(String raw, Board board) throws BoardFormatException, NumberFormatException{
        String[] elements = raw.split(" ");
        
        if(elements.length != 4)
            throw new BoardFormatException("ERROR: The given search space does not have the required 4 values (x1 y1 x2 y2).");
        
        int[] boundaries = new int[elements.length];
        
        for(int i = 0; i < boundaries.length; i++)
            boundaries[i] = Integer.parseInt(elements[i]);
        
        if(boundaries[0] < 0 || boundaries[0] > board.getWidth())
            throw new BoardFormatException("ERROR: The given search space x1 is not within the board's dimensions.");
        if(boundaries[1] < 0 || boundaries[1] > board.getHeight())
            throw new BoardFormatException("ERROR: The given search space y1 is not within the board's dimensions.");
        if(boundaries[2] < 0 || boundaries[2] > board.getWidth())
            throw new BoardFormatException("ERROR: The given search space x2 is not within the board's dimensions.");
        if(boundaries[3] < 0 || boundaries[3] > board.getHeight())
            throw new BoardFormatException("ERROR: The given search space y2 is not within the board's dimensions.");
        
        return boundaries;
    }

    //Checks the integrity of an inputted board data and translates it to a Board object. Throws an exception when encountering an error.
    public static Board translateToBoard(ArrayList<String> raw) throws BoardFormatException,NumberFormatException{
        int[][] rawBoard;
        int w,h;
        
        if (raw.size() < 2) {
            throw new BoardFormatException("ERROR: The given input does not contain enough lines for a Board.");
        }
        
        String[] rawInts = raw.remove(0).split(" ");
        
        if (rawInts.length == 2) {
            w = Integer.parseInt(rawInts[0]);
            h = Integer.parseInt(rawInts[1]);
        } 
        else 
            throw new BoardFormatException("ERROR: The initial line does not contain two valid integers.");
        
        if (h > raw.size() - 1)
            throw new BoardFormatException("ERROR: There are not enough rows in the specification.");
        
        for (int i = 0; i < h; i++)
            if (raw.get(i).length() != w)
                throw new BoardFormatException("ERROR: There is a mismatch in the given number of columns.");
        
        rawBoard = new int[w][h];
        for (int i = 0; i < h; i++) {
            String row = raw.remove(i);
            for (int j = 0; j < w; j++)
                rawBoard[j][i] = Translator.translateToInt(row.charAt(j));
        }
        return new Board(rawBoard);
    }
    
    
    // Method for translation of char positions to int values
    public static int translateToInt(char value) throws BoardFormatException {
        switch (value) {
            case Board.C_EMPTY:
                return Board.EMPTY;
            case Board.C_BLACK:
                return Board.BLACK;
            case Board.C_WHITE:
                return Board.WHITE;
            default:
                throw new BoardFormatException("ERROR: The board to translate contains an illegal character " + value + ". Board not translated.");
        }
    }

    //Translates Board int elements into text
    public static char translateToChar(int i) throws BoardFormatException {
        switch (i) {
            case Board.EMPTY:
                return Board.C_EMPTY;
            case Board.BLACK:
                return Board.C_BLACK;
            case Board.WHITE:
                return Board.C_WHITE;
            default:
                throw new BoardFormatException("ERROR: The board to translate contains an illegal integer " + i + ". Board not translated.");
        }
    }    
}
