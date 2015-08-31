import ip.model.Job;
import ip.model.Kernel;

import java.util.*;

/**
 * Created by Søren Palmund on 31-08-2015.
 */
public class Main
{

    private static final TreeMap<Integer, Stack<Kernel>> kernels = new TreeMap<>(  );

    public static void main(String[] args)
    {
        ArrayList<Job> input = new ArrayList<>();
        input.add( new Job( 5, 7 ) );
        input.add( new Job( 3, 8 ) );
        input.add( new Job( 2, 4 ) );
        input.add( new Job( 1, 5 ) );
        input.add( new Job( 6, 8 ) );

        input.sort( (o1, o2) -> Integer.compare( o1.start, o2.start ) );

        for (Job job : input)
        {
            final Integer kernelKey = kernels.floorKey( job.start );

            if ( kernelKey == null )
            {
                // No entries found in table
                Kernel newKernel = new Kernel( job );

                addKernel( newKernel, job.finish );
            }
            else
            {
                final Map.Entry<Integer, Stack<Kernel>> entry = kernels.floorEntry( kernelKey );

                // There is a kernel available
                final Stack<Kernel> stack = entry.getValue();
                final Kernel kernel = stack.pop(); // Remove kernel as free at the selected time slot
                kernel.add( job );

                addKernel( kernel, job.finish );

                if (stack.empty())
                {
                    kernels.remove( kernelKey );
                }
            }
        }

        int i = 0;
        for (Stack<Kernel> kernelStack : kernels.values())
        {
            if (kernelStack == null)
            {
                continue;
            }
            while (!kernelStack.empty()) {
                final Kernel kernel = kernelStack.pop();
                while (!kernel.schedule.empty()) {
                    final Job pop = kernel.schedule.pop();
                    System.out.printf( "%d %d %d\n", pop.start, pop.finish, i );
                }
                i++;
            }
        }
    }

    private static void addKernel(Kernel newKernel, int kernelKey)
    {
        final Stack<Kernel> possibleNewTimeSlot = kernels.get( kernelKey );
        if (possibleNewTimeSlot == null)
        {
            // No stack available at time slot
            // So insert a stack
            final Stack<Kernel> newStack = new Stack<>();
            newStack.push( newKernel );
            kernels.put( kernelKey, newStack );
        } else {
            possibleNewTimeSlot.push( newKernel );
        }
    }
}
