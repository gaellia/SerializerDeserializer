package main;

import gui.ObjectCreator;
import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;

public class Sender {

    private static Object obj;

    public static Object getObj() {
        return obj;
    }

    public static void setObj(Object obj) {
        Sender.obj = obj;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("ObjectCreator");
        frame.setContentPane(new ObjectCreator(frame).getMainPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(500, 600));
        frame.pack();
        frame.setVisible(true);

        // Start serializing when the frame is disposed ie serialize was pressed
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e)
            {
                serializeObject();
                sendXML();
            }
        });
    }

    public static void serializeObject() {
        Serializer serializer = new Serializer();
        try {
            Document doc = serializer.serialize(obj);

            // Display in a readable format in terminal and file
            XMLOutputter xmlOutput = new XMLOutputter();
            xmlOutput.setFormat(Format.getPrettyFormat());
            xmlOutput.output(doc, System.out);
            xmlOutput.output(doc, new FileWriter("serialized.xml"));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void sendXML() {
        String serverAddress = "localhost";
        int serverPort = 4444;
        try {
            Socket socket = new Socket(serverAddress, serverPort);
            ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
            File file = new File("serialized.xml");

            byte[] content = Files.readAllBytes(file.toPath());
            os.writeObject(content);

            os.flush();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
