package server.com;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MyThread extends Thread {
    Socket s;

    public MyThread(Socket s) {
        this.s = s;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);

            String request = in.readLine(); //PRIMA RIGA DI UNA RICHIESA HTTP
            String header="";

            do {
                header = in.readLine();
                System.out.println(header);
            } while (!header.isEmpty());

            String response = "<strong>hello</strong> world";
            String error = "Error 404 not found";

            if (request.equals("GET /ciao HTTP/1.1")) {
                out.println(
                    "HTTP/1.1 200 OK\r\n"
                  + "Content-Length: " 
                  + response.length()
                  + "\r\nContent-Type: text/html\r\n" 
                  + "\r\n" + response
              );
            }else{
                out.println(
                    "HTTP/1.1 404 Not Found\r\n"
                  + "Content-Length: " 
                  + error.length()
                  + "\r\nContent-Type: text/html\r\n" 
                  + "\r\n" + error
              );
            }
            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
