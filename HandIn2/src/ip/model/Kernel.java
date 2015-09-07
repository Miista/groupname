package ip.model;

import java.util.Stack;

/**
 * Created by Søren Palmund on 31-08-2015.
 */
public class Kernel
{
    private static int counter = 0;

    public final int id;

    public final Stack<Job> schedule = new Stack<>();

    public static Kernel createWithJob(Job job) {
        return new Kernel( counter++, job );
    }

    private Kernel(int id, Job job)
    {
        this.id = id;
        this.schedule.push( job );
    }

}
