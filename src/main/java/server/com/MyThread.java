package server.com;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URLConnection;

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

            String requestLine = in.readLine();
            if (requestLine == null){
                return;
            }

            String[] parts = requestLine.split(" ");

            if (!parts[0].equals("GET")) {
                out.println("HTTP/1.1 406 Not Acceptable");
                out.println("Content-Length: 0");
                out.println();
                return;
            }

            String path = parts[1];
            if (path.endsWith("/"))
                path+= "index.html";
            File file = new File("htdocs" + path);


            DataOutputStream outBinary = new DataOutputStream(s.getOutputStream());

            if (!file.exists() || file.isDirectory()) {
                out.println("HTTP/1.1 404 Not Found");
                out.println("Content-Type: text/html");
                out.println("Content-Length: 20");
                out.println();
                out.println("<h1>404 Error</h1>");
                return;
            }

            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: " + getContentType(file));
            out.println("Content-Length: " + file.length());
            out.println("");

            FileInputStream input = new FileInputStream(file);
            byte[] buf = new byte[8192];
            int n;
            while ((n = input.read(buf)) > 0) {
                outBinary.write(buf, 0, n);
            }
            input.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getContentType(File f) throws MalformedURLException, IOException{
        URLConnection connection = f.toURI().toURL().openConnection();
        return connection.getContentType();
    }

}