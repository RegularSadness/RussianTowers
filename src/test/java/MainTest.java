import junit.framework.TestCase;
import org.junit.Test;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainTest extends TestCase {

    @Test
    public void testPictureCreation() throws IOException {
        Pawn pawn = new Pawn(PawnColor.WHITE);
        pawn.addPawn(new Pawn(PawnColor.BLACK));
        pawn.addPawn(new Pawn(PawnColor.WHITE));
        pawn.addPawn(new Pawn(PawnColor.BLACK));

        Main main = new Main();
        BufferedImage imageForPieceOrTower = main.getImageForPieceOrTower(pawn);
        ImageIO.write(imageForPieceOrTower, "PNG", new File("combined11.png"));
    }

}