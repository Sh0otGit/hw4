package omok;

import java.util.Objects;
import java.util.Random;

public class Board {

    private int numberOfEmptyFields;

    private final int size;
    private final Stone[][] stones;
    private final Game game;

    /** Create a new board of the default size. */
    public Board(Game game) {
        this.game = game;
        this.size = 10;
        this.stones = new Stone[size][size];
        this.numberOfEmptyFields = size * size;
        arrayFill();
    }

    /** Create a new board of the specified size. */
    public Board(Game game, int size) {
        this.game = game;
        this.size = size;
        this.stones = new Stone[size][size];
        this.numberOfEmptyFields = size * size;
        arrayFill();
    }

    /** Return the size of this board. */
    public int size() {
        return this.size;
    }

    /** Return the board. */
    public Stone[][] boardMatrix(){return this.stones;}

    /** Removes all the stones placed on the board, effectively
     * resetting the board to its original state.
     */
    public void clear() {
        arrayFill();
    }

    /** Return a boolean value indicating whether all the places
     * on the board are occupied or not.
     */
    public boolean isFull() {
        for(int i = 0; i < this.size; i++) {
            for(int j = 0; j < this.size; j++) {
                if (this.stones[i][j].stoneOwner.name() == "N/A"){
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * Place a stone for the specified player at a specified
     * intersection (x, y) on the board.
     *
     * @param x 0-based column (vertical) index
     * @param y 0-based row (horizontal) index
     * @param player Player whose stone is to be placed
     */
    public void placeStone(int x, int y, Player player) {
        if (this.stones[x][y].stoneOwner.name() == "N/A"){
            this.numberOfEmptyFields -= 1;
        }
        this.stones[x][y].stoneOwner = player;
    }

    /**
     * Place a stone at a random empty location on the board.
     * @param player
     */
    public void placeRandomStone(Player player){
        Random rand = new Random();
        int x = rand.nextInt(this.size);
        int y = rand.nextInt(this.size);
        if (isEmpty(x, y)) {
            placeStone(x, y, player);
        } else {
            placeRandomStone(player);
        }
    }

    /**
     * Return a boolean value indicating whether the specified
     * intersection (x, y) on the board is empty or not.
     *
     * @param x 0-based column (vertical) index
     * @param y 0-based row (horizontal) index
     */
    public boolean isEmpty(int x, int y) {
        if (this.stones[x][y].stoneOwner.name() == "N/A"){
            return true;
        }
        return false;
    }

    /**
     * Is the specified place on the board occupied?
     *
     * @param x 0-based column (vertical) index
     * @param y 0-based row (horizontal) index
     */
    public boolean isOccupied(int x, int y) {
        if (this.stones[x][y].stoneOwner.name() == "N/A"){
            return false;
        }
        return true;
    }

    /**
     * Return a boolean value indicating whether the specified
     * intersection (x, y) on the board is occupied by the given
     * player or not.
     *
     * @param x 0-based column (vertical) index
     * @param y 0-based row (horizontal) index
     */
    public boolean isOccupiedBy(int x, int y, Player player) {
        if (this.stones[x][y].stoneOwner == player){
            return true;
        }
        return false;
    }

    /**
     * Return the player who occupies the specified intersection (x, y)
     * on the board. If the place is empty, this method returns null.
     *
     * @param x 0-based column (vertical) index
     * @param y 0-based row (horizontal) index
     */
    public Player playerAt(int x, int y) {
        return this.stones[x][y].stoneOwner;
    }

    /**
     * returns a boolean indicating if there is a row of stones
     * equal to the amount variable
     */
    public boolean checkForWinningRow(int amount, Player player, int x, int y){
        int[] inARow =
                {
                        inaRow_UpDown(player, x, y),
                        inaRow_LeftRight(player, x, y),
                        inaRow_DiagLeft(player, x, y),
                        inaRow_DiagRight(player, x, y)
                };
        boolean contains = false;
        for (int b : inARow) {
            if (b==amount) {
                contains = true;
                break;
            }
        }
        return contains;
    }
    /**
     * Return a boolean value indicating whether the given player
     * has a winning row on the board. A winning row is a consecutive
     * sequence of five or more stones placed by the same player in
     * a horizontal, vertical, or diagonal direction.
     */
    public boolean isWonBy(Player player) {
        for(int x = 0; x < this.size; x++) {
            for(int y = 0; y < this.size; y++) {
                if (isOccupiedBy(x, y, player)){
                    if(checkForWinningRow(5, player, x, y)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Iterate through this.boardMatrix and set every value to a "N/A"
     * valye to represent an empty intersection (x, y).
     */
    public void arrayFill(){
        for(int i = 0; i < this.size; i++) {
            for(int j = 0; j < this.size; j++) {
                this.stones[i][j] = new Stone(i, j, new Player(this.game, "N/A"));
            }
        }
    }

    /**
     * Recursively checks for stones placed in a row and returns the number of
     * stones in a row in all directions/
     *
     * @param current Player to check for stones in a row.
     * @param orientation The direction to check for stones in a row.
     * @param x 0-based column (vertical) index
     * @param y 0-based row (horizontal) index
     */
    int inARow_Recursive(Player current, String orientation, int x, int y){
        if(Objects.equals(orientation, "down")){x--;}
        else if (Objects.equals(orientation, "up")){x++;}
        else if (Objects.equals(orientation, "left")){y--;}
        else if (Objects.equals(orientation, "right")){y++;}
        else if(Objects.equals(orientation, "upleft")){x--;y--;}
        else if (Objects.equals(orientation, "upright")){x--;y++;}
        else if (Objects.equals(orientation, "downleft")){x++;y--;}
        else if (Objects.equals(orientation, "downright")){x++;y++;}
        if (x < 0 || x >= this.size){
            return 0;
        }
        if(y < 0 || y >= this.size){
            return 0;
        }
        if (this.stones[x][y].stoneOwner != current){
            return 0;
        }
        return 1 + inARow_Recursive(current, orientation, x, y);
    }

    /**
     * Used in the inARow_Recursive() method to check for a winning
     * vertical row of stones.
     *
     * @param current Player to check for stones in a row.
     * @param x 0-based column (vertical) index
     * @param y 0-based row (horizontal) index
     */
    int inaRow_UpDown(Player current, int x, int y){
        int check = 1 + inARow_Recursive(current, "down", x, y) + inARow_Recursive(current, "up", x, y);
        return check;
    }

    /**
     * Used in the inARow_Recursive() method to check for a winning
     * vertical row of stones.
     *
     * @param current Player to check for stones in a row.
     * @param x 0-based column (vertical) index
     * @param y 0-based row (horizontal) index
     */
    int inaRow_LeftRight(Player current, int x, int y){
        int check = 1 + inARow_Recursive(current, "left", x, y) + inARow_Recursive(current, "right", x, y);
        return check;
    }

    /**
     * Used in the inARow_Recursive() method to check for a winning
     * vertical row of stones.
     *
     * @param current Player to check for stones in a row.
     * @param x 0-based column (vertical) index
     * @param y 0-based row (horizontal) index
     */
    int inaRow_DiagLeft(Player current, int x, int y){
        int check = 1 + inARow_Recursive(current, "upleft", x, y) + inARow_Recursive(current, "downright", x, y);
        return check;
    }

    /**
     * Used in the inARow_Recursive() method to check for a winning
     * vertical row of stones.
     *
     * @param current Player to check for stones in a row.
     * @param x 0-based column (vertical) index
     * @param y 0-based row (horizontal) index
     */
    int inaRow_DiagRight(Player current, int x, int y){
        int check = 1 + inARow_Recursive(current, "downleft", x, y) + inARow_Recursive(current, "upright", x, y);
        return check;
    }

    /**
     * An intersection on an Omok board identified by its 0-based column
     * index (x) and row index (y). The indices determine the position
     * of a place on the board, with (0, 0) denoting the top-left
     * corner and (n-1, n-1) denoting the bottom-right corner,
     * where n is the size of the board.
     */

    public static class Stone {
        /** 0-based column index of this place. */
        public final int x;

        /** 0-based row index of this place. */
        public final int y;

        public Player stoneOwner;

        /** Create a new place of the given indices.
         *  @param x 0-based column (vertical) index
         * @param y 0-based row (horizontal) index
         * @param stoneOwner
         */
        public Stone(int x, int y, Player stoneOwner) {
            this.x = x;
            this.y = y;
            this.stoneOwner = stoneOwner;
        }

        // other methods if needed ...
    }

}