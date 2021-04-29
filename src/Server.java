import java.util.ArrayList;

public class Server {
    public int nextDeparture;
    public Job job;
    public ArrayList<Job> queue;

    public Server(){
        queue = new ArrayList<Job>();
        nextDeparture = 0;
        job = null;
    }
}
