package main;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Scanner;

public class Receiver {

    public static void main(String[] args) {
        try {
            System.out.println("Starting server...");
            int port = 4444;
            ServerSocket ss = new ServerSocket(port);
            Socket socket = ss.accept();
            ObjectInputStream is = new ObjectInputStream(socket.getInputStream());

            File xml = new File("serialized.xml");
            byte[] content = (byte[]) is.readObject();
            Files.write(xml.toPath(), content);

            socket.close();
            System.out.println("Received " + xml);

            // write to terminal
            Scanner scanner = new Scanner(xml);
            while (scanner.hasNextLine()) {
                System.out.println(scanner.nextLine());
            }
            scanner.close();

            deserializeObject(xml);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void deserializeObject(File xml) {
        try {
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(xml);

            Deserializer deserializer = new Deserializer();
            Object o = deserializer.deserialize(doc);

            visualizeObject(o);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void visualizeObject(Object o) {
        Visualizer visualizer = new Visualizer();
        visualizer.visualize(o);

        // Display result in a new window
        JFrame frame = new JFrame("Deserialized Objects");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTextArea textArea = new JTextArea();
        File file = new File("deserialized.txt");
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            textArea.read(input, "reading...");
        } catch (Exception e) {
            e.printStackTrace();
        }
        JScrollPane scroll = new JScrollPane (textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        frame.add(scroll);
        frame.setPreferredSize(new Dimension(750, 900));
        frame.pack();
        frame.setVisible(true);
    }
}
