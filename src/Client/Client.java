package Client;

import java.io.*;
import java.net.Socket;

class Client {
    Client(String host, int port) throws IOException {
        Socket socket = null;
        try {
            try {
                System.out.println("Run client\n" +
                        "Connecting to the server\n\t" +
                        "(IP address " + host +
                        ", port " + port + ")");
                socket = new Socket(host, port);
                System.out.println("The connection is established.");
                System.out.println(
                        "\tLocalPort = " +
                                socket.getLocalPort() +
                                "\n\tInetAddress.HostAddress = " +
                                socket.getInetAddress().getHostAddress() +
                                "\n\tReceiveBufferSize (SO_RCVBUF) = " +
                                socket.getReceiveBufferSize());

                DataInputStream in = new DataInputStream(socket.getInputStream());
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());

                InputStreamReader isr = new InputStreamReader(System.in);
                BufferedReader keyboard = new BufferedReader(isr);
                String line = null;
                System.out.println("Type command and press enter");
                System.out.println();
                while (true) {
                    line = keyboard.readLine();
                    out.writeUTF(line);
                    out.flush();
                    line = in.readUTF();
                    if (line.endsWith("exit"))
                        break;
                    else {
                        System.out.println("Server answer :\n\t" + line);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            try {
                if (socket != null)
                    socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
