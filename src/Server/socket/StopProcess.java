package Server.socket;


public class StopProcess implements Runnable{
    private String pid;

    public StopProcess(String pid) {
        this.pid= pid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        try {
            String cmd = "cmd.exe /c " + pid;
            ProcessBuilder pb = new ProcessBuilder(cmd.split(" "));
            pb.start();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
}
