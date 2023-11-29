package Test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Scanner;

public class test1 {
    public static void main(String[] args) {
        ProcessBuilder builder = new ProcessBuilder("S:\\StudyFiles\\SGU\\2023-2024-HK1\\LTUDM\\3.Project\\Workspace\\FinalProject\\Server\\src\\Server\\temp\\908497.exe");
        Process process;
        Scanner sc = new Scanner(System.in);
        try {
            process = builder.start();
            
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
            StreamReader outputReader = new StreamReader(process.getInputStream(), System.out);
            outputReader.start();
            StreamReader err = new StreamReader(process.getErrorStream(), System.err);
            err.start();
            
            for (int i = 0; i < 1; i++) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                // System.out.print("Nhap: ");
                writer.write(sc.nextLine());
                writer.flush();
            }
            writer.write("exit\n");
            writer.flush();
            
            while (process.isAlive()) {
                System.out.println("alive?");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            System.out.println("dead");
            outputReader.shutdown();
            err.shutdown(); 
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }   
}
