package server.com;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyThread extends Thread {
    private Socket s;

    public MyThread(Socket s) {
        this.s = s;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            DataOutputStream outBinary = new DataOutputStream(s.getOutputStream());

            String requestLine = in.readLine();
            if (requestLine == null || requestLine.isEmpty()) {
                handle400(out);
                return;
            }

            String[] parts = requestLine.split(" ");
            if (parts.length != 3) {
                handle400(out);
                return;
            }

            String method = parts[0];
            String path = parts[1];
            String version = parts[2];

            int contentLength = 0;

            String line;
            while ((line = in.readLine()) != null && !line.trim().isEmpty()) {
                if (line.toLowerCase().startsWith("content-length:")) {
                    contentLength = Integer.parseInt(line.split(":")[1].trim());
                }
            }

            if ("GET".equals(method)) {
                handleGet(path, out, outBinary);
            } else if ("HEAD".equals(method)) {
                handleHead(path, out);
            } else if ("POST".equals(method)) {
                handlePost(in, path, out, contentLength);
            } else {
                handle405(out);
            }

            String clientIP = s.getInetAddress().getHostAddress();
            String userAgent = "";
            logRequest(clientIP, method, path, 200, userAgent);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleGet(String path, PrintWriter out, DataOutputStream outBinary) throws IOException {
        if (path.endsWith("/")) {
            path += "index.html";  // Aggiungi index.html se il path è una directory
        }

        File file = new File("htdocs" + path);
        if (file.exists()) {
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: " + getContentType(file));
            out.println("Content-Length: " + file.length());
            out.println("");
            sendFile(file, outBinary);
        } else {
            handle404(out);
        }
    }

    private void handleHead(String path, PrintWriter out) throws IOException {
        File file = new File("htdocs" + path);
        if (file.exists()) {
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: " + getContentType(file));
            out.println("Content-Length: " + file.length());
            out.println("");
        } else {
            handle404(out);
        }
    }

    private void handlePost(BufferedReader in, String path, PrintWriter out, int contentLength) throws IOException {
        String body = readBody(in, contentLength);

        out.println("HTTP/1.1 200 OK");
        out.println("Content-Type: text/html");
        out.println("");
        out.println("<html><body><h1>POST Received</h1><p>" + body + "</p></body></html>");
    }

    private void handle405(PrintWriter out) {
        out.println("HTTP/1.1 405 Method Not Allowed");
        out.println("Content-Length: 0");
        out.println("");
    }

    private void handle400(PrintWriter out) {
        out.println("HTTP/1.1 400 Bad Request");
        out.println("Content-Length: 0");
        out.println("");
    }

    private String getContentType(File file) {
        if (file.getName().endsWith(".html")) {
            return "text/html";
        } else if (file.getName().endsWith(".css")) {
            return "text/css";
        } else if (file.getName().endsWith(".js")) {
            return "application/javascript";
        } else if (file.getName().endsWith(".jpg") || file.getName().endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (file.getName().endsWith(".png")) {
            return "image/png";
        } else {
            return "application/octet-stream";
        }
    }

    private void handle404(PrintWriter out) {
        out.println("HTTP/1.1 404 Not Found");
        out.println("Content-Type: text/html");
        out.println("Content-Length: 20");
        out.println("");
        out.println("<html><body><h1>404 Not Found</h1></body></html>");
    }

    private void sendFile(File file, DataOutputStream outBinary) throws IOException {
        try (FileInputStream in = new FileInputStream(file)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                outBinary.write(buffer, 0, bytesRead);
            }
        }
    }

    private String readBody(BufferedReader in, int contentLength) throws IOException {
        if (contentLength <= 0) return "";
        char[] buffer = new char[contentLength];
        int read = 0;
        while (read < contentLength) {
            int n = in.read(buffer, read, contentLength - read);
            if (n == -1) break;
            read += n;
        }
        return new String(buffer, 0, read);
    }

    private void logRequest(String clientIP, String method, String path, int status, String userAgent) {
        try (FileWriter logFile = new FileWriter("access.log", true)) {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            logFile.write(String.format("%s - %s %s %s %d %s\n", timestamp, clientIP, method, path, status, userAgent));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
