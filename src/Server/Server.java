package Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

class Server {
    private ServerSocket serverSocket = null;
    private ThreadDispatcher threadDispatcher = ThreadDispatcher.GetInstance();

    private void Echo(String s) {
        System.out.println(s);
    }

    Server(String host, int port) throws IOException {
        try {
            Echo("new ServerSocket " + port);
            serverSocket = new ServerSocket(port, 1000, InetAddress.getByName(host));

        } catch (Exception e) {
            Echo("server not create");
            Echo(e.toString());
            System.exit(-1);
        }
    }

    void Run() throws IOException {
        try {
            Echo("wait connection");
            while (!serverSocket.isClosed()) {
                try {
                    Socket client = serverSocket.accept();
                    Echo(
                            String.format("client (%s, %s) connected",
                                    client.getInetAddress().toString().substring(1),
                                    client.getPort())
                    );
                    threadDispatcher.Add(new ClientThread(client));
                } catch (Exception e) {
                    Echo("warning, client not connected");
                    e.printStackTrace();
                }
            }
        } finally {
            Echo("go to finally");
            try {
                Echo(serverSocket.toString() + " this is serverSocket");
                if (serverSocket != null) {
                    serverSocket.close();
                    while (threadDispatcher.GetThreadInfo().size() != 0)
                        threadDispatcher.DeleteThread(threadDispatcher.GetThreadInfo().listIterator().next().getKey());
                    Echo("server shutdown");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
