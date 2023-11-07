package checkers;

public class Space {
    private String[][] locators;

    public Space() {
        // Constructor. Create the spaces with locators
        locators = new String[8][8];
        setUpLocators();
    }

    public String[][] getLocators() {
        return locators;
    }

    public void setUpLocators() {
        // Set up the board with locators
        for (int row = 7; row >= 0; row--) {
            for (int col = 7; col >= 0; col--) {
                int rowLocatorId = 7 - row;
                int colLocatorId = 7 - col;
                locators[row][col] = String.format("space%d%d", colLocatorId, rowLocatorId);
            }
        }
    }
}
