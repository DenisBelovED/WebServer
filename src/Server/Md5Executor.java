package Server;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Md5Executor extends FileWorker {
    private File Dir;
    private String HashDir;
    private String HashFile;

    String DirInfo() {
        return Dir.listFiles() == null ? "this is not directory" : Arrays.toString(Dir.listFiles());
    }

    public String getHashDir() {
        return HashDir;
    }

    public String getHashFile() {
        return HashFile;
    }

    public boolean IsDirectory() {
        return Dir.isDirectory();
    }

    public Md5Executor(String path) throws Exception {
        Dir = new File(path);
    }

    @Override
    public void ExecuteFile(IExecutable command) throws IOException {
        HashFile = command.Proceess(Dir);
    }

    @Override
    public void ExecuteDir(IExecutable command) throws IOException {
        if (GetIsRecursive())
            RecursiveHandler(command, Dir);
        else
            NotRecursiveHandler(command);
    }

    private void NotRecursiveHandler(IExecutable command) throws IOException {
        StringBuilder result = new StringBuilder();
        for (File file : Dir.listFiles())
            if (file.isFile()) {
                result.append(command.Proceess(file));
            }
        HashDir = MD5.getMD5(result.toString());
    }

    private void RecursiveHandler(IExecutable command, File currentDir) throws IOException {
        StringBuilder result = new StringBuilder();
        for (File file : currentDir.listFiles()) {
            if (file.isFile())
                result.append(command.Proceess(file));
            if (file.isDirectory())
                RecursiveHandler(command, file.getAbsoluteFile());
        }
        HashDir = MD5.getMD5(result.toString());
    }
}
