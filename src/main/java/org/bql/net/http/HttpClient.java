package org.bql.net.http;

import okhttp3.*;
import org.bql.error.AppErrorCode;
import org.bql.error.GenaryAppError;
import org.bql.net.message.Msg;
import org.bql.player.LoginDto;
import org.bql.utils.ProtostuffUtils;

import java.io.*;

/**
 * @作者： big
 * @创建时间： 2018/5/18
 * @文件描述：
 */
public class HttpClient {
    private static HttpClient instance;

    public static HttpClient getInstance() {
        if(instance == null)
            instance = new HttpClient();
        return instance;
    }

    public static void main(String[] args) throws IOException {
        HttpClient client = new HttpClient();
        LoginDto s1 = client.syncPost((short)2,"2,2",LoginDto.class);
        System.out.println(s1);

    }

    /**
     * 同步回调
     * @param cmd
     * @param args
     * @param tClazz
     * @param <T>
     * @return
     */
    public <T> T syncPost(short cmd, String args, Class<T> tClazz){
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); DataOutputStream dos = new DataOutputStream(baos)){
            OkHttpClient client = new OkHttpClient();
            dos.writeInt(-777888);
            dos.writeShort(cmd);
            Msg msm = new Msg(args);
            byte[] buf = ProtostuffUtils.serializer(msm);
            dos.writeShort(buf.length);
            dos.write(buf);
            RequestBody body = RequestBody.create(MediaType.parse("application/octet-stream"),baos.toByteArray());
            Request request = new Request.Builder()
                    .url("http://127.0.0.1:1010")
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return resultData(response.body().byteStream(),tClazz);
            } else {
                throw new IOException("RPC数据同步异常 " + response);
            }
        }catch (Exception e){

        }
        return null;
    }

    /**
     * 异步回调
     * @param cmd
     * @param args
     */
    public void asyncPost(short cmd,String args){
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); DataOutputStream dos = new DataOutputStream(baos)){
            OkHttpClient client = new OkHttpClient();
            dos.writeInt(-777888);
            dos.writeShort(cmd);
            Msg msm = new Msg(args);
            byte[] buf = ProtostuffUtils.serializer(msm);
            dos.writeShort(buf.length);
            dos.write(buf);
            RequestBody body = RequestBody.create(MediaType.parse("application/octet-stream"),baos.toByteArray());
            Request request = new Request.Builder()
                    .url("http://127.0.0.1:1010")
                    .post(body)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                }
            });
        }catch (Exception e){

        }
    }
    private <T> T resultData(InputStream inputStream,Class<T> tClazz){
        try (DataInputStream dis = new DataInputStream(inputStream)){
            int head = dis.readInt();
            if(head != -777888)
                new GenaryAppError(AppErrorCode.DATA_ERR);
            short cmdId = dis.readShort();
            short length = dis.readShort();
            byte[] buf = dis.readAllBytes();
            if(length != buf.length)
                new GenaryAppError(AppErrorCode.DATA_ERR);
            T t = ProtostuffUtils.deserializer(buf,tClazz);
            return t;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
