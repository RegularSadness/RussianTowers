import java.util.ArrayList;
import java.util.List;

public class Pawn {
    private PawnColor color;
    private boolean isQueen = false;
    private final List<Pawn> pawnList = new ArrayList<>();

    public Pawn(PawnColor color) {
        this.color = color;
    }

    public Pawn(PawnColor color, boolean queen) {
        this.color = color;
        this.isQueen = queen;
    }

    public PawnColor getColor() {
        return color;
    }

    public boolean isQueen() {
        return isQueen;
    }

    public void setQueen(boolean queen) {
        isQueen = queen;
    }

    public void addPawn(Pawn pawn) {
        pawnList.add(pawn);
    }

    public void removePawn(Pawn pawn) {
        pawnList.remove(pawn);
    }

    public List<Pawn> getPawnList() {
        return pawnList;
    }

    @Override
    public String toString() {
        if (color == PawnColor.WHITE){
            if (isQueen){
                return "Wh/Q";
            } else {
                return "Wh/P";
            }
        } else {
            if (isQueen){
                return "Bl/Q";
            } else {
                return "Bl/P";
            }
        }
    }

    public void addAllChildPawns(List<Pawn> pawns) {
        pawnList.addAll(pawns);
    }

    public void addLastChild(Pawn pawn) {
        pawnList.add(pawnList.size(), pawn);
    }

    public void removeFirstChild() {
        Pawn upperPawn = pawnList.get(0);
        this.color = upperPawn.getColor();
        this.isQueen = upperPawn.isQueen;
        pawnList.remove(0);
    }

}
