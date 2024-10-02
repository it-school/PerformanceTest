import java.util.stream.IntStream;

public class EquationOptimization {
   /**
    * Computes the recursive function g(x), which is the sum of the
    * series 1 + x/2 + x/4 + x/8 + ... until x becomes less than 1.
    *
    * @param x input value
    *
    * @return the sum of the series
    */
   // Recursive function g(x)
   public static double g(double x) {
      if (x < 1) {
         // Base case: if x is less than 1, return 1
         return 1;
      }
      else {
         // Recursive case: return x/2 + g(x/2)
         return x / 2 + g(x / 2);
      }
   }

   /**
    * Computes the complex equation using a loop, with safety checks to
    * avoid NaN and overflow.
    *
    * @param x input value
    * @param n number of terms to sum
    *
    * @return the sum of terms
    */
   // Sequential version with safety checks to avoid NaN
   public static double complexEquationSafe(double x, int n) {
      double result = 0.0;

      for (int i = 1; i <= n; i++) {
         // Ensure positive arguments for logarithms
         double logArg = x + i;
         if (logArg <= 0) {
            logArg = 1e-10;  // Small positive value to avoid NaN
         }
         // Avoid overflow with large exponentiation
         double term1 = Math.pow(x, Math.min(i, 100)) * Math.log(logArg) / (Math.sin(x + Math.pow(i, 2)) + 1e-10);

         // Avoid overflow with large exponentiation and handle sqrt inputs
         double sqrtArg = x + i;
         if (sqrtArg < 0) {
            sqrtArg = 0;  // Avoid imaginary numbers from sqrt
         }
         double term2 = Math.exp(Math.cos(x)) * Math.sqrt(sqrtArg);

         result += term1 + term2;
      }

      // Add recursive g(x)
      result += g(x);

      return result;
   }

   /**
    * Computes the complex equation in parallel using a parallel stream with safety checks.
    * <p>
    * The computation is parallelized using a parallel stream, with the same safety checks as in the sequential version.
    * The result is the sum of the terms computed in parallel.
    *
    * @param x input value
    * @param n number of terms to sum
    *
    * @return the sum of terms
    */
   // Parallelized version with safety checks
   public static double complexEquationParallelSafe(double x, int n) {
      return IntStream.range(1, n + 1)
              .parallel()  // Parallelize the computation
              .mapToDouble(i -> {
                 // Ensure positive arguments for logarithms
                 double logArg = x + i;
                 if (logArg <= 0) {
                    logArg = 1e-10;  // Small positive value to avoid NaN
                 }

                 // First term: pow(x, i) * log(x + i) / sin(x + i^2)
                 double term1 = Math.pow(x, Math.min(i, 100)) * Math.log(logArg)
                         / (Math.sin(x + Math.pow(i, 2)) + 1e-10);

                 // Avoid overflow with large exponentiation and handle sqrt inputs
                 double sqrtArg = x + i;
                 if (sqrtArg < 0) {
                    sqrtArg = 0;  // Avoid imaginary numbers from sqrt
                 }

                 // Second term: exp(cos(x)) * sqrt(x + i)
                 double term2 = Math.exp(Math.cos(x)) * Math.sqrt(sqrtArg);

                 return term1 + term2;
              })
              .sum() + g(x);  // Add recursive g(x)
   }

   /**
    * Tests the complex equation by computing it with a large number of iterations and both sequential and parallel versions.
    * <p>
    * The test uses a sample value of x and a large number of iterations for the complex equation.
    * Both the sequential version with safety checks and the parallel version with safety checks are tested.
    * The test prints the execution time for both versions.
    *
    * @param args the command line arguments
    */
   public static void main(String[] args) {
      double x = 10.0;     // Sample value of x
      int n = 1_000_000;   // Large number of iterations for computational complexity

      // Sequential version with safety checks
      long startTimeSequential = System.nanoTime();
      double resultSequential = complexEquationSafe(x, n);
      long endTimeSequential = System.nanoTime();
      System.out.println("Result of the complex equation (Sequential with Safety): " + resultSequential);
      System.out.println("Execution time (Sequential with Safety): " + (endTimeSequential - startTimeSequential) / 1_000_000 + " ms");

      // Parallel version with safety checks
      long startTimeParallel = System.nanoTime();
      double resultParallel = complexEquationParallelSafe(x, n);
      long endTimeParallel = System.nanoTime();
      System.out.println("Result of the complex equation (Parallel with Safety): " + resultParallel);
      System.out.println("Execution time (Parallel with Safety): " + (endTimeParallel - startTimeParallel) / 1_000_000 + " ms");
   }
}
