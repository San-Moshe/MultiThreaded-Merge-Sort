package q1;

public interface IMatrixViewModel {
    void startGradualReduction(int numberOfThreads, int numberOfRuns);

    void clear();

    void toggleState(int row, int col);
}
