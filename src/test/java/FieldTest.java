import org.junit.Assert;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class FieldTest {

    @Test
    public void printField(){
        Field field = new Field();
        field.print();
    }

    @Test
    public void testWhiteMoveInvalid(){
        Field field = new Field();
        field.move(new Position(2,1), new Position(3,0));
        field.move(new Position(3,0), new Position(2,1));
        field.print();
    }

    @Test
    public void testBlackMoveInvalid(){
        Field field = new Field();
        field.move(new Position(5,0), new Position(4, 1));
        field.move(new Position(4,1), new Position(5,0));
        field.print();
    }

    @Test
    public void testBlackMoveOutOfField(){
        Field field = new Field();
        field.move(new Position(5,0), new Position(4, -1));
        field.print();
    }

    @Test
    public void testCut(){
        Field field = new Field();
        field.move(new Position(2,1), new Position(3,2));
        field.move(new Position(5,0), new Position(4, 1));
        field.move(new Position(3,2), new Position(4,1));
        field.print();
        Assert.assertTrue(field.getBlackPawnCounter() == 11);
        Assert.assertTrue(field.getWhitePawnCounter() == 12);
    }

    @Test
    public void initStartPositionTest() {
        Field field = new Field();
        field.initStartPosition();
        field.print();
    }

}