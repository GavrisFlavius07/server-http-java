package server.com;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(3000);     
        while(true){
            Socket s = ss.accept();
            MyThread thread = new MyThread(s);
            thread.start();
        }
    }
    
}