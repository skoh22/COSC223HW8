public class Job {
    int arrivalTime;
    int departureTime;
    int size;
    public Job(int arrTime){
        arrivalTime = arrTime;
    }
    public Job(int arrTime, int depTime, int jSize){
        arrivalTime = arrTime;
        departureTime = depTime;
        size = jSize;
    }
    public int getArrivalTime(){
        return arrivalTime;
    }
    public int getDepartureTime(){
        return departureTime;
    }
}
