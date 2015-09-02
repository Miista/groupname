import com.sun.org.apache.bcel.internal.generic.ALOAD;
import ip.model.Job;
import ip.model.Kernel;

import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by Soren Palmund on 31-08-2015.
 */
public class IP
{
    public static void main(String[] args)
    {
        try
        {
            Job[] input = InputParser.readFromFile( args[ 0 ] );

            final TreeMap<Integer, Stack<Kernel>> kernels = schedulePartitioning( input );

            Job[] list = new Job[ input.length ];
            for (Stack<Kernel> kernelStack : kernels.values())
            {
                while (!kernelStack.empty()) {
                    final Kernel kernel = kernelStack.pop();
                    for (Job job : kernel.schedule)
                    {
                        job.kernelId = kernel.id;
                        list[ job.id ] = job;
                    }
                }
            }

            System.out.println( kernels.values().size() );
            for (Job job : list)
            {
                System.out.printf( "%d %d %d\n", job.start, job.finish, job.kernelId );
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    private static TreeMap<Integer, Stack<Kernel>> schedulePartitioning(Job[] input)
    {
        // Sort the input according to start time -> earliest start time
        Arrays.sort( input, (o1, o2) -> Integer.compare( o1.start, o2.start ) );

        /**
         * This tree structure is indexed by when the kernel is available.
         */
        final TreeMap<Integer, Stack<Kernel>> kernels = new TreeMap<>(  );
        for (Job job : input)
        {
            // Attempt to find the id of the kernel that is available
            // at the specified time or earlier
            final Integer kernelKey = kernels.floorKey( job.start );

            if ( kernelKey == null ) // No available kernel
            {
                Kernel newKernel = Kernel.createWithJob( job );
                addKernel( newKernel, job.finish, kernels );
            }
            else
            {
                final Stack<Kernel> stack = kernels.get( kernelKey );
                final Kernel kernel = stack.pop(); // Remove kernel as free at the selected time slot
                kernel.add( job );
                addKernel( kernel, job.finish, kernels );

                if (stack.empty())
                {
                    kernels.remove( kernelKey );
                }
            }
        }

        return kernels;
    }

    private static void addKernel(Kernel newKernel, int kernelKey, TreeMap<Integer, Stack<Kernel>> kernels)
    {
        /*
        If there are no stack of kernels available at the time, that means
        that no kernels were available at the time.
         */
        final Stack<Kernel> possibleNewTimeSlot = kernels.get( kernelKey );
        if (possibleNewTimeSlot == null)
        {
            // No kernels available at time slot
            // So insert a stack
            final Stack<Kernel> newStack = new Stack<>();
            newStack.push( newKernel );
            kernels.put( kernelKey, newStack );
        } else {
            possibleNewTimeSlot.push( newKernel );
        }
    }
}
