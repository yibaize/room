package org.zgl.net.message;

import java.util.concurrent.atomic.AtomicInteger;

public class ClientRequest {
    private final int id;
    private final AtomicInteger aid = new AtomicInteger(1);
    private byte[] data;

    public ClientRequest(int id, byte[] data) {
        this.id = id;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getDataLength(){
        if(data != null){
            return data.length;
        }
        return 0;
    }
}
