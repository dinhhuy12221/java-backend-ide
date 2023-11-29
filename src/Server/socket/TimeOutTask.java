package Server.socket;

import java.util.Timer;
import java.util.TimerTask;

class TimeOutTask extends TimerTask {
    private Thread thread;
    private Timer timer;

    public TimeOutTask(Thread thread, Timer timer) {
        this.thread = thread;
        this.timer = timer;
    }

    @Override
    public void run() {
        if(thread != null) {
            try {
                thread.start();
                // thread.join();
                timer.cancel();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
