import java.lang.Math;
import java.util.ArrayList;

public class SimulatedQueue {
    private static long jobsRunningTotal;
    private static long responseTimeRunningTotal;
    public static void main(String[] args){
        /* checking for geometric generator functionality
        for(int i=0; i<20; i++) {
            int g = GeometricGenerator(0.5);
            System.out.println("Generated Geometric "+i+": " + g);
        }*/
        double p;
        double q = 0.5;
        int n = 100000;
        int k = 2000;
        double[] expectedJobArray = new double[50];
        double[] expectedResponseTimeArray = new double[50];
        int jobsLeft;
        double expectedJob, expectedResponseTime;
        for (int i=0; i<49; i++) {
            p=0.01+0.01*i;
            jobsRunningTotal=0;
            responseTimeRunningTotal=0;
            jobsLeft = Simulate(p, q, k, n);
            expectedJob = 1.0 * jobsRunningTotal / (n - k);
            expectedResponseTime = 1.0 * responseTimeRunningTotal / (n - k - jobsLeft);
            expectedJobArray[i] = expectedJob;
            expectedResponseTimeArray[i] = expectedResponseTime;
            System.out.println("p: "+p);
            System.out.println("Theoretical Expected Job Number: " + p * (1 - q) / (q - p));
            System.out.println("Expected Job Number: " + expectedJob);
            System.out.println("Theoretical Expected Response Time: " + (1 - p) / (q - p));
            System.out.println("Expected Response Time: " + expectedResponseTime);
            System.out.println();
        }
        System.out.println("Expected Job Number Varying p"+expectedJobArray);
        System.out.println("Expected Response Time Varying p"+expectedResponseTimeArray);
    }
    //1 Geometric Generate Method
    public static int GeometricGenerator(double p){
        double rand = Math.random(); //uniform distibution
        if(rand<p){
            return 1;
        }
        else{
            int k = 1;
            double cdf = p;
            while(cdf<rand) {
                cdf = cdf + p*Math.pow(1-p,k);
                k++;
            }
            return k;
        }
    }
    //2
    public static int Simulate(double p, double q, int k, int n) {
        //returns jobs remaining in queue at end
        int jobs = 0;
        int sysTime = 0;
        int nextArrivalTime = 0;
        int nextDepartureTime = GeometricGenerator(q);
        Server server = new Server();
        while (jobs < n) { //Repeat until n jobs have arrived
            if (nextArrivalTime < nextDepartureTime) {
                sysTime = nextArrivalTime; //Advance the current system time to the next arrival time.
                Job j = new Job(sysTime); //Generate a new job
                j.size = GeometricGenerator(q);
                if(!(jobs<k)){ //tracking jobs in service or in queue
                    jobsRunningTotal += server.queue.size();
                    if (server.job !=null){
                        jobsRunningTotal++;
                    }
                }
                jobs++;
                //System.out.println("job arrived");
                if (server.job != null) { //add it to the queue
                    server.queue.add(j);
                    //System.out.println("job added to queue");
                } else { //or have it enter service, if the server is idle
                    //System.out.println("job entered service");
                    server.job = j;
                    nextDepartureTime = sysTime + server.job.size; // Updating the next departure time by generating a job size S ∼ Geometric(q)
                    //System.out.println("job size: "+server.job.size);
                }
                nextArrivalTime = sysTime + GeometricGenerator(p); // Update the next arrival time by generating an interarrival time
            } else {
                sysTime = nextDepartureTime; //Advance the current system time to the next departure time
                if(!(jobs<k)){ //tracking response time
                    int diff = sysTime - server.job.arrivalTime;
                    responseTimeRunningTotal += diff;
                }
                server.job = null; //“Complete” the current job by removing it from the server
                //System.out.println("job removed from service");
                if (!server.queue.isEmpty()) { //If the queue is nonempty, have a new job enter service.
                    server.job = server.queue.remove(0);
                    //System.out.println("job entered service");
                    nextDepartureTime = sysTime + server.job.size;//Update the next departure time
                    //System.out.println("job size: "+server.job.size);
                }
                else{
                    nextDepartureTime = sysTime + 999999999;
                }
            }
        }
        return server.queue.size();
    }
}
