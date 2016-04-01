package util;

import com.thoughtworks.xstream.XStream;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.FileData;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TableViewUtils {

    public static void addNewFilesToTable(TableView<FileData> tableView, List<File> files) {
        for (File f : files) {
            FileData fileData = new FileData(f);
            tableView.getItems().add(fileData);
        }

        tableView.refresh();
    }

    public static void changeClickedCellValue(TableView<FileData> tableView, double mouseX) {
        double col1 = tableView.getColumns().get(0).getWidth();
        double col2 = tableView.getColumns().get(1).getWidth();
        double col3 = tableView.getColumns().get(2).getWidth();
        double col4 = tableView.getColumns().get(3).getWidth();
        double col5 = tableView.getColumns().get(4).getWidth();
        double col6 = tableView.getColumns().get(5).getWidth();

        double executableStart = col1 + col2;
        double executableStop = col1 + col2 + col3;

        double readableStart = col1 + col2 + col3;
        double readableStop = col1 + col2 + col3 + col4;

        double writableStart = col1 + col2 + col3 + col4;
        double writableStop = col1 + col2 + col3 + col4 + col5;

        double hiddenStart = col1 + col2 + col3 + col4 + col5;
        double hiddenStop = col1 + col2 + col3 + col4 + col5 + col6;

        FileData row = tableView.getSelectionModel().getSelectedItems().get(0);
        if (mouseX > executableStart && mouseX < executableStop) {
            row.setIsExecutable(!row.getIsExecutable());
            row.getFile().setExecutable(row.getIsExecutable());
        } else if (mouseX > readableStart && mouseX < readableStop) {
            row.setIsReadable(!row.getIsReadable());
            row.getFile().setReadable(row.getIsReadable());
        } else if (mouseX > writableStart && mouseX < writableStop) {
            row.setIsWritable(!row.getIsWritable());
            row.getFile().setWritable(row.getIsWritable());
        } else if (mouseX > hiddenStart && mouseX < hiddenStop) {
            row.setIsHidden(!row.getIsHidden());
            try {
                Files.setAttribute(Paths.get(row.getFile().getAbsolutePath()), "dos:hidden", row.getIsHidden());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void addColumnsToTableView(TableView tableView) {
        TableColumn<FileData, String> fileNameColumn = new TableColumn<>("FileName");
        fileNameColumn.setMinWidth(150);
        fileNameColumn.setCellValueFactory(new PropertyValueFactory<>("fileName"));

        TableColumn<FileData, String> pathColumn = new TableColumn<>("Path");
        pathColumn.setPrefWidth(300);
        pathColumn.setMinWidth(300);
        pathColumn.setCellValueFactory(new PropertyValueFactory<>("path"));

        TableColumn<FileData, Boolean> executableColumn = new TableColumn<>("Executable");
        executableColumn.setMinWidth(100);
        executableColumn.setCellValueFactory(new PropertyValueFactory<>("isExecutable"));

        TableColumn<FileData, Boolean> readableColumn = new TableColumn<>("Readable");
        readableColumn.setMinWidth(100);
        readableColumn.setCellValueFactory(new PropertyValueFactory<>("isReadable"));

        TableColumn<FileData, Boolean> writableColumn = new TableColumn<>("Writable");
        writableColumn.setMinWidth(100);
        writableColumn.setCellValueFactory(new PropertyValueFactory<>("isWritable"));

        TableColumn<FileData, Boolean> hiddenColumn = new TableColumn<>("Hidden");
        hiddenColumn.setMinWidth(100);
        hiddenColumn.setCellValueFactory(new PropertyValueFactory<>("isHidden"));

        ArrayList<String> columns = new ArrayList<>();
        columns.add((String) UserProperties.getProperty(UserProperties.COLUMN_1));
        columns.add((String) UserProperties.getProperty(UserProperties.COLUMN_2));
        columns.add((String) UserProperties.getProperty(UserProperties.COLUMN_3));
        columns.add((String) UserProperties.getProperty(UserProperties.COLUMN_4));
        columns.add((String) UserProperties.getProperty(UserProperties.COLUMN_5));
        columns.add((String) UserProperties.getProperty(UserProperties.COLUMN_6));
        for (String colName : columns) {
            if (colName.equals(fileNameColumn.getText()))
                tableView.getColumns().add(fileNameColumn);
            if (colName.equals(pathColumn.getText()))
                tableView.getColumns().add(pathColumn);
            if (colName.equals(executableColumn.getText()))
                tableView.getColumns().add(executableColumn);
            if (colName.equals(readableColumn.getText()))
                tableView.getColumns().add(readableColumn);
            if (colName.equals(writableColumn.getText()))
                tableView.getColumns().add(writableColumn);
            if (colName.equals(hiddenColumn.getText()))
                tableView.getColumns().add(hiddenColumn);
        }
    }

    public static void readFilesFromPreviousSession(TableView<FileData> tableView) {
        String obj = (String) UserProperties.getProperty(UserProperties.PREVIOUS_FILES);
        if (obj == null || obj.isEmpty())
            return;

        XStream xStream = new XStream();
        ArrayList<String> paths = (ArrayList<String>) xStream.fromXML(obj);
        for (String s : paths) {
            try {
                File f = new File(s);
                if (f.exists()) {
                    FileData fd = new FileData(f);
                    tableView.getItems().add(fd);
                }
            } catch (Exception e) {
            }
        }

        tableView.refresh();
    }

    public static void saveColumnNamesToProperties(TableView<FileData> tableView) {
        UserProperties.putProperty(UserProperties.COLUMN_1, tableView.getColumns().get(0).getText());
        UserProperties.putProperty(UserProperties.COLUMN_2, tableView.getColumns().get(1).getText());
        UserProperties.putProperty(UserProperties.COLUMN_3, tableView.getColumns().get(2).getText());
        UserProperties.putProperty(UserProperties.COLUMN_4, tableView.getColumns().get(3).getText());
        UserProperties.putProperty(UserProperties.COLUMN_5, tableView.getColumns().get(4).getText());
        UserProperties.putProperty(UserProperties.COLUMN_6, tableView.getColumns().get(5).getText());
    }

    public static void saveFilesToProperties(TableView<FileData> tableView) {
        ArrayList<String> paths = new ArrayList<>();
        for (FileData fd : tableView.getItems()) {
            paths.add(fd.getPath());
        }
        XStream xStream = new XStream();
        String xml = xStream.toXML(paths);
        UserProperties.putProperty(UserProperties.PREVIOUS_FILES, xml);
    }
}