import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JList;

public class FileListUtil {

    public static List<String> getFileNamesFromDirectory(String directoryPath) {
        List<String> filenames = new ArrayList<>();
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    filenames.add(file.getName());
                }
            }
        } else {
            System.out.println("Directory does not exist or is not accessible.");
        }

        return filenames;
    }
    public static JList<String> convertToJList(List<String> stringList) {
        String[] stringArray = stringList.toArray(new String[stringList.size()]);
        return new JList<>(stringArray);
    }

}
