package org.bql.utils;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomUtils {
    /**
     * 返回随机的index，[0,size-1]
     *
     * @param size
     * @return
     */
    public static int randomIndex(int size) {
        if (size <= 0) {
            throw new IndexOutOfBoundsException("无法获取index，数组越界！");
        }
        return ThreadLocalRandom.current().nextInt(size);
    }
    public static int randomSection(int startIndex,int endIndex){
        if (startIndex <= 0 || endIndex <= 0) {
            throw new IndexOutOfBoundsException("无法获取index，数组越界！");
        }
        return ThreadLocalRandom.current().nextInt(startIndex) + endIndex;
    }
    public static Integer[] randomNotRepeatForArr(Integer[] arr,int count){
        Integer[] randoms = new Integer[count];
        List<Integer> listRandom = new ArrayList<>();
        if(arr.length < count)
            throw new IndexOutOfBoundsException("数组长度不足");
        if(arr.length == count)
            return arr;

        // 从候选list中取出放入数组，已经被选中的就从这个list中移除
        for(int i = 0; i < count; i++){
            int index = getRandom(0, listRandom.size()-1);
            randoms[i] = listRandom.get(index);
            listRandom.remove(index);
        }
        return randoms;
    }
    /**
     * 获取随机不重复的索引
     * @return
     */
    /**
     * 根据min和max随机生成count个不重复的随机数组
     * @param min
     * @param max
     * @param count
     * @return int[]
     */
    public static Integer[] randomNotRepeat(int min, int max, int count){
        Integer[] randoms = new Integer[count];
        List<Integer> listRandom = new ArrayList<>();

        if( count > ( max - min + 1 )){
            return null;
        }
        // 将所有的可能出现的数字放进候选list
        for(int i = min; i <= max; i++){
            listRandom.add(i);
        }
        // 从候选list中取出放入数组，已经被选中的就从这个list中移除
        for(int i = 0; i < count; i++){
            int index = getRandom(0, listRandom.size()-1);
            randoms[i] = listRandom.get(index);
            listRandom.remove(index);
        }
        return randoms;
    }
    /**
     * 根据min和max随机生成一个范围在[min,max]的随机数，包括min和max
     * @param min
     * @param max
     * @return int
     */
    public static int getRandom(int min, int max){
        Random random = new Random();
        return random.nextInt( max - min + 1 ) + min;
    }
    public void printlsn(){
        for(int i = 0;i<10;i++) {
            System.out.println(randomIndex(2));
//            System.out.println(Arrays.toString(randomNotRepeat(1 ,4,3)));
        }
    }
}