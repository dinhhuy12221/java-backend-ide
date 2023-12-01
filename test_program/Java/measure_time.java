
// Java Program to demonstrate use
// of System.nanoTime() method
            import java.io.*;
 
// Driver class
public class ExecutionTimeExample 
{
    // main function
                public static void main(String[] args)
                                {
        
        // Start measuring execution time
                        long startTime = System.nanoTime();count_function(10000000);

        // Stop measuring execution time
                long endTime = System.nanoTime();
 
        // Calculate the execution time in milliseconds

                long executionTime
= (endTime - startTime) / 1000000;
                System.out.println("Counting to 10000000 takes "
                           + executionTime + "ms");}
   
    // A dummy function that runs a loop x times
    
    public static void count_function(long x){System.out.println("Loop starts");for (long i = 0; i < x; i++);
                        System.out.println("Loop ends");
             }
                    }
