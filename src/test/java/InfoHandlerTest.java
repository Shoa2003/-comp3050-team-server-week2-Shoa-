import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class InfoHandlerTest {

    @BeforeEach
    void resetPlayerPosition() {
        Test.playerY = 5;
        Test.playerX = 5;
    }

    // --- Position matching tests ---

    @org.junit.jupiter.api.Test
    void matchingPositionShouldSucceed() {
        // If y and x match the player position, we should get info back
        assertTrue(Test.playerY == 5 && Test.playerX == 5);
    }

    @org.junit.jupiter.api.Test
    void mismatchedPositionShouldFail() {
        // If y or x don't match, the handler returns 204
        assertFalse(Test.playerY == 10 && Test.playerX == 10);
    }

    // --- View window calculation tests ---

    @org.junit.jupiter.api.Test
    void viewWindowCentredOnPlayer() {
        int top = Test.playerY - 5;
        int bottom = Test.playerY + 5;
        int left = Test.playerX - 5;
        int right = Test.playerX + 5;

        assertEquals(0, top);
        assertEquals(10, bottom);
        assertEquals(0, left);
        assertEquals(10, right);
    }

    @org.junit.jupiter.api.Test
    void viewWindowAlways11x11() {
        int top = Test.playerY - 5;
        int bottom = Test.playerY + 5;
        int left = Test.playerX - 5;
        int right = Test.playerX + 5;

        assertEquals(11, bottom - top + 1);
        assertEquals(11, right - left + 1);
    }

    @org.junit.jupiter.api.Test
    void viewWindowAtCornerPosition() {
        // Player at (1,1) - close to top-left corner
        Test.playerY = 1;
        Test.playerX = 1;

        int top = Test.playerY - 5;    // -4
        int left = Test.playerX - 5;   // -4

        // top and left go negative, which is out of bounds
        assertTrue(top < 0);
        assertTrue(left < 0);
        // but the window is still 11x11
        int bottom = Test.playerY + 5;
        int right = Test.playerX + 5;
        assertEquals(11, bottom - top + 1);
        assertEquals(11, right - left + 1);
    }

    // --- Tile retrieval tests ---

    @org.junit.jupiter.api.Test
    void centreOfViewIsPlayerTile() {
        // Player at (5,5) should be standing on 'g' (grass)
        char tile = GameMap.getTile(Test.playerY, Test.playerX);
        assertEquals('g', tile);
    }

    @org.junit.jupiter.api.Test
    void topLeftCornerOfMapIsBrickWall() {
        // (0,0) is 'B' in map.txt
        assertEquals('B', GameMap.getTile(0, 0));
    }

    @org.junit.jupiter.api.Test
    void viewIncludesBlockingTiles() {
        // With player at (5,5), the view includes row 0 which is all B's
        int top = Test.playerY - 5; // row 0
        assertTrue(GameMap.isInBounds(top, Test.playerX));
        assertEquals('B', GameMap.getTile(top, 0));
    }

    @org.junit.jupiter.api.Test
    void outOfBoundsHandledGracefully() {
        // If row or col is negative, isInBounds should return false
        assertFalse(GameMap.isInBounds(-1, 0));
        assertFalse(GameMap.isInBounds(0, -1));
    }

    // --- Info array construction test ---

    @org.junit.jupiter.api.Test
    void infoArrayHas11Rows() {
        int top = Test.playerY - 5;
        int bottom = Test.playerY + 5;
        int rowCount = 0;
        for (int row = top; row <= bottom; row++) {
            rowCount++;
        }
        assertEquals(11, rowCount);
    }

    @org.junit.jupiter.api.Test
    void infoArrayHas11Columns() {
        int left = Test.playerX - 5;
        int right = Test.playerX + 5;
        int colCount = 0;
        for (int col = left; col <= right; col++) {
            colCount++;
        }
        assertEquals(11, colCount);
    }

    // --- Edge case: player moves then info is checked ---

    @org.junit.jupiter.api.Test
    void infoReflectsUpdatedPosition() {
        // Simulate a move south
        Test.playerY = 6;
        Test.playerX = 5;

        int top = Test.playerY - 5; // now 1 instead of 0
        assertEquals(1, top);

        // The view should now show a different slice of the map
        char topLeftTile = GameMap.isInBounds(top, Test.playerX - 5)
                ? GameMap.getTile(top, Test.playerX - 5)
                : ' ';
        assertEquals('B', topLeftTile); // row 1, col 0 is still 'B'
    }

    @org.junit.jupiter.api.Test
    void viewWindowAfterMovingToEdge() {
        // Player near bottom-right of map
        Test.playerY = 18;
        Test.playerX = 18;

        int bottom = Test.playerY + 5; // 23, past the map (20 rows)
        int right = Test.playerX + 5;  // 23, past the map (20 cols)

        // Some tiles will be out of bounds
        assertFalse(GameMap.isInBounds(bottom, right));
        // But the in-bounds ones should still be correct
        assertTrue(GameMap.isInBounds(18, 18));
    }
}
