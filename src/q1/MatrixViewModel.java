package q1;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class MatrixViewModel extends Observable implements IMatrixViewModel {
    private GameMatrix gameMatrix;
    private List<Thread> threadPool;
    private int threadsSharedPhaseCounter;

    public MatrixViewModel(int rows, int cols, Observer matrixObserver) {
        gameMatrix = new GameMatrix(rows, cols);
        threadsSharedPhaseCounter = 0;
        addObserver(matrixObserver);
    }

    @Override
    public void startGradualReduction(int numberOfThreads, int numberOfRuns) {
        initThreads(gameMatrix.getMatrix().length, gameMatrix.getMatrix().length, numberOfThreads, numberOfRuns);
        gameMatrix.freeze();
        setChanged();
        notifyObservers(gameMatrix.isFrozen());
        threadPool.forEach(Thread::start);
    }

    @Override
    public void clear() {
        gameMatrix.clear();
        setChanged();
        notifyObservers(gameMatrix.getMatrix());
    }

    @Override
    public void toggleState(int row, int col) {
        gameMatrix.toggleCellState(row, col);
        setChanged();
        notifyObservers(gameMatrix.getMatrix());
    }

    private void initThreads(int rows, int cols, int numOfThreads, int runs) {
        threadPool = new ArrayList<>();
        for (int i = 0; i < numOfThreads; i++) {
            int finalI = i;
            threadPool.add(new Thread(new Runnable() {
                private int avgNumberOfCells = cols * rows / numOfThreads;
                private int numberOfCellsForCurrThread = cols * rows / numOfThreads + ((finalI == numOfThreads - 1) ? cols * rows % numOfThreads : 0);
                private int startFlatIndex = finalI * avgNumberOfCells;
                private int endFlatIndex = startFlatIndex + numberOfCellsForCurrThread;
                private int xStartPos = startFlatIndex / cols;
                private int yStartPos = startFlatIndex % cols;
                private int xEndPos = endFlatIndex / cols;

                @Override
                public void run() {
                    for (int counter = 0; counter < runs; counter++) {
                        for (int colIndex = yStartPos; colIndex < cols; colIndex++) {
                            if (gameMatrix.hasWhiteNeighbour(xStartPos, colIndex)) {
                                gameMatrix.eatPixel(xStartPos, colIndex);
                            }
                        }

                        int cellsVisitedCounter = 1 + cols - yStartPos;
                        for (int i = xStartPos + 1; i <= xEndPos; i++) {
                            int colCellIndex = 0;
                            while (colCellIndex != cols && cellsVisitedCounter <= numberOfCellsForCurrThread) {
                                if (gameMatrix.hasWhiteNeighbour(i, colCellIndex)) {
                                    gameMatrix.eatPixel(i, colCellIndex);
                                }
                                colCellIndex++;
                                cellsVisitedCounter++;
                            }
                        }
                        onPhaseFinished(runs);
                    }
                }
            }));
        }
    }

    private synchronized void onPhaseFinished(int runs) {
        threadsSharedPhaseCounter++;

        if (threadsSharedPhaseCounter % threadPool.size() == 0) {
            gameMatrix.unfreezeMatrix();
            setChanged();
            notifyObservers(gameMatrix.getMatrix());
            try {
                Thread.sleep(2000);
                if (threadsSharedPhaseCounter == runs * threadPool.size()) {
                    setChanged();
                    notifyObservers(gameMatrix.isFrozen());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            notifyAll();
        } else {
            lockThreadsWithViewModel();
        }
    }

    private void lockThreadsWithViewModel() {
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
