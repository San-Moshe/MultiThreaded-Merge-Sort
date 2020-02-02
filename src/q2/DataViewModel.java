package q2;

import java.util.ArrayList;
import java.util.List;

public class DataViewModel {
    private int[] dataArray;
    private volatile List<int[]> splitDataArray;
    private volatile List<int[]> tmpDataArray;
    private List<Thread> threadPool;
    private int numOfThreads;
    private volatile boolean isFinishedMerge;
    private volatile int activeThreadsCounter;
    private volatile int holdingThreadsCounter;
    private volatile int waitingThreads;

    public DataViewModel(int[] dataArray, int numOfThreads) {
        activeThreadsCounter = numOfThreads;
        waitingThreads = 0;
        holdingThreadsCounter = 0;
        this.numOfThreads = numOfThreads;
        this.dataArray = dataArray;
        splitDataArray = new ArrayList<>();
        splitDataArray(dataArray.length);
    }

    private void initThreadPool(int numOfThreads) {
        threadPool = new ArrayList<>();
        for (int i = 0; i < numOfThreads; i++) {
            threadPool.add(new Thread(() -> {
                while (!isFinishedMerge) {
                    List<int[]> inactiveArrays = getTwoInactiveArrays();
                    onThreadMergeFinished(merge(inactiveArrays.get(0), inactiveArrays.get(1)));
                }
            }));
        }
    }

    private synchronized void onThreadMergeFinished(int[] mergeResult) {
        activeThreadsCounter--;
        holdingThreadsCounter--;
        tmpDataArray.add(mergeResult);

        if (activeThreadsCounter > 0 && (activeThreadsCounter - holdingThreadsCounter >= splitDataArray.size() / 2)) {
            try {
                waitingThreads++;
                wait();
                waitingThreads--;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else if (splitDataArray.isEmpty() && tmpDataArray.size() == 1) {
            dataArray = tmpDataArray.get(0);
            isFinishedMerge = true;
        } else if (splitDataArray.isEmpty()) {
            splitDataArray.addAll(tmpDataArray);
            tmpDataArray.clear();
            activeThreadsCounter = Math.min(splitDataArray.size() / 2, waitingThreads);
            for (int i = 0; i < Math.min(splitDataArray.size() / 2, waitingThreads) - 1; i++) {
                notify();
            }
        } else {
            activeThreadsCounter++;
        }
    }

    private synchronized List<int[]> getTwoInactiveArrays() throws IndexOutOfBoundsException {
        List<int[]> resultList = new ArrayList<>();
        if (splitDataArray.size() >= 2) {
            resultList.add(splitDataArray.remove(0));
            resultList.add(splitDataArray.remove(0));
        } else if (splitDataArray.size() == 1) {
            resultList.add(splitDataArray.remove(0));
            resultList.add(new int[0]);
        }

        holdingThreadsCounter++;
        return resultList;
    }

    private int[] merge(int[] firstArray, int[] secondArray) {
        int[] resultArray = new int[firstArray.length + secondArray.length];
        int i = 0, j = 0, k = 0;

        while (i < firstArray.length && j < secondArray.length) {
            if (firstArray[i] < secondArray[j]) {
                resultArray[k++] = firstArray[i++];
            } else {
                resultArray[k++] = secondArray[j++];
            }
        }

        while (i < firstArray.length) {
            resultArray[k++] = firstArray[i++];
        }

        while (j < secondArray.length) {
            resultArray[k++] = secondArray[j++];
        }

        return resultArray;
    }

    private void splitDataArray(int numOfArrays) {
        for (int i = 0; i < numOfArrays; i++) {
            splitDataArray.add(new int[]{dataArray[i]});
        }
    }

    public int[] mergeSort() {
        isFinishedMerge = false;
        tmpDataArray = new ArrayList<>();
        initThreadPool(numOfThreads);
        threadPool.forEach(Thread::start);
        //TODO replace with lock on some condition
        while (!isFinishedMerge) {

        }
        return dataArray;
    }
}
