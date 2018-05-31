package org.bql.net.message;

public class ServerRequest {
    private short id;
    /**
     * 数据
     */
    private Msg data;

    public ServerRequest() {
    }

    public ServerRequest(short id, Msg data) {
        this.id = id;
        this.data = data;
    }

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    public Msg getData() {
        return data;
    }

    public void setData(Msg data) {
        this.data = data;
    }
}
