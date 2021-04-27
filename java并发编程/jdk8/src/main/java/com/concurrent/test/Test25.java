//import java.util.Arrays;
//import java.util.Scanner;
//public class test{
//
//    public static void main(String[] args){
//        Scanner scanner = new Scanner(System.in);
//        int n = scanner.nextInt();
//        int m = scanner.nextInt();
//        int[] nums = new int[n];
//        for(int i=0; i<n; i++){
//            nums[i] = scanner.nextInt();
//        }
//        Arrays.sort(nums);
//        for(int i =0; i<m ; i++){
//            int temp = scanner.nextInt();
//            int left  = getLeft(nums, temp);
//            int right = getRight(nums, temp);
//            System.out.println(left + " "+  right);
//        }
//
//    }
//
//
//
//    // 寻找第一个出现的数字和最后一个出现的位置
//    public static int getRight (int[] nums , int target){
//        int left =0; int right = nums.length-1;
//        while(left<=right){
//            int mid = left + (right -left)/2;
//            if(nums[mid] < target){
//                left = mid +1;
//            }else if(nums[mid] > target){
//                right = mid -1;
//            }else{
//                left = mid +1;
//            }
//        }
//        if(right < 0 || nums[right]!=target){
//            return 0;
//        }
//        return right;
//    }
//
//
//    public static int getLeft(int[] nums, int target){
//        int left =0; int right = nums.length -1;
//
//        while(left <=right ){
//            int mid = left + (right -left)/2;
//
//            if(nums[mid] < target){
//                left = mid +1;
//            }else if(nums[mid] > target){
//                right = mid -1;
//            }else{
//                right = mid -1;
//            }
//        }
//        if(left >= nums.length || nums[left] != target){
//            return 0;
//        }
//        return left;
//    }
//
//
//}
//
//
