package Server;

import java.io.IOException;

public abstract class FileWorker {
    private boolean isRecursive = false;
    private String currentPath = null;

    public boolean GetIsRecursive() {
        return isRecursive;
    }

    public void SetIsRecursive(boolean bool) {
        isRecursive = bool;
    }

    public abstract void ExecuteDir(IExecutable command) throws IOException;

    public abstract void ExecuteFile(IExecutable command) throws IOException;
}
