For a given large, one-dimensional array (vector) and a two-dimensional array (matrix), perform matrix-vector multiplication. The result is another one-dimensional array.
Sequential implementation iterates through each row of the matrix. For each row, it calculates the dot product with the vector and stores the result in the corresponding element of the result vector. The dot product is calculated using a simple multiplication and addition operation. The code uses nested loops to iterate through the matrix and vector.
Parallel implementation divides the matrix into chunks and assigns each chunk to a separate thread. Threads compute their respective portions of the result vector concurrently. The code creates multiple threads based on the all available processors. Each thread processes a specific range of rows in the matrix. Synchronization is used to ensure that the threads access the result vector correctly.
Performance Comparison To compare the performance of the sequential and parallel implementations, we generate a large matrix and vector. Then we measure the execution time for both sequential and parallel implementations.
<p>
Upd. Added sequential and parallel processing of equation
![image](https://github.com/user-attachments/assets/dc31f0c5-2d3d-49d2-9eea-578832b1b160)
<p>
Added Performance estimation of parallel execution for array element summation 
