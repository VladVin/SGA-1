package documents_reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by VladVin on 16.09.2016.
 */
public class DocumentsReader {
    public static ArrayList<String> readDocsFromDirectories(
            ArrayList<String> dirs) throws IOException {
        ArrayList<File> filesList = new ArrayList<>();

        for (String dir : dirs) {
            filesList = getAllFilesInFolder(new File(dir), filesList);
        }

        ArrayList<String> docs = new ArrayList<>();

        for (File file : filesList) {
//            String doc = new Scanner(file).useDelimiter("\\Z").next();
            String doc = new String(
                    Files.readAllBytes(file.toPath()),
                    StandardCharsets.UTF_8);
            docs.add(doc);
        }

        return docs;
    }

    public static ArrayList<File> getAllFilesInFolder(
            File folder, ArrayList<File> curFilesSet) {
        if (folder.isFile()) {
            curFilesSet.add(folder);
            return curFilesSet;
        }

        File[] files = folder.listFiles();
        if (files == null) {
            return curFilesSet;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                getAllFilesInFolder(file, curFilesSet);
            } else if (file.isFile()) {
                curFilesSet.add(file);
            }
        }

        return curFilesSet;
    }
}
