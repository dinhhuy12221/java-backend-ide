package Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.System.Logger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.logging.LogManager;

public class test4 {
    static java.util.logging.Logger logger;
    public static void main(String[] args) throws Exception {
        // create a lock that will be shared between reader threads
        // the lock is fair to minimize starvation possibilities
        ReentrantLock lock = new ReentrantLock(true);

        // exec the command: I use nslookup for testing on windows
        // because it is interactive and prints to stderr too
        Process p = new ProcessBuilder().command("nslookup").start();

        // create a thread to handle output from process (uses a test consumer)
        Thread outThread = createThread(p.getInputStream(), lock, System.out::print);
        outThread.setName("outThread");
        outThread.start();

        // create a thread to handle error from process (test consumer, again)
        Thread errThread = createThread(p.getErrorStream(), lock, System.err::print);
        errThread.setName("errThread");
        errThread.start();

        // create a thread to handle input to process (read from stdin for testing
        // purpose)
        PrintWriter writer = new PrintWriter(p.getOutputStream());
        Thread inThread = createThread(System.in, null, str -> {
            writer.print(str);
            writer.flush();
        });
        inThread.setName("inThread");
        inThread.start();

        // create a thread to handle termination gracefully. Not really needed in this
        // simple
        // scenario, but on a real application we don't want to block the UI until
        // process dies
        Thread endThread = new Thread(() -> {
            try {
                // wait until process is done
                p.waitFor();
                // logger.debug("process exit");

                // signal threads to exit
                outThread.interrupt();
                errThread.interrupt();
                inThread.interrupt();

                // close process streams
                p.getOutputStream().close();
                p.getInputStream().close();
                p.getErrorStream().close();

                // wait for threads to exit
                outThread.join();
                errThread.join();
                inThread.join();

                // logger.debug("exit");
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        });
        endThread.setName("endThread");
        endThread.start();

        // wait for full termination (process and related threads by cascade joins)
        endThread.join();

        // logger.debug("END");
    }

    // convenience method to create a specific reader thread with exclusion by lock
    // behavior
    private static Thread createThread(InputStream input, ReentrantLock lock, Consumer<String> consumer) {
        return new Thread(() -> {
            // wrap input to be buffered (enables ready()) and to read chars
            // using explicit encoding may be relevant in some case
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            // create a char buffer for reading
            char[] buffer = new char[8192];

            try {
                // repeat until EOF or interruption
                while (true) {
                    try {
                        // wait for your turn to bulk read
                        if (lock != null && !lock.isHeldByCurrentThread()) {
                            lock.lockInterruptibly();
                        }

                        // when there's nothing to read, pass the hand (bulk read ended)
                        if (!reader.ready()) {
                            if (lock != null) {
                                lock.unlock();
                            }

                            // this enables a soft busy-waiting loop, that simultates non-blocking reads
                            Thread.sleep(100);
                            continue;
                        }

                        // perform the read, as we are sure it will not block (input is "ready")
                        int len = reader.read(buffer);
                        if (len == -1) {
                            return;
                        }

                        // transform to string an let consumer consume it
                        String str = new String(buffer, 0, len);
                        consumer.accept(str);
                    } catch (InterruptedException e) {
                        // catch interruptions either when sleeping and waiting for lock
                        // and restore interrupted flag (not necessary in this case, however it's a best
                        // practice)
                        Thread.currentThread().interrupt();
                        return;
                    } catch (IOException e) {
                        throw new RuntimeException(e.getMessage(), e);
                    }
                }
            } finally {
                // protect the lock against unhandled exceptions
                if (lock != null && lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }

                // logger.debug("exit");
            }
        });
    }
}
