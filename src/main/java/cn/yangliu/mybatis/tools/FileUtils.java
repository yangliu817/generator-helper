package cn.yangliu.mybatis.tools;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * The type File utils.
 */
@Slf4j
public class FileUtils {

    /**
     * Gets full path.
     *
     * @param folder   the folder
     * @param filename the filename
     * @return the full path
     */
    public static String getFullPath(String folder, String filename) {
        String path = PathUtils.getHomePath(FileUtils.class);
        if (path.endsWith(File.separator)) {
            return path + folder + File.separator + filename;
        }
        return path + File.separator + folder + File.separator + filename;
    }

    /**
     * Output.
     *
     * @param content  the content
     * @param path     the path
     * @param filename the filename
     */
    public static void output(String content, String path, String filename) {
        File fileFolder = new File(path);
        if (!fileFolder.exists()) {
            fileFolder.mkdirs();
        }
        File file = new File(fileFolder, filename);

        if (file.exists()) {
            file.delete();
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        try (FileOutputStream fos = new FileOutputStream(file);
             OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
             BufferedWriter bw = new BufferedWriter(osw)) {
            bw.write(content);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Read string.
     *
     * @param filepath the filepath
     * @param wrap     the wrap
     * @return the string
     */
    public static String read(String filepath, boolean wrap) {
        try (InputStream is = new FileInputStream(filepath)) {
            return read(is, "", wrap);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Read string.
     *
     * @param filepath the filepath
     * @param space    the space
     * @param wrap     the wrap
     * @return the string
     */
    public static String read(String filepath, String space, boolean wrap) {
        try (InputStream is = new FileInputStream(filepath)) {
            return read(is, space, wrap);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Read string.
     *
     * @param is    the is
     * @param space the space
     * @param wrap  the wrap
     * @return the string
     */
    public static String read(InputStream is, String space, boolean wrap) {
        StringBuilder sb = new StringBuilder();
        try (InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(isr)) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(space).append(line);
                if (wrap) {
                    sb.append("\n");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return sb.toString();
    }

    /**
     * Read string.
     *
     * @param is   the is
     * @param wrap the wrap
     * @return the string
     */
    public static String read(InputStream is, boolean wrap) {
        return read(is, "", wrap);
    }
}
