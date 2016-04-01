package model;

import lombok.Data;

import java.io.File;

@Data
public class FileData {

    private File file;
    private String fileName;
    private String path;
    private Boolean isExecutable;
    private Boolean isReadable;
    private Boolean isWritable;
    private Boolean isHidden;

    public FileData(File file) {
        this.file = file;
        update();
    }

    public void update() {
        fileName = file.getName();
        path = file.getAbsolutePath();
        isExecutable = file.canExecute();
        isReadable = file.canRead();
        isWritable = file.canWrite();
        isHidden = file.isHidden();
    }

}
