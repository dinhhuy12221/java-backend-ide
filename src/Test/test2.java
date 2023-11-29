package Test;

// import java.io.BufferedReader;
// import java.io.IOException;
// import java.io.InputStream;
// import java.io.InputStreamReader;
// import java.util.stream.Collectors;
// import java.util.stream.Stream;

// public class test2 {
//     public static void main(String[] args) throws IOException {
//         getOutputFromProgram("cmd /C S:\\StudyFiles\\SGU\\2023-2024-HK1\\LTUDM\\3.Project\\Workspace\\FinalProject\\Server\\src\\Server\\temp\\992730.exe 2>&1");
//     }
//     public static String getOutputFromProgram(String program) throws IOException {
//     Process proc = new ProcessBuilder(program.split(" ")).start();
//     return Stream.of(proc.getErrorStream(), proc.getInputStream()).parallel().map((InputStream isForOutput) -> {
//         StringBuilder output = new StringBuilder();
//         try (BufferedReader br = new BufferedReader(new InputStreamReader(isForOutput))) {
//             String line;
//             while ((line = br.readLine()) != null) {
//                 output.append(line);
//                 System.out.println(line);
//                 output.append("\n");
//             }
//         } catch (IOException e) {
//             throw new RuntimeException(e);
//         }
//         return output;
//     }).collect(Collectors.joining());
// }
// }
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;

class test2 {
    public static void main(String[] args) {
        for (String arg : args)
            System.out.println("arg: " + arg);

        for (String arg : args)
            if (arg.equals("-test")) {
                subProcessStuff();
                return;
            }
        mainProcess();

    }

    public static void subProcessStuff() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Enter String");
            String s = br.readLine();
            System.out.println("Enered String: " + s);
            System.out.println("Enter Integer:");
            int i = Integer.parseInt(br.readLine());
            System.out.println("Entered Integer: " + i);
        } catch (IOException e) {
            System.err.println("io error - " + e.getMessage());
        } catch (NumberFormatException nfe) {
            System.err.println("Invalid Format!");
        }
    }

    private static PrintStream out;

    public static void mainProcess() {
        // String[] commands = { "ls", "-alt" };
        ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "S:\\StudyFiles\\SGU\\2023-2024-HK1\\LTUDM\\3.Project\\Workspace\\FinalProject\\Server\\src\\Server\\temp\\968634.exe");

        // builder.inheritIO(); // I avoid this. It was messing me up.

        try {
            Process proc = builder.start();
            InputStream errStream = proc.getErrorStream();
            InputStream inStream = proc.getInputStream();
            OutputStream outStream = proc.getOutputStream();

            new Thread(new StreamGobbler("err", out, errStream)).start();

            out = new PrintStream(new BufferedOutputStream(outStream));

            Callback cb = new Callback() {
                @Override
                public void onNextLine(String line) {
                    if (line.equals("Enter String")) {
                        out.println("aaaaa");
                        out.flush();
                    }
                    if (line.equals("Enter Integer:")) {
                        out.println("123");
                        out.flush();                    
                    }
                }
            };
            new Thread(new StreamGobbler("in", out, inStream, cb)).start();
            int errorCode = proc.waitFor();
            System.out.println("error code: " + errorCode);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}

interface Callback {
    void onNextLine(String line);
}

class StreamGobbler implements Runnable {
    private PrintStream out;
    private Scanner inScanner;
    private String name;
    private Callback cb;

    public StreamGobbler(String name, PrintStream out, InputStream inStream) {
        this.name = name;
        this.out = out;
        inScanner = new Scanner(new BufferedInputStream(inStream));
    }

    public StreamGobbler(String name, PrintStream out, InputStream inStream, Callback cb) {
        this.name = name;
        this.out = out;
        inScanner = new Scanner(new BufferedInputStream(inStream));
        this.cb = cb;
    }

    @Override
    public void run() {
        while (inScanner.hasNextLine()) {
            String line = inScanner.nextLine();
            if (cb != null)
                cb.onNextLine(line);
            System.out.printf("%s: %s%n", name, line);
        }
    }
}