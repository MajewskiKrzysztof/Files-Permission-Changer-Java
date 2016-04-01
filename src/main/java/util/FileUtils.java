package util;

import model.FileData;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtils {

    public static void changeFileProperties(FileData fd) {
        try {
            File file = fd.getFile();
            file.setExecutable(fd.getIsExecutable());
            file.setReadable(fd.getIsReadable());
            file.setWritable(fd.getIsWritable());
            Files.setAttribute(Paths.get(file.getAbsolutePath()), "dos:hidden", fd.getIsHidden());
        } catch (Exception e) {
            e.printStackTrace();
        }

        fd.update();
    }

}
