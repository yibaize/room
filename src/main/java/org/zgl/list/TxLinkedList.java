package org.zgl.list;


/**
 * @作者： big
 * @创建时间： 2018/5/28
 * @文件描述：
 */
public class TxLinkedList {
    private Node first;
    private Node last;
    private int size;

    public void add(Object o) {
        final Node l = last;
        final Node newNode = new Node(l,o,null);
        last = newNode;
        if (l == null) {
            first = newNode;
        } else {
            l.next = newNode;
        }
        size++;
    }

    public Object get(int index) {
        return node(index).item;
    }
    private Node node(int index) {
        //如果index小于容量长度除以2 也就是 size/2那么从前往后找
        if (index < (size >> 1)) {
            Node x = first;
            for (int i = 0; i < index; i++)
                x = x.next;
            return x;
        } else {
            //否则从后往前找
            Node x = last;
            for (int i = size - 1; i > index; i--)
                x = x.prev;
            return x;
        }
    }

    public Object remove(int index) {
        Node temp = node(index);
        return unlink(temp);
    }
    public boolean remove(Object o){
        Node n = first;
        while (true){
            if(n == null)
                return false;
            if(o == null){
                if(n.item == null){
                    unlink(n);
                    return true;
                }
            }
            if(n.item.equals(o)) {
                unlink(n);
                return true;
            }
            n = n.next;
        }
    }
    private Object unlink(Node x){
        Node prev = x.prev;
        Object obj = x.item;
        Node next = x.next;
        if(prev == null){
            //删除的是第一个
            first = next;
        }else {
            prev.next = next;
            x.prev = null;
        }

        if(next == null){
            //删除的是最后一个
            last = prev;
        }else {
            next.prev = prev;
            x.next = null;
        }
        x.item = null;
        size--;
        return obj;
    }
    public int getSize() {
        return size;
    }

    private class Node {
         Node prev;
         Object item;
         Node next;
        public Node(Node prev, Object item, Node next) {
            this.prev = prev;
            this.item = item;
            this.next = next;
        }
    }

    public static void main(String[] args) {
        TxLinkedList linkList = new TxLinkedList();
        linkList.add("111");
        linkList.add("66");
        linkList.add("41");
        linkList.add("133");
        System.out.println(linkList.getSize());
        System.out.println(linkList.get(2));
        linkList.remove("11");
        System.out.println(linkList.size);
        System.out.println(linkList.get(2));
//        LinkedList l = new LinkedList();
//        l.add("3");
//        l.remove("");
    }
}
