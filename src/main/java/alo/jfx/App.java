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
        gameOfLife = new GameOfLife(100, 20);

        gameOfLife.initCells(
                new Cell(0,0),
                new Cell(1,1),
                new Cell(2,2),
                new Cell(3,3),
                new Cell(4,1),
                new Cell(-1,200)
        );

        String board = gameOfLife.getBoardString();

        System.out.print(board);

//        launch();
    }
}