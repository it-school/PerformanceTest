import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;

public class ForkJoinSum extends RecursiveTask<Long> {
   private final int[] array;
   private final int start, end;
   private static final int THRESHOLD = 10_000_000;  // Threshold value for recursive task splitting

   /**
    * A constructor that creates a new `ForkJoinSum` object.
    *
    * @param array the array to be processed
    * @param start the starting index of the array
    * @param end   the ending index of the array
    */
   public ForkJoinSum(int[] array, int start, int end) {
      this.array = array;
      this.start = start;
      this.end = end;
   }

   /**
    * This code method named `compute()` calculates the sum of elements in an array using the Fork/Join framework.
    * It checks if the size of the task (the range of indices from `start` to `end`) is less than a threshold value.
    * If it is, it performs the computation directly by iterating over the array and summing up the elements in parallel using the Fork/Join framework.
    * If the size is greater than the threshold, it divides the task into two smaller tasks and executes them in parallel.
    * It creates two new instances of `ForkJoinSum` for the left and right halves of the array, and then calls the `fork()` method on the left task to execute it in parallel.
    * It then waits for the left task to finish by calling the `join()` method, and adds the results of the left and right tasks together.
    * The final sum is returned.
    *
    * @return the sum of the array elements
    */
   @Override
   protected Long compute() {
      if (end - start <= THRESHOLD) {
         // Direct computation if the size of the task is small enough
         long sum = 0;
         for (int i = start; i < end; i++) {
            sum += array[i];
         }
         return sum;
      }
      else {
         // Divide the task into two smaller tasks and execute them in parallel
         int middle = (start + end) / 2;
         ForkJoinSum leftTask = new ForkJoinSum(array, start, middle);
         ForkJoinSum rightTask = new ForkJoinSum(array, middle, end);
         leftTask.fork(); // execute the left task in parallel
         return rightTask.compute() + leftTask.join(); // wait for the left task to finish and add the results
      }
   }

   /**
    * A main method that calculates the sum of elements in a large array using a {@link ForkJoinPool} and then prints
    * the total sum and the time it took to calculate it.
    * The code creates a large array, initializes it with values from 0 to 1 billion, and divides it into 4 equal parts.
    * Then it uses a {@link ForkJoinPool} to calculate the sum of each part concurrently.
    * The results from each task are stored in a `Future` array and then combined to calculate the total sum.
    * The code measures the execution time using `System.nanoTime()` and prints the total sum and execution time.
    * This process is repeated 20 times, and the average execution time is calculated and printed at the end.
    *
    * @param args the command line arguments
    */
   public static void main(String[] args) {
      long averageTime = 0;
      for (int k = 0; k < 15; k++) {
         long totalSum;
         int[] array = new int[1_000_000_000];

         // Initialize the array with values from 0 to 1 billion
         for (int i = 0; i < array.length; i++) {
            array[i] = i;
         }

         // Create a task to calculate the sum of the array
         ForkJoinSum task = new ForkJoinSum(array, 0, array.length);

         // Measure the start time
         long startTime = System.nanoTime();

         // Instance of ForkJoinPool, which is a high-level, multithreaded utility for executing tasks that can be split into smaller subtasks.
         // The ForkJoinPool class is part of Java's concurrency API and is designed to efficiently execute tasks
         // that can be divided into smaller, independent pieces
         try (ForkJoinPool pool = new ForkJoinPool()) {
            // Execute the task using the ForkJoinPool
            totalSum = pool.invoke(task);
         }

         // Measure the end time
         long endTime = System.nanoTime();

         // Calculate the duration in milliseconds
         long duration = (endTime - startTime) / 1_000_000;

         // Print the total sum and execution time
         System.out.println("Total sum: " + totalSum);
         System.out.println("Execution time with ForkJoinPool: " + duration + " ms");

         // Update the average time
         if (k > 4) {
            averageTime += duration;
         }
      }
      // Print the average execution time
      System.out.printf("Average time: %d ms", averageTime / 15);
   }
}