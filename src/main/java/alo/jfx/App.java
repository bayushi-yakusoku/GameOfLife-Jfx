package alo.jfx;

import alo.jfx.controller.Cell;
import alo.jfx.controller.GameOfLife;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static GameOfLife gameOfLife;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        gameOfLife = new GameOfLife(20, 10);

        gameOfLife.initCells(
                new Cell(2, 4),
                new Cell(3, 4),
                new Cell(4, 4),
                new Cell(5, 4),
                new Cell(6, 4),
                new Cell(3, 3)
        );

        System.out.println("##############################################################");
        System.out.print(gameOfLife.getBoardString());

        for (int nbTurns = 0; nbTurns < 10; nbTurns++) {
            System.out.println(nbTurns + 1 + " ##############################################################");
            gameOfLife.updateBoard();
            System.out.print(gameOfLife.getBoardString());
        }

//        launch();
    }
}
