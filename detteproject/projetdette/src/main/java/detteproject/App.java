package detteproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {

        scene = new Scene(loadFXML("createDette"));

        stage.setScene(scene);

        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        URL url = App.class.getResource("/fxml/" + fxml + ".fxml");

        System.out.println("URL du fichier FXML : " + url);
        if (url == null) {
            throw new IOException("FXML file not found at: resources/fxml/" + fxml + ".fxml");
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(url);
            return fxmlLoader.load();
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

}