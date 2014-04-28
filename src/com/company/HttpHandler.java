package com.company;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by java on 25.04.14.
 */
public class HttpHandler {
    private final Socket skt;
    private BufferedReader in;
    private PrintWriter out;

    public HttpHandler(Socket socket) {
        skt=socket;
        try {
            in = new BufferedReader(new InputStreamReader(skt.getInputStream()));
            out = new PrintWriter(skt.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        String msg="1";
        List<String> packet = new ArrayList<String>();
        try {
            while (msg!= null && !msg.equals("")) {
                msg=in.readLine();
                packet.add(msg);
                System.out.println(msg);
            }
            handlePacket(packet);
            skt.close();
        } catch (IOException ex) {

        }
    }

    private void handlePacket(List<String> packet) {
        for (String s: packet) {
            if (s.contains("n=7")) {
                another();
                break;
            }
            System.out.println(s);
        }

        out.println("HTTP/1.1 200 Ok");
        out.println("Content-Type: text/html; charset=utf-8");
        out.println("Content-Length: 1000");
        out.println();
        String file = loadFile();
        out.println(file);
        String st = "<center><h4>Server time: "+new Date()+"</h4></center>";
        out.println(st);
    }

    private String loadFile() {
        try {
            FileReader f = new FileReader("c:\\Users\\java\\JWebServer\\src\\com\\company\\index.html");
            BufferedReader buf = new BufferedReader(f);
            StringBuilder sb = new StringBuilder();
            String line = buf.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = buf.readLine();
            }
            return sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Empty";
    }

    private void another() {
        out.println("HTTP/1.1 200 Ok");
        out.println("Content-Type: text/html; charset=utf-8");
        out.println("Content-Length: 500");
        out.println();
        out.println("Another text");
    }
}
