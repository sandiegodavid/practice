import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.function.BinaryOperator;

public class QuickSort {
	static int ARRAY_SIZE = 10000;
	static int a[] = new int[ARRAY_SIZE];
	static String inputFileName = "resources/quicksort.txt";
	static int comparisonCount = 0;

	public static void main(String args[]) {
		BinaryOperator<Integer> pivotByFirst = (l, r) -> a[l];
		qs(pivotByFirst, "pivotByFirst");
		BinaryOperator<Integer> pivotByLast = (l, r) -> {
			int p = a[r - 1];
			a[r - 1] = a[l];
			return p;
		};
		qs(pivotByLast, "pivotByLast");
		BinaryOperator<Integer> pivotByMedianOfThree = (l, r) -> {
			int pi = chooseMedian(l, r - 1);
			int p = a[pi];
			a[pi] = a[l];
			return p;
		};
		qs(pivotByMedianOfThree, "pivotByMedianOfThree");
		BinaryOperator<Integer> pivotByRandom = (l, r) -> {
			int pi = l + (int) (Math.random() * (r - l));
			int p = a[pi];
			a[pi] = a[l];
			return p;
		};
		qs(pivotByRandom, "pivotByRandom");
	}

	private static void qs(BinaryOperator<Integer> pivotGetter, String pivotGetterName) {
		readInput();
		qsPartition(0, ARRAY_SIZE, pivotGetter);
		System.out.println(pivotGetterName + " comparisonCount: " + comparisonCount);
	}

	private static void qsPartition(int l, int r, BinaryOperator<Integer> pivotGetter) {
		if (r - l < 2) {
			return;
		}
		if (r - l == 2) {
			comparisonCount++;
			if (a[l] > a[l + 1]) {
				swap(l, l + 1);
			}
			return;
		}

		comparisonCount += r - 1 - l;
		int p = pivotGetter.apply(l, r);
		int i = l + 1;
		for (int j = l + 1; j < r; j++) {
			if (a[j] < p) {
				swap(i, j);
				i++;
			}
		}
		a[l] = a[i - 1];
		a[i - 1] = p;
		qsPartition(l, i - 1, pivotGetter);
		qsPartition(i, r, pivotGetter);
	}

	private static int chooseMedian(int l, int r) {
		int m = (l + r) / 2;
		if ((a[l] - a[m]) * (a[l] - a[r]) < 0) {
			return l;
		}
		if ((a[m] - a[l]) * (a[m] - a[r]) < 0) {
			return m;
		}
		return r;
	}

	private static void swap(int i, int j) {
		int s = a[i];
		a[i] = a[j];
		a[j] = s;
	}

	private static void readInput() {
		comparisonCount = 0;
		try (FileReader r = new FileReader(inputFileName); BufferedReader br = new BufferedReader(r);) {
			String is;
			for (int i = 0; (i < ARRAY_SIZE) && ((is = br.readLine()) != null); i++) {
				a[i] = Integer.valueOf(is);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("can't file input: " + inputFileName);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("can't read file input: " + inputFileName);
		}
	}
}
