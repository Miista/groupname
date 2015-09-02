package ip.model;

/**
 * Created by Søren Palmund on 31-08-2015.
 */
public class Job {
    public int id;
    public int start;
    public int finish;
    public int kernelId;

    public Job(int id, int start, int finish)
    {
        this.id = id;
        this.start = start;
        this.finish = finish;
    }
}
