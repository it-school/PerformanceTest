public class FormulaOptimization {

   // Изначальная реализация
   public static double calculateOriginal(double x, double y) {
      return (3 * x * x + 5 * x) * (y * y - y) +
              (2 * x * x + x) * (2 * y + 1) +
              Math.pow(x + y, 2) / 2;
   }

   // Оптимизация 1: Вынос повторяющихся выражений в переменные
   public static double calculateOptimized1(double x, double y) {
      double x2 = x * x;  // x^2
      double y2 = y * y;  // y^2
      double xy = x + y;  // x + y
      double yyMinusY = y2 - y;  // y^2 - y

      return (3 * x2 + 5 * x) * yyMinusY +
              (2 * x2 + x) * (2 * y + 1) +
              (xy * xy) / 2;
   }

   // Оптимизация 2: Вынесение сложных выражений в отдельные переменные
   public static double calculateOptimized2(double x, double y) {
      double x2 = x * x;  // x^2
      double y2 = y * y;  // y^2
      double xy = x + y;  // x + y
      double yyMinusY = y2 - y;  // y^2 - y

      // Предварительное вычисление часто встречающихся выражений
      double term1 = 3 * x2 + 5 * x;  // 3x^2 + 5x
      double term2 = 2 * x2 + x;      // 2x^2 + x
      double term3 = 2 * y + 1;       // 2y + 1

      return term1 * yyMinusY +
              term2 * term3 +
              (xy * xy) / 2;
   }

   // Оптимизация 3: Использование Math и предварительное вычисление выражений
   public static double calculateOptimized3(double precomputedTerm1, double precomputedTerm2, double precomputedTerm3, double precomputedXY, double yyMinusY) {
      return precomputedTerm1 * yyMinusY +
              precomputedTerm2 * precomputedTerm3 +
              Math.pow(precomputedXY, 2) / 2;
   }

   // Метод для предварительного вычисления выражений
   public static double[] precomputeValues(double x, double y) {
      double x2 = x * x;  // x^2
      double y2 = y * y;  // y^2
      double xy = x + y;  // x + y
      double yyMinusY = y2 - y;  // y^2 - y

      // Предварительные вычисления
      double term1 = 3 * x2 + 5 * x;  // 3x^2 + 5x
      double term2 = 2 * x2 + x;      // 2x^2 + x
      double term3 = 2 * y + 1;       // 2y + 1

      return new double[]{term1, term2, term3, xy, yyMinusY};
   }

   // Метод для измерения времени выполнения
   public static void measurePerformance(String version, double x, double y) {
      long startTime = System.nanoTime();
      double result = switch (version) {
         case "original" -> calculateOriginal(x, y);
         case "optimized1" -> calculateOptimized1(x, y);
         case "optimized2" -> calculateOptimized2(x, y);
         case "optimized3" -> {
            double[] precomputedValues = precomputeValues(x, y);
            yield calculateOptimized3(precomputedValues[0], precomputedValues[1], precomputedValues[2], precomputedValues[3], precomputedValues[4]);
         }
         default -> 0;
      };

      long endTime = System.nanoTime();
      double duration = (endTime - startTime) / 1_000_000.0;  // Время в миллисекундах

      System.out.println("Version: " + version + " | Result: " + result + " | Execution time: " + duration + " ms");
   }

   public static void main(String[] args) {
      double x = 3.0;
      double y = 4.0;

      // Измерение производительности каждой версии
      measurePerformance("original", x, y);
      measurePerformance("optimized1", x, y);
      measurePerformance("optimized2", x, y);
      measurePerformance("optimized3", x, y);
   }
}
