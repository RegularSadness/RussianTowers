import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;


public class FieldTest {
    private final Pawn[][] cells = new Pawn[8][8];

    @Test (expected = RuntimeException.class)
    public void testWhiteMoveInvalid(){
        Field field = new Field();
        field.initStartPosition();
        field.move(new Position(2,1), new Position(3,0));
        field.move(new Position(3,0), new Position(2,1));
    }

    @Test (expected = RuntimeException.class)
    public void testBlackMoveInvalid(){
        Field field = new Field();
        field.initStartPosition();
        field.move(new Position(5,0), new Position(4, 1));
        field.move(new Position(4,1), new Position(5,0));
    }


    @Test
    public void testBlackMoveOutOfField(){
        Field field = new Field();
        field.initStartPosition();
        RuntimeException e = assertThrows(
                RuntimeException.class, () -> {field.move(new Position(5,0), new Position(4, -1));});
        assertEquals(e.getMessage(),"Move is invalid");
    }

    @Test
    public void testCut(){
        Field field = new Field();
        field.initStartPosition();
        field.move(new Position(2,1), new Position(3,2));
        field.move(new Position(5,0), new Position(4, 1));
        field.move(new Position(3,2), new Position(4,1));
        Assert.assertTrue(field.getBlackPawnCounter() == 11);
        Assert.assertTrue(field.getWhitePawnCounter() == 12);
    }

    @Test (expected = RuntimeException.class)
    public void testInvalidCut() {
        Field field = new Field();
        field.initStartPosition();
        field.move(new Position(2,1), new Position(3,2));
        field.move(new Position(5,0), new Position(4, 1));
        field.move(new Position(1,2), new Position(2,1));
        field.move(new Position(4,1), new Position(3,2));
    }

    @Test (expected = RuntimeException.class)
    public void moveFromEmptyPositionTest() {
        Field field = new Field();
        field.initStartPosition();
        field.move(new Position(4,1), new Position(3,2));
    }

    public Pawn[][] createNewCellsForSuccessMoveTest() {
        cells[0][3] = new Pawn(PawnColor.WHITE);
        cells[0][1] = new Pawn(PawnColor.WHITE);
        cells[0][5] = new Pawn(PawnColor.WHITE);
        cells[0][7] = new Pawn(PawnColor.WHITE);
        cells[1][0] = new Pawn(PawnColor.WHITE);
        cells[1][2] = new Pawn(PawnColor.WHITE);
        cells[1][4] = new Pawn(PawnColor.WHITE);
        cells[1][6] = new Pawn(PawnColor.WHITE);
        cells[3][2] = new Pawn(PawnColor.WHITE);
        cells[2][3] = new Pawn(PawnColor.WHITE);
        cells[2][5] = new Pawn(PawnColor.WHITE);
        cells[2][7] = new Pawn(PawnColor.WHITE);
        cells[5][0] = new Pawn(PawnColor.BLACK);
        cells[5][2] = new Pawn(PawnColor.BLACK);
        cells[5][4] = new Pawn(PawnColor.BLACK);
        cells[5][6] = new Pawn(PawnColor.BLACK);
        cells[6][1] = new Pawn(PawnColor.BLACK);
        cells[6][3] = new Pawn(PawnColor.BLACK);
        cells[6][5] = new Pawn(PawnColor.BLACK);
        cells[6][7] = new Pawn(PawnColor.BLACK);
        cells[7][0] = new Pawn(PawnColor.BLACK);
        cells[7][2] = new Pawn(PawnColor.BLACK);
        cells[7][4] = new Pawn(PawnColor.BLACK);
        cells[7][6] = new Pawn(PawnColor.BLACK);
        return cells;
    }

    @Test
    public void successMoveTest() {
        Field field = new Field();
        field.initStartPosition();
        field.move(new Position(2,1), new Position(3,2));
        Pawn[][] actual = field.getCells();
        Pawn[][] expected = createNewCellsForSuccessMoveTest();
        assertArrayEquals(expected, actual);
    }
}