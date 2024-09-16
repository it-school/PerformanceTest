import java.util.Arrays;
import java.util.Random;

/**
 * Problem Statement: for a given large, one-dimensional array (vector) and a two-dimensional array (matrix), perform matrix-vector multiplication. The result is another one-dimensional array.
 * Sequential implementation iterates through each row of the matrix.
 * For each row, it calculates the dot product with the vector and stores the result in the corresponding element of the result vector.
 * The dot product is calculated using a simple multiplication and addition operation.
 * The code uses nested loops to iterate through the matrix and vector.
 * Parallel implementation divides the matrix into chunks and assigns each chunk to a separate thread.
 * Threads compute their respective portions of the result vector concurrently.
 * The code creates multiple threads based on the all available processors.
 * Each thread processes a specific range of rows in the matrix.
 * Synchronization is used to ensure that the threads access the result vector correctly.
 * Performance Comparison
 * To compare the performance of the sequential and parallel implementations, we generate a large matrix and vector.
 * Then we measure the execution time for both sequential and parallel implementations.
 */

public class MatrixVectorMultiplication {
   public static void main(String[] args) {
      long start = System.nanoTime();

      int rows = 1_000_000;
      int cols = 1000;

      // Generate random matrix
      double[][] matrix = new double[rows][cols];
      double[] vector = new double[cols];

      Random random = new Random();
      for (int i = 0; i < rows; i++) {
         for (int j = 0; j < cols; j++) {
            matrix[i][j] = random.nextDouble();
         }
      }

      // Generate random vector
      for (int i = 0; i < cols; i++) {
         vector[i] = random.nextDouble();
      }

      long end = System.nanoTime();

      long startTime = System.nanoTime();
      double[] sequentialResult = matrixVectorMultiplicationSequential(matrix, vector);
      long endTime = System.nanoTime();
      double sequentialTime = (endTime - startTime) / 1000000.0;

      startTime = System.nanoTime();
      double[] parallelResult = matrixVectorMultiplicationParallel(matrix, vector);
      endTime = System.nanoTime();
      double parallelTime = (endTime - startTime) / 1000000.0;

      // Verify results equality
      boolean resultsMatch = Arrays.equals(sequentialResult, parallelResult);

      if (resultsMatch) {
         System.out.println("Generation time: " + (end - start) / 1000000.0 + " ms");
         System.out.println("Results are match to each other");
         System.out.println("Sequential processing time: " + sequentialTime + " ms");
         System.out.println("Parallel processing time: " + parallelTime + " ms");
      }
   }

   /**
    * Performs matrix-vector multiplication sequentially.
    *
    * @param matrix The matrix (number of rows x number of columns).
    * @param vector The vector (number of columns).
    *
    * @return The result vector (number of rows).
    */
   public static double[] matrixVectorMultiplicationSequential(double[][] matrix, double[] vector) {
      int rows = matrix.length;
      int cols = matrix[0].length;
      double[] result = new double[rows];

      // Iterate over each row in the matrix and compute the dot product
      for (int i = 0; i < rows; i++) {
         double sum = 0.0;

         // Iterate over each element in the row and column and compute the dot product
         for (int j = 0; j < cols; j++) {
            // Compute the dot product of the current row and column
            sum += matrix[i][j] * vector[j];
         }

         // Store the result in the result vector
         result[i] = sum;
      }

      return result;
   }

   /**
    * Performs matrix-vector multiplication in parallel using threads.
    *
    * @param matrix The matrix.
    * @param vector The vector.
    *
    * @return The result vector.
    */
   public static double[] matrixVectorMultiplicationParallel(double[][] matrix, double[] vector) {
      int rows = matrix.length;
      int cols = matrix[0].length;
      double[] result = new double[rows];
      int numThreads = Runtime.getRuntime().availableProcessors();
      System.out.println(numThreads + " threads will be used");

      // Divide the matrix into chunks for each thread
      int chunkSize = rows / numThreads;

      Thread[] threads = new Thread[numThreads];
      for (int i = 0; i < numThreads; i++) {
         int startIndex = i * chunkSize;
         int endIndex;
         if (i == numThreads - 1) {
            // Assign the last chunk to the last thread
            endIndex = rows - 1;
         }
         else {
            // Assign the next chunk to the next thread
            endIndex = (i + 1) * chunkSize - 1;
         }

         // Create a new thread to compute the result for the chunk
         threads[i] = new Thread(() -> {
            // Iterate over each row in the chunk
            for (int row = startIndex; row <= endIndex; row++) {
               for (int col = 0; col < cols; col++) {
                  // Compute the dot product of the current row and column and store the result in the result vector
                  result[row] += matrix[row][col] * vector[col];
               }
            }
         });
         threads[i].start();
      }

      // Wait for all threads to finish
      for (Thread thread : threads) {
         try {
            thread.join();
         } catch (InterruptedException e) {
            e.printStackTrace();
         }
      }

      return result;
   }
}