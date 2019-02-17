import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    private final FileChooser fileChooser = new FileChooser();

    private Stage stage = new Stage();

    private DBController dbController;

    @FXML
    private TextField dbUrlTxt;
    @FXML
    private TabPane tabPane;
    @FXML
    private Button connectBtn, disconnectBtn, addBtn, updateBtn, deleteBtn, saveBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dbUrlTxt.setPromptText("select a sqlite database");
        toggleButtons();
    }

    public void displayFileChooserOnClick() {
        handleDisplayFileChooser();
    }

    public void displayFileChooserOnReturn(KeyEvent e) {
        if (e.getCode().equals(KeyCode.ENTER)) {
            handleDisplayFileChooser();
        }
    }

    private void handleDisplayFileChooser() {
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            dbUrlTxt.setText(file.getAbsolutePath());
        }
    }

    public void connectOnClick() {
        handleConnect();
    }

    public void connectOnReturn(KeyEvent e) {
        if (e.getCode().equals(KeyCode.ENTER)) {
            handleConnect();
        }
    }

    private void handleConnect() {

        if (dbUrlTxt.getText().isEmpty()) {

            new Alert(Alert.AlertType.ERROR, "Database url must be specified.").showAndWait();

        } else {

            dbController = new DBController(dbUrlTxt.getText());
            toggleButtons();

            List<String> tableNames = dbController.queryTables();

            for (String tableName : tableNames) {
                Tab tab = new Tab(tableName);
                tabPane.getTabs().add(tab);
                TableView<ObservableList> tableView = new TableView<>();
                tab.setContent(tableView);
                Map<String, String> columns = dbController.queryColumns(tableName);



                columns.forEach((key, value) -> {
                    TableColumn tableColumn = new TableColumn(key);
                    switch (value) {
                        case "INTEGER":
                            tableColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, Integer>, ObservableValue<String>>) param ->
                                    new SimpleStringProperty(param.getValue().toString()));
                        case "REAL":
                            tableColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, Double>, ObservableValue<String>>) param ->
                                    new SimpleStringProperty(param.getValue().toString()));
                        case "TEXT":
                        case "NONE":
                        default:
                            tableColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param ->
                                    new SimpleStringProperty(param.getValue().toString()));
                    }
                    tableView.getColumns().addAll(tableColumn);
                });

                ObservableList<ObservableList> rows = dbController.queryRows(tableName);
                tableView.getItems().addAll(rows);

            }
        }
    }

    public void disconnectOnClick() {
        handleDisconnect();
    }

    public void disconnectOnReturn(KeyEvent e) {
        if (e.getCode().equals(KeyCode.ENTER)) {
            handleDisconnect();
        }
    }

    private void handleDisconnect() {
        dbController.disconnect();
        toggleButtons();
        tabPane.getTabs().clear();
    }

    public void exitOnClick() {
        handleExit();
    }

    public void exitOnReturn(KeyEvent e) {
        if (e.getCode().equals(KeyCode.ENTER))
            handleExit();
    }

    private void handleExit() {
        new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to quit?").showAndWait();
        if (dbController != null && dbController.getConnection() != null) {
            dbController.disconnect();
        }
        Platform.exit();
    }

    private void toggleButtons() {
        if (dbController != null && dbController.getConnection() != null) {
            connectBtn.setDisable(true);
            disconnectBtn.setDisable(false);
            addBtn.setDisable(false);
            updateBtn.setDisable(false);
            deleteBtn.setDisable(false);
            saveBtn.setDisable(false);
        } else {
            connectBtn.setDisable(false);
            disconnectBtn.setDisable(true);
            addBtn.setDisable(true);
            updateBtn.setDisable(true);
            deleteBtn.setDisable(true);
            saveBtn.setDisable(true);
        }
    }

}
