package org.bql.rooms.thousands_of.model;

public class MoneyPool implements Comparable<MoneyPool> {
    private int id;
    private long money;

    public MoneyPool(int id, long money) {
        this.id = id;
        this.money = money;
    }

    public MoneyPool() {
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    /**
     * 大到小排序
     * @param o
     * @return
     */
    @Override
    public int compareTo(MoneyPool o) {
        return (int) (o.getMoney() - this.money);
    }
}
