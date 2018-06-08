package org.bql.utils;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayUtils {
    public static <T> List<T> arrayToList(T[] element){
        if(element.length <=0)
            return null;
        List<T> list = new ArrayList<>();
        for(T t : element){
            list.add(t);
        }
        return list;
    }
    public static <T>  T[] add(T[] arr,T obj){
        if(arr == null)
            throw new NullPointerException("出入的数组为空");
        int length = Array.getLength(arr);
        if(obj == null)
            return arr;
        T[] newArray = (T[]) Array.newInstance(obj.getClass(), length+1);
        newArray[0] = obj;
        System.arraycopy(arr, 0, newArray, 1,length);
        return newArray;
    }
    public static <T extends Comparable> boolean contains(T[] arr,T element){
        if(arr.length<=0)
            return false;
        for(int i = 0; i<arr.length;i++){
            if(element.equals(arr[i]))
                return true;
        }
        return false;
    }
    public static <T extends Comparable> boolean contains(T[] arr,T[] element){
        if(arr.length != element.length)
            return false;
        try {
            Arrays.sort(arr);
            Arrays.sort(element);
        }catch (NullPointerException e){
            throw new NullPointerException("数组元素不能有空值");
        }
        for(int i = 0;i<arr.length;i++){
            T t1 = arr[i];
            T t2 = element[i];
            if(!t1.equals(t2))
                return false;
        }
        return true;
    }
    public static <T> T[] clear(T[] t){
        for(int i = 0;i<t.length;i++){
            t[i] = null;
        }
        return t;
    }
    public static <T> List<T> arrToList(T[] t){
        if(t == null){
            throw new NullPointerException("传入的数组不能为空");
        }
        List<T> list = new ArrayList<>(t.length);
        for(T tt:t){
            list.add(tt);
        }
        return list;
    }
    public static <T> T[] arrForList(List<T> t){
        if(t == null || t.size() <= 0)
            throw new NullPointerException("传入的集合为空");
        int length = t.size();
        T[] tt = (T[]) Array.newInstance(t.getClass(),length);
        for(int i = 0;i<length;i++){
            tt[i] = t.get(i);
        }
        return tt;
    }
    public static <T> void set(T[] arr,T src,T dest){
        for(int i = 0;i<arr.length;i++){
            if(arr[i].equals(src)){
                arr[i] = dest;
            }
        }
    }
    public static void main(String[] args) {
//        Integer[] i = new Integer[]{1,2,3,4,5,6};
//        System.out.println(Arrays.toString(add(i,9)));
        Integer[] i = new Integer[]{1,3,2};
        set(i,2,4);
        System.out.println(Arrays.toString(i));
    }
}
