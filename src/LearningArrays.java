public class LearningArrays {
    public static void main(String[] args) {
        LearningArrays x = new LearningArrays();
    }
    public int[] nums = new int[100];

    public LearningArrays(){
        nums[0] = 7;
        nums[1] = 10;
        nums[2] = 52;
        nums[3] = 6;
        for(int y =4;y<nums.length;y++){
            nums[y] = (int) (100*Math.random());
        }
        displayNums();
    }
    void displayNums(){
        for(int x =0;x<=nums.length-1;x++){
            System.out.println(nums[x]);
        }
    }
}
