package Test;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

class StreamReader extends Thread {

    private AtomicBoolean running = new AtomicBoolean(false);
    private InputStream in;
    private OutputStream out;

    public StreamReader(OutputStream out, InputStream in) {
        this.in = in;
        this.out = out;
        running.set(true);
    }

    @Override
    public void run() {
        try {
            Scanner scanner = new Scanner(in);
            PrintWriter writer = new PrintWriter(out, true);
            while (running.get()) {
                if (scanner.hasNextLine()) {
                    String st = scanner.nextLine();
                    writer.println(st);
                    writer.flush();
                }
            }
            out.close();
            writer.close();
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        running.set(false);
    }

}