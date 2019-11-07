package com.zonar.zonarapp.utils;

import android.os.Environment;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Collections;

public class FileManager {
    private String ROOT_FOLDER = "XroundAPP";
    private String SAMPLE_FOLDER = "sample";
    private String RESULT_FOLDER = "result";
    private String MEDIA_EXT = ".wav";

    private static FileManager instance;

    private FileManager() {
        // singleton
    }

    public static FileManager getInstance() {
        if (instance == null) {
            instance = new FileManager();
        }
        return instance;
    }

    public File[] getSampleList() {
        return getFileList(SAMPLE_FOLDER);
    }

    private File[] getFileList(String folder) {
        File rootFile = getFolderPath(folder);
        File[] recordFiles = rootFile.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String name) {
                return name.endsWith(MEDIA_EXT);
            }
        });

        if (recordFiles != null) {
            Arrays.sort(recordFiles, Collections.reverseOrder());
        }

        return recordFiles;
    }

    private File getFolderPath(String folder) {
        File rootFolder = new File(Environment.getExternalStorageDirectory(), ROOT_FOLDER);
        File fileFolder = new File(rootFolder, folder);

        if(!fileFolder.exists()){
            fileFolder.mkdirs();
        }
        return fileFolder;
    }

    public String createRecordFile(String LorR) {
        String timestamp = Util.getCurrentDateTime();
        String fileName = timestamp + LorR + MEDIA_EXT;
        return getFile(RESULT_FOLDER, fileName);
    }

    public String getSampleFile(String fileName) {
        return getFile(SAMPLE_FOLDER, fileName);
    }

    private String getFile(String folder, String fileName) {
        File rootFolder = getFolderPath(folder);
        File file = new File(rootFolder, fileName);
        return file.getPath();
    }
}
