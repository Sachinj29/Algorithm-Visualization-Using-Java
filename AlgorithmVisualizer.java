import java.awt.*;
import java.util.Random;
import javax.swing.*;

public class AlgorithmVisualizer extends JPanel {
    private int[] array; // Data array
    private final int BAR_WIDTH = 20; // Width of each bar
    private final int DELAY = 500; // Animation delay in ms
    private Thread animationThread; // Thread to handle animation
    private boolean isPaused = false; // Play/Pause control
    private final Color DEFAULT_COLOR = new Color(70, 130, 180); // Default color for bars
    private Color[] colors; // To store colors for each bar

    public AlgorithmVisualizer() {
        array = generateRandomArray(20, 10, 100); // Initialize with random values
        colors = new Color[array.length];
        for (int i = 0; i < array.length; i++) {
            colors[i] = DEFAULT_COLOR; // Set default color for all bars
        }
        setPreferredSize(new Dimension(array.length * BAR_WIDTH, 300));
    }

    // Generate random array for visualization
    private int[] generateRandomArray(int size, int min, int max) {
        Random rand = new Random();
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = rand.nextInt(max - min + 1) + min;
        }
        return arr;
    }

    // Bubble Sort Visualization
    public void visualizeBubbleSort() {
        animationThread = new Thread(() -> {
            try {
                for (int i = 0; i < array.length - 1; i++) {
                    for (int j = 0; j < array.length - i - 1; j++) {
                        if (isPaused) waitForResume(); // Pause functionality
                        colors[j] = Color.RED; // Mark bars to be compared as red
                        colors[j + 1] = Color.RED;
                        repaint(); // Update visualization
                        if (array[j] > array[j + 1]) {
                            // Swap
                            int temp = array[j];
                            array[j] = array[j + 1];
                            array[j + 1] = temp;
                            repaint(); // Update visualization
                            Thread.sleep(DELAY); // Delay for animation
                        }
                        // Reset color back to default after comparison
                        colors[j] = DEFAULT_COLOR;
                        colors[j + 1] = DEFAULT_COLOR;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        animationThread.start();
    }

    // Selection Sort Visualization
    public void visualizeSelectionSort() {
        animationThread = new Thread(() -> {
            try {
                for (int i = 0; i < array.length - 1; i++) {
                    int minIndex = i;
                    for (int j = i + 1; j < array.length; j++) {
                        if (isPaused) waitForResume();
                        colors[j] = Color.YELLOW; // Mark the current comparison element as yellow
                        repaint();
                        if (array[j] < array[minIndex]) {
                            minIndex = j;
                        }
                    }
                    // Swap the minimum element
                    int temp = array[minIndex];
                    array[minIndex] = array[i];
                    array[i] = temp;
                    repaint();
                    Thread.sleep(DELAY);
                    colors[minIndex] = DEFAULT_COLOR; // Reset color after swap
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        animationThread.start();
    }

    // Insertion Sort Visualization
    public void visualizeInsertionSort() {
        animationThread = new Thread(() -> {
            try {
                for (int i = 1; i < array.length; i++) {
                    int key = array[i];
                    int j = i - 1;
                    while (j >= 0 && array[j] > key) {
                        array[j + 1] = array[j];
                        j--;
                        repaint();
                        Thread.sleep(DELAY);
                    }
                    array[j + 1] = key;
                    repaint();
                    Thread.sleep(DELAY);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        animationThread.start();
    }

    // Quick Sort Visualization
    public void visualizeQuickSort() {
        animationThread = new Thread(() -> {
            try {
                quickSort(0, array.length - 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        animationThread.start();
    }

    private void quickSort(int low, int high) throws InterruptedException {
        if (low < high) {
            int pivotIndex = partition(low, high);
            quickSort(low, pivotIndex - 1);
            quickSort(pivotIndex + 1, high);
        }
    }

    private int partition(int low, int high) throws InterruptedException {
        int pivot = array[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (isPaused) waitForResume();
            colors[j] = Color.GREEN; // Mark the element being compared with pivot as green
            repaint();
            if (array[j] < pivot) {
                i++;
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
                repaint();
                Thread.sleep(DELAY);
            }
            colors[j] = DEFAULT_COLOR; // Reset color after comparison
        }
        int temp = array[i + 1];
        array[i + 1] = array[high];
        array[high] = temp;
        repaint();
        Thread.sleep(DELAY);
        return i + 1;
    }

    // Merge Sort Visualization
    public void visualizeMergeSort() {
        animationThread = new Thread(() -> {
            try {
                mergeSort(0, array.length - 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        animationThread.start();
    }

    private void mergeSort(int left, int right) throws InterruptedException {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(left, mid);
            mergeSort(mid + 1, right);
            merge(left, mid, right);
        }
    }

    private void merge(int left, int mid, int right) throws InterruptedException {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        int[] leftArray = new int[n1];
        int[] rightArray = new int[n2];

        System.arraycopy(array, left, leftArray, 0, n1);
        System.arraycopy(array, mid + 1, rightArray, 0, n2);

        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (leftArray[i] <= rightArray[j]) {
                array[k] = leftArray[i];
                i++;
            } else {
                array[k] = rightArray[j];
                j++;
            }
            k++;
            repaint();
            Thread.sleep(DELAY);
        }

        while (i < n1) {
            array[k] = leftArray[i];
            i++;
            k++;
            repaint();
            Thread.sleep(DELAY);
        }

        while (j < n2) {
            array[k] = rightArray[j];
            j++;
            k++;
            repaint();
            Thread.sleep(DELAY);
        }
    }

    // Pause/Resume logic
    private synchronized void waitForResume() throws InterruptedException {
        while (isPaused) {
            wait();
        }
    }

    public synchronized void togglePause() {
        isPaused = !isPaused;
        if (!isPaused) {
            notifyAll(); // Resume if paused
        }
    }

    // Reset the visualization
    public void resetVisualization() {
        array = generateRandomArray(20, 10, 100); // Reset array to random values
        for (int i = 0; i < array.length; i++) {
            colors[i] = DEFAULT_COLOR; // Reset bar colors
        }
        repaint(); // Refresh the panel
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < array.length; i++) {
            g.setColor(colors[i]);
            g.fillRoundRect(i * BAR_WIDTH, getHeight() - array[i], BAR_WIDTH, array[i], 10, 10); // Rounded corners
            g.setColor(Color.BLACK);
            g.drawRoundRect(i * BAR_WIDTH, getHeight() - array[i], BAR_WIDTH, array[i], 10, 10); // Rounded corners border
        }
    }

    public static void main(String[] args) {
        // Create frame and set up components
        JFrame frame = new JFrame("Algorithm Visualizer");
        AlgorithmVisualizer visualizer = new AlgorithmVisualizer();

        // Create JComboBox for selecting sorting algorithm
        String[] sortingAlgorithms = {"Bubble Sort", "Selection Sort", "Insertion Sort", "Quick Sort", "Merge Sort"};
        JComboBox<String> algorithmSelector = new JComboBox<>(sortingAlgorithms);
        algorithmSelector.setPreferredSize(new Dimension(150, 30));

        // Create Buttons with Tooltips
        JButton startButton = new JButton("Start Visualization");
        startButton.setToolTipText("Start the selected sorting algorithm visualization");
        startButton.addActionListener(e -> {
            String selectedAlgorithm = (String) algorithmSelector.getSelectedItem();
            switch (selectedAlgorithm) {
                case "Bubble Sort":
                    visualizer.visualizeBubbleSort();
                    break;
                case "Selection Sort":
                    visualizer.visualizeSelectionSort();
                    break;
                case "Insertion Sort":
                    visualizer.visualizeInsertionSort();
                    break;
                case "Quick Sort":
                    visualizer.visualizeQuickSort();
                    break;
                case "Merge Sort":
                    visualizer.visualizeMergeSort();
                    break;
            }
        });

        JButton resetButton = new JButton("Reset");
        resetButton.setToolTipText("Reset the array and visualization");
        resetButton.addActionListener(e -> visualizer.resetVisualization());

        JButton pauseButton = new JButton("Pause/Resume");
        pauseButton.setToolTipText("Pause or resume the sorting visualization");
        pauseButton.addActionListener(e -> visualizer.togglePause());

        // Layout Panel
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.add(new JLabel("Select Sorting Algorithm:"));
        panel.add(algorithmSelector);
        panel.add(startButton);
        panel.add(resetButton);
        panel.add(pauseButton);

        // Set up frame
        frame.setLayout(new BorderLayout());
        frame.add(visualizer, BorderLayout.CENTER);
        frame.add(panel, BorderLayout.SOUTH);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
