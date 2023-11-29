package Test;

import java.io.*;
import java.lang.ProcessBuilder.Redirect;
import java.util.Scanner;

import javax.swing.SwingWorker;

public class test3 {
    public static void main(String[] args) {
        try {
            ProcessBuilder pb = new ProcessBuilder().command("nslookup".split(" "));
            pb.redirectErrorStream(true);
            // pb.redirectOutput(Redirect.to(new File(".\\src\\Test\\a.txt")));
            // pb.redirectInput(Redirect.from(new File(".\\src\\Test\\b.txt")));
    
            // File f = new File(".\\src\\Test\\b.txt");
            // FileWriter fw = new FileWriter(f, true);
            // BufferedWriter bw = new BufferedWriter(fw);
            Process process = pb.start();
            StreamReader pOut = new StreamReader(process.getOutputStream(), new FileInputStream(new File(".\\src\\Test\\b.txt") ));
            StreamReader pIn = new StreamReader(new FileOutputStream(new File(".\\src\\Test\\a.txt")), process.getInputStream());
            // Thread.sleep(1000);
            pIn.start();
            SwingWorker sw1 = new SwingWorker<Void,Void>() {
                
                @Override
                protected Void doInBackground() throws Exception {
                    // process.waitFor();
                    pOut.start();
                    return null;
                }
                
            };
            sw1.execute();

            SwingWorker sw2 = new SwingWorker<Void,Void>() {
                
                @Override
                protected Void doInBackground() throws Exception {
                    pIn.start();
                    return null;
                }
                
            };
            sw2.execute();
            
            // Scanner sc = new Scanner(System.in);
            // String st = sc.nextLine();
            // pw.println(st);
            // pw.flush();
            // pw.close();
            // Thread.sleep(2000);
            // bw.write(sc.nextLine());
            // bw.flush();
            // bw.close();
                
            process.waitFor();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();;
        }
    }
}
