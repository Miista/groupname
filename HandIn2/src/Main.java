import ip.model.Job;
import ip.model.Kernel;

import java.util.*;

/**
 * Created by Søren Palmund on 31-08-2015.
 */
public class Main
{
    public static void main(String[] args)
    {
        ArrayList<Job> input = new ArrayList<>();
        input.add( new Job( 5, 7 ) );
        input.add( new Job( 3, 8 ) );
        input.add( new Job( 2, 4 ) );
        input.add( new Job( 1, 5 ) );
        input.add( new Job( 5, 8 ) );

        input.sort( (o1, o2) -> Integer.compare( o1.start, o2.start ) );

        TreeSet<Kernel> kernels = new TreeSet<>( (k1, k2) -> Integer.compare( k1.nextAvailableTimes(), k2.nextAvailableTimes() ) );
        kernels.add( new Kernel() );

        Job lastJob = null;
        for (Job job : input)
        {
            Kernel kernel = kernels.first();
            if (kernel.hasRoomForJob( job ))
            {
                kernels.remove( kernel );
                kernel.add( job );
                if (kernels.add( kernel ))
                {
                    System.out.println("lol");
                }
                lastJob = job;
            } else
            {
                Kernel newKernel = new Kernel();
                newKernel.add( job );
                kernels.add( newKernel );
            }
        }

        int i = 0;
        for (Kernel kernel : kernels)
        {
            while (!kernel.schedule.empty()) {
                final Job pop = kernel.schedule.pop();
                System.out.printf( "%d %d %d", pop.start, pop.finish, i );
                System.out.println();
            }
            i++;
        }
    }
}
