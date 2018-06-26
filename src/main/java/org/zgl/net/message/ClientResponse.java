package org.zgl.net.message;

public class ClientResponse {
    private int id;
    private byte[] data;

    public ClientResponse() {
    }

    public ClientResponse(int id, byte[] data) {
        this.id = id;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
