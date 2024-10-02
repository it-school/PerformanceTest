import java.util.concurrent.*;

public class ParallelSum {
   /**
    * Calculates the sum of elements in a large array in parallel using a fixed thread pool and then prints the total sum and the time it took to calculate it.
    * The array is divided into chunks and each chunk is given to a separate thread to process. The results are then summed up.
    * 1. The code creates a large array, initializes it with values from 0 to 1 billion, and divides it into 4 equal parts.
    * 2. It uses an `ExecutorService` with 4 threads to calculate the sum of each part concurrently.
    * 3. The results from each thread are stored in a `Future` array and then combined to calculate the total sum.
    * 4. The code measures the execution time using `System.nanoTime()` and prints the total sum and execution time.
    * 5. This process is repeated 20 times, and the average execution time is calculated and printed at the end.
    * <p>
    * A main method that calculates the sum of elements in a large array in parallel
    * using a fixed thread pool and then prints the total sum and the time it took
    * to calculate it.
    * <p>
    * The array is divided into chunks and each chunk is given to a separate thread
    * to process. The results are then summed up.
    * <p>
    * The average time is calculated by taking the average of the times of 20 runs.
    *
    * @param args the command line arguments
    *
    * @throws InterruptedException if the main thread is interrupted
    * @throws ExecutionException   if an exception occurred while executing the thread
    */
   public static void main(String[] args) throws InterruptedException, ExecutionException {
      // Average time for 20 runs
      long averageTime = 0;

      for (int k = 0; k < 20; k++) {
         // Create a large array and initialize it with values from 0 to 1 billion
         int[] array = new int[1_000_000_000];

         // Array init
         for (int i = 0; i < array.length; i++) {
            array[i] = i;
         }

         // Set the number of threads
         int numberOfThreads = 4;
         try (ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads)) {
            // Calculate the size of each chunk
            int step = array.length / numberOfThreads;

            // Create an array of futures
            Future<?>[] results = new Future[numberOfThreads];

            // Start the timer
            long startTime = System.nanoTime();

            // Submit each chunk to the executor
            for (int i = 0; i < numberOfThreads; i++) {
               final int start = i * step;
               final int end = (i == numberOfThreads - 1) ? array.length : (i + 1) * step;
               results[i] = executor.submit(() -> {
                  long sum = 0;
                  // Sum the elements of the array
                  for (int j = start; j < end; j++) {
                     sum += array[j];
                  }
                  return sum;
               });
            }

            long totalSum = 0;
            for (Future<?> future : results) {
               totalSum += (Long) future.get();
            }

            // Shutdown the executor
            executor.shutdown();

            long endTime = System.nanoTime();
            long duration = (endTime - startTime) / 1_000_000;  // Время в миллисекундах
            if (k > 1) {
               averageTime += duration;
            }

            System.out.println("Total sum: " + totalSum);
            System.out.println("Execution time with ExecutorService: " + duration + " ms");
         }
      }
      System.out.printf("Average time: %d ms", averageTime / 18);
   }
}
