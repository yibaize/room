package org.zgl.list;

/**
 * @作者： big
 * @创建时间： 2018/5/28
 * @文件描述：
 */
public class MyLinkedList {
    /**记录第一个节点*/
    private Node first;
    /**记录最后节点*/
    private Node last;
    /**当前容量*/
    private int size;


    public void add(Object o) {
        final Node l = last;
        //这里new的时候便把新节点的上一个节点prev设置成之前链表的最后一个节点
        final Node newNode = new Node(l,o,null);
        last = newNode;
        if (l == null) {
            //l为空说明当前加入的是第一个
            //所以first和last都为新创建的节点并且该节点的prev和next都为空
            first = newNode;
        } else {
            //否则将新添加的节点设置为最后一个节点
            //那么它的prev就是上次的最后一个节点也就是l而它的next就是空的
            l.next = newNode;
        }
        size++;
    }
    /**
     * 内部类Node节点
     */
    private class Node{
        Node prev;
        Object item;
        Node next;
        public Node(Node prev, Object item, Node next) {
            this.prev = prev;
            this.item = item;
            this.next = next;
        }
    }
}
