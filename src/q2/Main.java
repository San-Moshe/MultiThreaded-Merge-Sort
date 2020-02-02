package q2;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter array size");
        int numberOfElements = scanner.nextInt();
        int[] rndArray = generateRandomNumbers(numberOfElements);

        System.out.println("Enter number of threads");
        int numberOfThreads = scanner.nextInt();

        DataViewModel viewModel = new DataViewModel(rndArray, numberOfThreads);

        int[] mergedArray = viewModel.mergeSort();
        System.out.println("\n\n\n\n\n");

        System.out.println("Original array " + Arrays.toString(rndArray));
        System.out.println("Array after multi-thread merge-sort: " + Arrays.toString(mergedArray));

        List<Integer> sortedList = Arrays.stream(rndArray).boxed().sorted(Integer::compareTo).collect(Collectors.toList());
        System.out.println("Original array after java-framework sort" + Arrays.toString(sortedList.toArray()));
        System.out.println("Checking for string equality on java-sort and our sort = " + Arrays.toString(mergedArray).equals(Arrays.toString(sortedList.toArray())));
    }

    private static int[] generateRandomNumbers(int numOfNumbers) {
        Random rnd = new Random();
        int[] resultArray = new int[numOfNumbers];
        for (int i = 0; i < numOfNumbers; i++) {
            resultArray[i] = rnd.nextInt(99) + 1;
        }

        return resultArray;
    }
}
