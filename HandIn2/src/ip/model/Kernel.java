package ip.model;

import java.util.Stack;

/**
 * Created by Søren Palmund on 31-08-2015.
 */
public class Kernel
{
    private static int counter = 0;

    public int id;

    public Stack<Job> schedule = new Stack<>();

    public void add(Job job)
    {
        schedule.add( job );
    }

    public boolean hasRoomForJob(Job job)
    {
        return schedule.empty() || schedule.peek().finish <= job.start;
    }

    public int nextAvailableTimes()
    {
        return schedule.empty() ? 0 : schedule.peek().finish;
    }

    public static Kernel createWithJob(Job job) {
        return new Kernel( counter++, job );
    }

    private Kernel(int id, Job job)
    {
        this.id = id;
        this.schedule.push( job );
    }
}
