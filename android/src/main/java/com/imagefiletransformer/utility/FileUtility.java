package com.imagefiletransformer.utility;



import static com.imagefiletransformer.ImageFileTransformerModule.PARENT_DIR;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FileUtility {

    private static final String TAG = FileUtility.class.getSimpleName();

    public static String getUniqueName() {
        return "out_" + UUID.randomUUID().toString();
    }


    public static File createOutputFile(String format) {
        // temp fix:
        if("awebp".equalsIgnoreCase(format)) format = "webp";

        String newFileName = getUniqueName() + "." + format.toLowerCase();

        File file = new File(PARENT_DIR, newFileName);
        Log.v(TAG,TAG+"2");

        // Check if the file exists
        if (!file.exists()) {
            Log.v(TAG,TAG+"3");

            try {
                Log.v(TAG,TAG+"4");

                // Create the file and its parent directories if they don't exist
                if (file.getParentFile() != null && !file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                Log.v(TAG,TAG+"5");

                // Create the actual file
                file.createNewFile();
                Log.v(TAG,TAG+"6");

                Log.d(TAG, "File created: " + file.getAbsolutePath());
            } catch (IOException e) {
                Log.d(TAG, "Error creating the file: " + e.getMessage());
                throw new RuntimeException(e);
            }
        } else {
            Log.d(TAG, "File already exists: " + file.getAbsolutePath());
        }
        return file;
    }
    public static boolean deleteDirectory(File directory) {
        if (!directory.exists()) {
            return true;
        }

        if (!directory.isDirectory()) {
            return false;
        }

        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                Log.d(TAG, "file to delte "+file.getAbsolutePath());
                if (file.isDirectory()) {
                    // Recursively delete subdirectories
                    deleteDirectory(file);
                } else {
                    // Delete files
                    if (!file.delete()) {
                        return false;
                    }
                }
            }
        }

        // Delete the empty directory itself
        return directory.delete();
    }
}
