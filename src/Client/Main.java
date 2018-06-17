package Client;

import java.io.IOException;

public class Main {
    public static void main(String[] ar) {
        try {
            Client client = new Client("127.0.0.1", 50000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}