package oracle;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import oracle.ui.MainWindow;

/**
 * The Main class for running the GUI
 */
public class Main extends Application {
    private Oracle oracle = new Oracle("data/oracle.txt");

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane root = fxmlLoader.load();

            MainWindow controller = fxmlLoader.getController();
            controller.setOracle(oracle);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("ORACLE");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
