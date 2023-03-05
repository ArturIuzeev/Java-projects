package search;

public class Functions {

    // Pred: numbers[0] >= numbers[1] >= ... >= numbers[n]
    // Post: min(r') == r
    public static int IterativeSearch(int[] numbers,int x) {
        int l = -1;
        int r = numbers.length;
        while (l < r - 1) {
            // min(r') == r && l' < r' - 1
            int m = (l + r) / 2;
            if (numbers[m] <= x) {
                // min(r') == r && l' < r' - 1 && numbers[m'] <= x
                // min(r') == r && numbers[m'] <= x
                r = m;
                // min(r') == r && numbers[r'] <= x
            } else {
                // min(r') == r && l' < r' - 1 && numbers[m'] > x
                // min(r') == r && numbers[m'] > x
                l = m;
                // min(r') == r && numbers[l'] > x
            }
            // min(r') == r && l' >= r' - 1
        }
        // min(r') == r
        return r;
    }

    // Pred: l' == -1 && r' == numbers.length && numbers[0] >= numbers[1] >= ... >= numbers[n]
    // Post: min(r') == r
    public static int RecursiveSearch(int[] numbers, int x, int l,int r) {
        // min(r') == r
        if (l < r - 1) {
            // min(r') == r && l' < r' - 1
            int m = (l + r) / 2;
            if (numbers[m] <= x) {
                // min(r') == r && l' < r' - 1 && numbers[m'] <= x
                // min(r') == r && numbers[m'] <= x
                r = m;
                // min(r') == r && numbers[r'] <= x
            } else {
                // min(r') == r && l' < r' - 1 && numbers[m'] > x
                // min(r') == r && numbers[m'] > x
                l = m;
                // min(r') == r && numbers[l'] > x
            }
            // min(r') == r && l' < r' - 1 && return RecursiveSearch(numbers, x, l', r');
            return RecursiveSearch(numbers, x, l, r);
        }
        // min(r') == r
        return r;
    }

    //Post: numbers[0] >= numbers[1] >= ... >= numbers[n]
    //Pred :min(r') = r
    public static int MissingSearch(int[] numbers, int x) {
        int l = -1;
        int r = numbers.length;
        while (l < r - 1) {
            // min(r') == r && l' < r' - 1
            int m = (l + r) / 2;
            if (numbers[m] >= x) {
                // min(r') == r && l' < r' - 1 && numbers[m'] >= x
                // min(r') == r && numbers[m'] >= x
                r = m;
                // min(r') == r && numbers[r'] >= x
            } else {
                // min(r') == r && l' < r' - 1 && numbers[m'] < x
                // min(r') == r && numbers[m'] < x
                l = m;
                // min(r') == r && numbers[l'] < x
            }
            // min(r') == r && l' >= r' - 1
        }
        if (r < numbers.length) {
            // l >= r - 1 && r < numbers.length
            if (x == numbers[r]) {
                // l >= r - 1 && r < numbers.length && x == numbers[r]
                return r;
            } else {
                // l >= r - 1 && r < numbers.length && x != numbers[r]
                return -r - 1;
            }
        } else {
            // l >= r - 1 && r >= numbers.length
            return -(numbers.length + 1);
        }
    }

    // :NOTE: нарушается при повторном вызове
    // запишите пред и пост условия математически, иначе у вас бред
    // :NOTE: исправления нет
    // Pred: l' == -1 && r' == numbers.length && numbers[0] >= numbers[1] >= ... >= numbers[n]
    // Post: min(r') == r
    public static int RecursiveSearchMissing(int[] numbers, int x, int l,int r) {
        // min(r') == r
        if (l < r - 1) {
            // min(r') == r && l' < r' - 1
            int m = (l + r) / 2;
            if (numbers[m] >= x) {
                // min(r') == r && l' < r' - 1 && numbers[m'] <= x
                // min(r') == r && numbers[m'] <= x
                r = m;
                // min(r') == r && numbers[r'] <= x
            } else {
                // min(r') == r && l' < r' - 1 && numbers[m'] > x
                // min(r') == r && numbers[m'] > x
                l = m;
                // min(r') == r && numbers[l'] > x
            }
            return RecursiveSearchMissing(numbers, x, l, r);
        }
        // min(r') == r && l >= r - 1
        if (r < numbers.length) {
            // l >= r - 1 && r < number.length
            if (x == numbers[r]) {
                // l >= r - 1 && r < number.length && x == numbers[r]
                return r;
            } else {
                // l >= r - 1 && r < number.length && x != numbers[r]
                return -r - 1;
            }
        } else {
            // l >= r - 1 && r >= number.length
            return -(numbers.length + 1);
        }
    }

}
