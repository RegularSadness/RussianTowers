import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    private final int scale = 2;
    private final int size = 8;
    private final Field field = new Field();
    Group grid = new Group();
    BorderPane border = new BorderPane();

    @Override
    public void start(Stage stage) {

        HBox control = new HBox();
        control.setPrefHeight(40);
        control.setSpacing(10.0);
        control.setAlignment(Pos.BASELINE_CENTER);
        Button start = new Button("Начать");
        start.setOnMouseClicked(
                event -> refresh()
        );
        control.getChildren().addAll(start);
        border.setBottom(control);
        border.setCenter(buildGrid());
        stage.setScene(new Scene(border, 400, 400));
        stage.setTitle("Russian Towers");
        stage.setResizable(false);
        stage.show();
        refresh();
    }

    private void refresh() {
        field.initStartPosition();
        try {
            drawField();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void drawField() throws IOException {
        grid.getChildren().removeAll(grid.getChildren());
        grid = buildGrid();
        border.setCenter(grid);
        Pawn[][] cells = field.getCells();
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (cells[i][j] != null) {
                        Image image = SwingFXUtils.toFXImage(getImageForPieceOrTower(cells[i][j]), null);
                        grid.getChildren().add(getPawns(j * 40, i * 40, image));

                    }
                }
        }
    }

    private Rectangle getPawns(int x, int y, Image image) {
        Rectangle rectangle = new Rectangle();
        rectangle.setX(x);
        rectangle.setY(y);
        rectangle.setWidth(40);
        rectangle.setHeight(40);
        rectangle.setFill(new ImagePattern(image));
        final Rectangle momento = new Rectangle(x, y);
        rectangle.setOnDragDetected(
                event -> {
                    momento.setX(event.getX());
                    momento.setY(event.getY());
                }
        );
        rectangle.setOnMouseDragged(
                event -> {
                    rectangle.setX(event.getX() - 40 / 2);
                    rectangle.setY(event.getY() - 40 / 2);
                }
        );
        rectangle.setOnMouseReleased(
                event -> {
                    try {
                        field.move(
                                new Position((int) (momento.getY() / 40), (int) (momento.getX() / 40)),
                                new Position((int) (event.getY() / 40), (int) (event.getX() / 40))
                        );
                        drawField();
                        field.print();
                        if (field.isToilet()) {
                            if (field.getCurrentMoveColor() == PawnColor.BLACK) {
                                showWindow("Game over. Black in toilet. White win!");
                            }
                            if (field.getCurrentMoveColor() == PawnColor.WHITE) {
                                showWindow("Game over. White in toilet. Black win!");
                            }
                        }
                        if (field.getBlackPawnCounter() == 0) {
                            showWindow("Game over. White win!");
                        }
                        if (field.getWhitePawnCounter() == 0) {
                            showWindow("Game over. Black win!");
                        }
                    } catch (Exception e) {
                        try {
                            drawField();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                }
        );
        return rectangle;
    }

    private void showWindow(String message) {
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setContentText(message);
        info.show();
    }

    private Rectangle buildRectangle(int x, int y, int size, boolean white) {
        Rectangle rect = new Rectangle();
        rect.setX(x * size);
        rect.setY(y * size);
        rect.setHeight(size);
        rect.setWidth(size);
        if (white) {
            rect.setFill(Color.WHITE);
        } else {
            rect.setFill(Color.GRAY);
        }
        rect.setStroke(Color.BLACK);
        return rect;
    }

    private Group buildGrid() {
        Group panel = new Group();
        for (int y = 0; y != size; y++) {
            for (int x = 0; x != size; x++) {
                panel.getChildren().add(
                        buildRectangle(x, y, 40, (x + y) % 2 == 0)
                );
            }
        }
        return panel;
    }

    public BufferedImage getImageForPieceOrTower(Pawn pawn) throws IOException {
        String frontImage;

        if (pawn.isQueen()) {
            if (pawn.getColor().equals(PawnColor.WHITE)) {
                frontImage = "WhiteQueen.png";
            } else {
                frontImage = "BlackQueen.png";
            }
        } else {
            if (pawn.getColor().equals(PawnColor.WHITE)) {
                frontImage = "WhitePiece.png";
            } else {
                frontImage = "BlackPiece.png";
            }
        }

        File path = new File("src/main/resources/");
        BufferedImage currentImage = ImageIO.read(new File(path, frontImage));

        List<Pawn> pawnList = pawn.getPawnList();
        List<String> childPictures = new ArrayList<>();
        for (Pawn childPawn : pawnList) {
            if (childPawn.getColor() == PawnColor.WHITE) {
                childPictures.add("WhitePiece.png");
            } else {
                childPictures.add("BlackPiece.png");
            }
        }

        if (!childPictures.isEmpty()) {
            int i = 5;
            for (String childPicture : childPictures) {
                BufferedImage overlay = ImageIO.read(new File(path, childPicture));
                int w = currentImage.getWidth();
                int h = currentImage.getHeight() + i;
                BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

                Graphics g = combined.getGraphics();
                g.drawImage(overlay, 0, i, null);
                g.drawImage(currentImage, 0, 0, null);
                g.dispose();
                currentImage = combined;
                i += 5;
            }
        }

        return currentImage;

    }
}
