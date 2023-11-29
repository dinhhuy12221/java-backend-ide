package Test;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;

public class test {


    private JButton executeButton;

    protected void initUI() {
        final JFrame frame = new JFrame();
        frame.setTitle(test.class.getSimpleName());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        executeButton = new JButton("Clik me to execute command");
        executeButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                doWork();
            }
        });
        frame.add(executeButton, BorderLayout.SOUTH);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    protected void doWork() {
        SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                ProcessBuilder builder = new ProcessBuilder(System.getProperty("java.home") + "/bin/java", "-version");
                builder.redirectErrorStream(true);
                Process process = builder.start();
                ConsoleReader consoleReader = new ConsoleReader(process.getInputStream());
                consoleReader.start();
                int waitFor = process.waitFor();
                consoleReader.join();
                switch (waitFor) {
                case 0:
                    return consoleReader.getResult();
                default:
                    throw new RuntimeException("Failed to execute " + builder.command() + " \nReturned message: "
                            + consoleReader.getResult());
                }
            }

            @Override
            protected void done() {
                try {
                    showCommandResult(get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    showCommandError(e);
                }
            }
        };
        worker.execute();
    }

    protected void showCommandError(ExecutionException e) {
        JOptionPane.showMessageDialog(executeButton, e.getMessage(), "An error has occured", JOptionPane.ERROR_MESSAGE);
    }

    protected void showCommandResult(String commandResult) {
        JOptionPane.showMessageDialog(executeButton, commandResult);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new test().initUI();
            }
        });
    }

    public static class ConsoleReader extends Thread {
        private InputStream is;

        private StringWriter sw;

        ConsoleReader(InputStream is) {
            this.is = is;
            sw = new StringWriter();
        }

        @Override
        public void run() {
            try {
                int c;
                while ((c = is.read()) != -1) {
                    sw.write(c);
                }
            } catch (IOException e) {
                ;
            }
        }

        String getResult() {
            return sw.toString();
        }
    }
}