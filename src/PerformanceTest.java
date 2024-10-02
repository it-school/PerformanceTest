import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

public class PerformanceTest {
   public static void main(String[] args) {
      summaryStatisticsTest();

   }

   private static void summaryStatisticsTest() {
      final int ITERATIONS = 500_000_000;
      SummaryStatistics stats = new SummaryStatistics();
      long start_time = System.currentTimeMillis();

      for (int i = 0; i < ITERATIONS; i++) {
         stats.addValue(i + 1);
         stats.addValue(i + 2);
         stats.addValue(i + 3);
         stats.addValue(i + 4);
         stats.addValue(i + 5);
         stats.getMean();
         stats.getStandardDeviation();
         stats.getMin();
         stats.getMax();
         stats.getN();
         stats.getSum();
      }

      long end_time = System.currentTimeMillis();
      double total_time = end_time - start_time;

      // выводим статистические данные
      System.out.println("Время выполнения: " + total_time + " мс");
      System.out.println("Количество значений: " + stats.getN());
      System.out.println("Минимальное значение: " + stats.getMin());
      System.out.println("Максимальное значение: " + stats.getMax());
      System.out.println("Среднее значение: " + stats.getMean());
      System.out.println("Отклонение: " + stats.getStandardDeviation());
      System.out.println("Сумма значений: " + stats.getSum());

      System.out.println("\n" + stats);
   }
}

