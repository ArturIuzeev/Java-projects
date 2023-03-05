package search;

public class BinarySearch {
    public static void main(String[] args) {
        // args.length() >= 1
        int x = Integer.parseInt(args[0]);
        // x = (int) args[0] && args.length() >= 1
        int[] numbers = new int[args.length - 1];
        // numbers.length() == args.length() - 1
        for (int i = 0; i < args.length - 1;i++) {
            // numbers.length() == args.length() - 1 && i < args.length - 1
            numbers[i] = Integer.parseInt(args[i + 1]);
            //numbers[i] = (int) args[i + 1] && numbers.length() == args.length() - 1
        }
        int result = Functions.IterativeSearch(numbers,x);
//        int result = Functions.RecursiveSearch(numbers,x,-1, numbers.length);
        // result = min(index) && numbers[index] <= x
        System.out.println(result);
    }
}
