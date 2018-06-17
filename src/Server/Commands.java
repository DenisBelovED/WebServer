package Server;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class Commands {
    private static Md5Executor md5Executor = null;
    private static String Path = null;

    private static String GetText(File file) throws IOException {
        return new String(Files.readAllBytes(Paths.get(file.toURI())));
    }

    static String Set(String path) {
        File file = new File(path);
        if (!file.exists())
            return null;
        Path = path;
        try {
            md5Executor = new Md5Executor(Path);
            return Path;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    static String List() {
        if (Path == null)
            return "need set path";
        return md5Executor.DirInfo();
    }

    static String Hash() throws IOException {
        if (md5Executor == null)
            return "no information list, md5Executor not init";
        else {
            if (md5Executor.IsDirectory()) {
                IExecutable md5 = new IExecutable() {
                    @Override
                    public String Proceess(File file) throws IOException {
                        return MD5.getMD5(GetText(file));
                    }
                };

                md5Executor.SetIsRecursive(true);
                md5Executor.ExecuteDir(md5);
                return md5Executor.getHashDir();
            } else {
                IExecutable md5 = new IExecutable() {
                    @Override
                    public String Proceess(File file) throws IOException {
                        return MD5.getMD5(GetText(file));
                    }
                };

                md5Executor.ExecuteFile(md5);
                return md5Executor.getHashFile();
            }
        }
    }
}
