package ip.model;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by Søren Palmund on 31-08-2015.
 */
public class Kernel
{
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

    public Kernel(Job job)
    {
        this.schedule.push( job );
    }
}
