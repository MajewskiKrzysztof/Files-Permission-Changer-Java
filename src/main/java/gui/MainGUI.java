package gui;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.FileData;
import util.FileUtils;
import util.TableViewUtils;
import util.UserProperties;

import java.io.File;
import java.util.List;

public class MainGUI extends Application {

    private Stage window;

    private TableView<FileData> tableView;
    private CheckBox executable;
    private CheckBox readable;
    private CheckBox writable;
    private CheckBox hidden;
    private Button addFiles;
    private Button changeProperties;

    private ObservableList<FileData> selectedItems;

    public static void main(String[] args) {
        launch(args);
    }

    // TODO createcheckbox "should save session files?" <- option

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.window = primaryStage;
        window.setTitle("FileModifier by Krzysztof Majewski");

        setGUI();
        setTableViewClickListener();
        setOnClicks();

        TableViewUtils.readFilesFromPreviousSession(tableView);

        window.show();
    }

    private void setGUI() {
        GridPane layout = new GridPane();
        layout.setPadding(new Insets(10, 10, 10, 10));

        setTableView(layout);
        setLeftSizePanel(layout);

        Scene scene = new Scene(layout);
//        window.setResizable(false);


        GridPane.setVgrow(tableView, Priority.ALWAYS);
        GridPane.setHgrow(tableView, Priority.ALWAYS);

        window.setScene(scene);
        window.sizeToScene();
    }

    private void setTableView(GridPane layout) {
        tableView = new TableView<>();
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        TableViewUtils.addColumnsToTableView(tableView);

        layout.add(tableView, 0, 0);
    }

    private void setLeftSizePanel(GridPane layout) {
        VBox leftPanel = new VBox();
        leftPanel.setPadding(new Insets(35, 10, 10, 10));
        leftPanel.setSpacing(10);

        addFiles = new Button("Add files");
        Label emptySpace = new Label("");
        emptySpace.setMinHeight(20);

        executable = new CheckBox("Executable");
        readable = new CheckBox("Readable");
        writable = new CheckBox("Writable");
        hidden = new CheckBox("Hidden");
        changeProperties = new Button("Change");

        leftPanel.getChildren().addAll(addFiles, emptySpace, executable, readable, writable, hidden, changeProperties);

        layout.add(leftPanel, 1, 0);
    }

    private void setTableViewClickListener() {
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedItems = tableView.getSelectionModel().getSelectedItems();
            if (selectedItems.size() > 0)
                updateCheckBoxes();
        });

        tableView.setOnMouseClicked(event -> {
            if (tableView.getSelectionModel().getSelectedItems().size() == 1 && event.getClickCount() == 2) {
                TableViewUtils.changeClickedCellValue(tableView, event.getX());
                updateCheckBoxes();
                tableView.refresh();
            }
        });
    }

    private void updateCheckBoxes() {
        FileData firstFile = selectedItems.get(0);
        executable.setSelected(firstFile.getIsExecutable());
        readable.setSelected(firstFile.getIsReadable());
        writable.setSelected(firstFile.getIsWritable());
        hidden.setSelected(firstFile.getIsHidden());
    }

    private void setOnClicks() {
        addFiles.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            List<File> files = fileChooser.showOpenMultipleDialog(window);
            if (files != null)
                TableViewUtils.addNewFilesToTable(tableView, files);
        });

        changeProperties.setOnAction(event -> {
            for (FileData fd : selectedItems) {
                fd.setIsExecutable(executable.isSelected());
                fd.setIsReadable(readable.isSelected());
                fd.setIsWritable(writable.isSelected());
                fd.setIsHidden(hidden.isSelected());

                FileUtils.changeFileProperties(fd);
            }

            tableView.refresh();
        });
    }

    @Override
    public void stop() throws Exception {
        TableViewUtils.saveColumnNamesToProperties(tableView);
        TableViewUtils.saveFilesToProperties(tableView);
        UserProperties.saveProperties();

        super.stop();
    }
}