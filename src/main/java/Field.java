import java.util.Arrays;

public class Field {
    private final Pawn[][] cells = new Pawn[8][8];
    private int blackPawnCounter = 12;
    private int whitePawnCounter = 12;
    public final Pawn EMPTY_VALUE = null;
    private PawnColor currentMoveColor = PawnColor.WHITE;
    private Pawn lastPawnMove;

    public PawnColor getCurrentMoveColor() {
        return currentMoveColor;
    }

    public int getBlackPawnCounter() {
        return blackPawnCounter;
    }

    public int getWhitePawnCounter() {
        return whitePawnCounter;
    }

    public void initStartPosition() {
        currentMoveColor = PawnColor.WHITE;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                cells[i][j] = null;
            }
        }
        cells[0][3] = new Pawn(PawnColor.WHITE);
        cells[0][1] = new Pawn(PawnColor.WHITE);
        cells[0][5] = new Pawn(PawnColor.WHITE);
        cells[0][7] = new Pawn(PawnColor.WHITE);
        cells[1][0] = new Pawn(PawnColor.WHITE);
        cells[1][2] = new Pawn(PawnColor.WHITE);
        cells[1][4] = new Pawn(PawnColor.WHITE);
        cells[1][6] = new Pawn(PawnColor.WHITE);
        cells[2][1] = new Pawn(PawnColor.WHITE);
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
    }

    public Pawn[][] getCells() {
        return cells;
    }


    public void print() {
        for (Pawn[] cell : cells) {
            System.out.println(Arrays.toString(cell));
        }
    }

    public void switchCurrentMove(PawnColor color) {
        this.currentMoveColor = getOpponentValue(color);
    }


    public void move(Position from, Position to) {
        if (isEmpty(from)) {
            throw new RuntimeException("Move is invalid");
        }
        Pawn pawn = getValue(from);
        PawnColor color = pawn.getColor();
        if (color != currentMoveColor && !(isOccupiedByValue(to, getOpponentValue(color)) && (lastPawnMove == pawn))) {
            throw new RuntimeException("Move of opposite team");
        }
        if (!isCorrectMove(from, to, pawn)) {
            throw new RuntimeException("Move is invalid");
        }
        if (isOccupiedByValue(to, getOpponentValue(color))) {
            Position positionAfterCut = getPositionAfterTo(from, to);
            if (!isOutOfField(positionAfterCut) && isEmpty(positionAfterCut)) {
                cutOpponentPawn(color);
                Pawn oppositePawn = getValue(to);
                if (oppositePawn.getPawnList().isEmpty()) {
                    setValue(to, EMPTY_VALUE);
                    setValue(from, EMPTY_VALUE);
                    setValue(positionAfterCut, pawn);
                    checkAndSetQueen(positionAfterCut, pawn);
                    pawn.addPawn(oppositePawn);
                } else {

                    setValue(from, EMPTY_VALUE);
                    setValue(positionAfterCut, pawn);
                    checkAndSetQueen(positionAfterCut, pawn);
                    pawn.addLastChild(new Pawn(oppositePawn.getColor(), oppositePawn.isQueen()));
                    oppositePawn.removeFirstChild();
                    if (oppositePawn.getColor() == PawnColor.WHITE) {
                        whitePawnCounter++;
                    } else {
                        blackPawnCounter++;
                    }
                }
            }
        } else {
            setValue(from, EMPTY_VALUE);
            setValue(to, pawn);
            checkAndSetQueen(to, pawn);
        }
        lastPawnMove = pawn;
        switchCurrentMove(pawn.getColor());
    }

    private boolean isCutEnabled(Position from, Position to, Pawn pawn) {
        PawnColor color = pawn.getColor();
        if (isOccupiedByValue(to, getOpponentValue(color))) {
            Position positionAfterCut = getPositionAfterTo(from, to);
            if (!isOutOfField(positionAfterCut) && isEmpty(positionAfterCut)) {
                return true;
            }
        }
        return false;
    }

    private boolean isPathEmpty(Position from, Position to) {
        if (Math.abs(from.getX() - to.getX()) == 1 && Math.abs(from.getY() - to.getY()) == 1) {
            return true;
        }
        Position positionAfterFrom = getPositionAfterFrom(from, to);
        if (!isEmpty(positionAfterFrom)) {
            return false;
        }
        while (!positionAfterFrom.equals(to)) {
            positionAfterFrom = getPositionAfterTo(from, positionAfterFrom);

            if (!positionAfterFrom.equals(to) && !isEmpty(positionAfterFrom)) {
                return false;
            }
        }
        return true;
    }

    public Boolean isToilet() {
        boolean wayIsPresent = true;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (cells[i][j] != null && cells[i][j].getColor() == currentMoveColor) {
                    if (isCorrectMove(new Position(i, j), new Position(i + 1, j + 1), cells[i][j])) {
                        wayIsPresent = false;
                    }
                    if (isCorrectMove(new Position(i, j), new Position(i + 1, j - 1), cells[i][j])) {
                        wayIsPresent = false;
                    }
                    if (isCorrectMove(new Position(i, j), new Position(i - 1, j + 1), cells[i][j])) {
                        wayIsPresent = false;
                    }
                    if (isCorrectMove(new Position(i, j), new Position(i - 1, j - 1), cells[i][j])) {
                        wayIsPresent = false;
                    }
                }
            }
        }
        return wayIsPresent;
    }

    private Boolean isCorrectMove(Position from, Position to, Pawn pawn) {
        Boolean isCorrect = true;
        if (!isValid(from, to, pawn) || isOccupiedByValue(to, pawn.getColor()) || !isPathEmpty(from, to) || (isOccupiedByValue(to, getOpponentValue(pawn.getColor()))&& !isCutEnabled(from, to, pawn))) {
            isCorrect = false;
        }
        return isCorrect;
    }

    private void checkAndSetQueen(Position position, Pawn pawn) {
        if (pawn.getColor() == PawnColor.BLACK && position.getX() == 0) {
            pawn.setQueen(true);
        }
        if (pawn.getColor() == PawnColor.WHITE && position.getX() == 7) {
            pawn.setQueen(true);
        }
    }


    private Position getPositionAfterTo(Position from, Position to) {
        int horizontalShift = to.getY() - from.getY();
        int verticalShift = to.getX() - from.getX();
        int moveX = 1;
        int moveY = 1;
        if (horizontalShift < 0) {
            moveY = -1;
        }
        if (verticalShift < 0) {
            moveX = -1;
        }

        return new Position(to.getX() + moveX, to.getY() + moveY);
    }

    private Position getPositionAfterFrom(Position from, Position to) {
        int horizontalShift = to.getY() - from.getY();
        int verticalShift = to.getX() - from.getX();
        int moveX = 1;
        int moveY = 1;
        if (horizontalShift < 0) {
            moveY = -1;
        }
        if (verticalShift < 0) {
            moveX = -1;
        }

        return new Position(from.getX() + moveX, from.getY() + moveY);
    }

    private void cutOpponentPawn(PawnColor color) {
        if (color == PawnColor.WHITE) {
            blackPawnCounter--;
        } else if (color == PawnColor.BLACK) {
            whitePawnCounter--;
        } else throw new RuntimeException("Invalid value");
    }

    private PawnColor getOpponentValue(PawnColor color) {
        if (color == PawnColor.WHITE) {
            return PawnColor.BLACK;
        } else if (color == PawnColor.BLACK) {
            return PawnColor.WHITE;
        } else throw new RuntimeException("Invalid value");
    }

    private boolean isOccupiedByValue(Position position, PawnColor color) {
        if (getValue(position) == null) {
            return false;
        }
        return getValue(position).getColor() == color;
    }

    private boolean isValid(Position from, Position to, Pawn pawn) {
        PawnColor color = pawn.getColor();
        if (Math.abs(to.getX() - from.getX()) != Math.abs(to.getY() - from.getY())) {
            return false;
        }
        int delta = 0;
        if (pawn.isQueen()) {
            delta = Math.abs(to.getX() - from.getX());
        } else {
            delta = 1;
        }
        if (isOutOfField(to)) {
            return false;
        }
        if (!pawn.isQueen() && !isOccupiedByValue(to, getOpponentValue(color))) {
            if (color == PawnColor.WHITE) {
                if (to.getX() - from.getX() != delta) {
                    return false;
                }
            } else if (color == PawnColor.BLACK) {
                if (from.getX() - to.getX() != delta) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    private boolean isOutOfField(Position position) {
        if (position.getY() < 0 || position.getY() > 7 || position.getX() < 0 || position.getX() > 7) {
            return true;
        }
        return false;
    }

    private void setValue(Position position, Pawn pawn) {
        cells[position.getX()][position.getY()] = pawn;
    }

    private Pawn getValue(Position position) {
        return cells[position.getX()][position.getY()];
    }

    private boolean isEmpty(Position position) {
        return cells[position.getX()][position.getY()] == null;
    }
}
