package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientThread extends ThreadTask {
    private Socket clientSoket = null;

    public ClientThread(Socket client) {
        threadName = "client on " + client.getPort() + " port";
        clientSoket = client;
        System.out.println("created " + threadName + " " + clientSoket);
    }

    @Override
    public void run() {
        try {
            DataInputStream inputStream = new DataInputStream(clientSoket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(clientSoket.getOutputStream());
            Commands engine = new Commands();
            boolean loop = true;
            while (!clientSoket.isClosed() && loop) {
                String[] data = inputStream.readUTF().split(" ");
                switch (data[0]) {
                    case "set":
                        if (data.length == 2)
                            Send(outputStream, "setup: " + engine.Set(data[1]));
                        else
                            Send(outputStream, "wrong command");
                        break;
                    case "list":
                        if (data.length == 1)
                            Send(outputStream, engine.List());
                        else
                            Send(outputStream, "wrong command");
                        break;
                    case "hash":
                        if (data.length == 1) {
                            try {
                                Send(outputStream, engine.Hash());
                            } catch (Exception e) {
                                Send(outputStream, "wrong command");
                                Send(outputStream, e.toString());
                            }
                        } else Send(outputStream, "wrong command");
                        break;
                    case "exit":
                        if (data.length == 1)
                            loop = false;
                        else
                            Send(outputStream, "wrong command");
                        break;
                    default:
                        Send(outputStream, "wrong command");
                        break;
                }
            }
            DisruptionConnection(inputStream, outputStream);
        } catch (Exception e) {
            try {
                clientSoket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } finally {
            Terminate();
        }
    }

    private void DisruptionConnection(DataInputStream inputStream, DataOutputStream outputStream) throws IOException {
        inputStream.close();
        outputStream.close();
        System.out.println(
                String.format("client (%s, %s) disconnected",
                        clientSoket.getInetAddress().toString().substring(1),
                        clientSoket.getPort())
        );
        clientSoket.close();
    }

    private void Send(DataOutputStream outputStream, String data) throws IOException {
        outputStream.writeUTF(data);
        outputStream.flush();
    }
}
