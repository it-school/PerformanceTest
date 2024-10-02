public class SequentialSum {
   /**
    * A main method that calculates the sum of elements in a large array in sequence
    * and then prints the total sum and the time it took to calculate it.
    * The code creates a large array, initializes it with values from 0 to 1 billion,
    * and then calculates the sum of the elements using a simple loop.
    * The code measures the execution time using `System.nanoTime()` and prints the total sum and execution time.
    * This process is repeated 20 times, and the average execution time is calculated and printed at the end.
    * <p>
    * This is a baseline for the performance comparison with the parallel implementations.
    * <p>
    *
    * @param args the command line arguments
    */
   public static void main(String[] args) {
      long averageTime = 0;
      for (int k = 0; k < 20; k++) {
         // Create a large array and initialize it with values from 0 to 1 billion
         int[] array = new int[1_000_000_000];

         // Initialize the array with values from 0 to 1 billion
         for (int i = 0; i < array.length; i++) {
            array[i] = i;
         }

         // Measure the start time
         long startTime = System.nanoTime();

         // Calculate the sum of the array
         long totalSum = 0;
         for (int j : array) {
            totalSum += j;
         }

         // Measure the end time
         long endTime = System.nanoTime();

         // Calculate the duration in milliseconds
         long duration = (endTime - startTime) / 1_000_000;

         // Print the total sum and execution time
         System.out.println("Total sum: " + totalSum);
         System.out.println("Execution time: " + duration + " ms");

         // Update the average time
         if (k > 0) {
            averageTime += duration;
         }
      }
      // Print the average execution time
      System.out.printf("Average time: %d ms", averageTime / 19);
   }
}
