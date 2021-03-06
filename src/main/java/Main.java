import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    static Boolean debug = true;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/dbManager.fxml"));
        primaryStage.setTitle("elsheepo's SQLite DB Manager");
        if (debug) { primaryStage.setTitle("elsheepo's SQLite DB Manager - DEBUG MODE"); }
        primaryStage.setScene(new Scene(root, 800, 600));

        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(632);

//        primaryStage.setMaxWidth(800);
//        primaryStage.setMaxHeight(632);

        primaryStage.show();
    }

    public static void main(String[] args) {launch(args);}
}
