package main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class Visualizer {

    public static void visualize(Object o) {
        boolean rec = true;
        runTest("deserialized.txt", o, rec);
    }

    public static void runTest(String filename, Object testObj, boolean recursive) {
        try {
            PrintStream old = System.out;
            File file = new File(filename);
            FileOutputStream fos = new FileOutputStream(file);
            PrintStream ps = new PrintStream(fos);
            System.setOut(ps);
            System.out.println("======================================================");
            new Inspector().inspect(testObj, recursive);
            System.out.println("======================================================");
            ps.flush();
            fos.flush();
            ps.close();
            fos.close();
            System.setOut(old);
        } catch (IOException ioe) {
            System.err.println("Unable to open file: " + filename);
        } catch (Exception e) {
            System.err.println("Unable to compleatly run test: " + testObj);
            e.printStackTrace();
        }
    }
}
