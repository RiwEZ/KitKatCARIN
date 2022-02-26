package carin.util;

import java.io.File;

public class ListFile {

    public static String[] getList() {
        //Creating a File object for directory
        File directoryPath = new File("genetic_codes/antibody");
        //List of all files and directories
        return directoryPath.list();
    }
}
