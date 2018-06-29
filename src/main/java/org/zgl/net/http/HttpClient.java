package org.zgl.net.http;

import okhttp3.*;
import org.zgl.error.AppErrorCode;
import org.zgl.error.AppErrorDataTable;
import org.zgl.error.GenaryAppError;
import org.zgl.net.handler.NetCnf;
import org.zgl.net.message.Msg;
import org.zgl.player.LoginDto;
import org.zgl.remote.IoMessageJavaTpeImpl;
import org.zgl.utils.ProtostuffUtils;
import org.zgl.utils.logger.LoggerUtils;

import java.io.*;
import java.util.concurrent.TimeUnit;

/**
 * @作者： big
 * @创建时间： 2018/5/18
 * @文件描述：
 */
public class HttpClient {
    private static HttpClient instance;
    private final int port;
    private final String ip;
    public static HttpClient getInstance() {
        if (instance == null)
            instance = new HttpClient();
        return instance;
    }

    private HttpClient() {
        ip = NetCnf.getInstance().getPathCnf().getClientIp();
        port = NetCnf.getInstance().getPathCnf().getClientPort();
    }

    public static void main(String[] args) throws IOException {
        HttpClient client = new HttpClient();
        LoginDto s1 = client.syncPost((short) 2, "2,2", LoginDto.class);
        System.out.println(s1);

    }

    /**
     * 同步回调
     *
     * @param cmd
     * @param args
     * @param tClazz
     * @param <T>
     * @return
     */
    public <T> T syncPost(short cmd, String args, Class<T> tClazz) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); DataOutputStream dos = new DataOutputStream(baos)) {
            OkHttpClient client = new OkHttpClient();
            dos.writeInt(-777888);
            dos.writeShort(cmd);
            Msg msm = new Msg(args);
            byte[] buf = ProtostuffUtils.serializer(msm);
            dos.writeShort(buf.length);
            dos.write(buf);
            RequestBody body = RequestBody.create(MediaType.parse("application/octet-stream"), baos.toByteArray());
            Request request = new Request.Builder()
                    .url("http://"+ip+":"+port+"/game/handle")
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                if (tClazz != null)
                    return resultData(response.body().byteStream(), tClazz);
            } else {
                throw new IOException("RPC数据同步异常 " + response);
            }
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 异步回调
     *
     * @param cmd
     * @param args
     */
    public void asyncPost(short cmd, String args) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); DataOutputStream dos = new DataOutputStream(baos)) {
            OkHttpClient client = new OkHttpClient()
                    .newBuilder()
                    .connectTimeout(3,TimeUnit.SECONDS)
                    .readTimeout(3,TimeUnit.SECONDS)
                    .writeTimeout(3,TimeUnit.SECONDS)
                    .build();
            dos.writeInt(-777888);
            dos.writeShort(cmd);
            Msg msm = new Msg(args);
            byte[] buf = ProtostuffUtils.serializer(msm);
            dos.writeShort(buf.length);
            dos.write(buf);
            RequestBody body = RequestBody.create(MediaType.parse("application/octet-stream"), baos.toByteArray());
            Request request = new Request.Builder()
                    .url("http://"+ip+":"+port+"/game/handle")
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
        } catch (Exception e) {

        }
    }

    private <T> T resultData(InputStream inputStream, Class<T> tClazz) {
        try (DataInputStream dis = new DataInputStream(inputStream)) {
            int head = dis.readInt();
            if (head != -777888)
                new GenaryAppError(AppErrorCode.DATA_ERR);
            short cmdId = dis.readShort();
            short length = dis.readShort();
            byte[] buf = dis.readAllBytes();
            if (length != buf.length)
                new GenaryAppError(AppErrorCode.DATA_ERR);
            T t = ProtostuffUtils.deserializer(buf, tClazz);
            return t;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public <T> T syncPost(IoMessageJavaTpeImpl ioMessage,Class<T> tClazz) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); DataOutputStream dos = new DataOutputStream(baos)) {
            OkHttpClient client = new OkHttpClient()
                    .newBuilder()
                    .connectTimeout(3,TimeUnit.SECONDS)
                    .readTimeout(3,TimeUnit.SECONDS)
                    .writeTimeout(3,TimeUnit.SECONDS)
                    .build();
            dos.writeInt(-777888);
            byte[] buf = ProtostuffUtils.serializer(ioMessage);
            dos.write(buf);
            RequestBody body = RequestBody.create(MediaType.parse("application/octet-stream"), baos.toByteArray());
            Request request = new Request.Builder()
                    .url("http://"+ip+":"+port+"/game/rpc")
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                if (tClazz != null) {
                    return result(response.body().byteStream(), tClazz);
                }
            } else {
                throw new IOException("RPC数据同步异常 " + response);
            }
        } catch (Exception e) {
            LoggerUtils.getLogicLog().error(e);
        }
        return null;
    }
    public void asyncPost(IoMessageJavaTpeImpl ioMessage) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); DataOutputStream dos = new DataOutputStream(baos)) {
            OkHttpClient client = new OkHttpClient()
                    .newBuilder()
                    .connectTimeout(3,TimeUnit.SECONDS)
                    .readTimeout(3,TimeUnit.SECONDS)
                    .writeTimeout(3,TimeUnit.SECONDS)
                    .build();
            dos.writeInt(-777888);
            byte[] buf = ProtostuffUtils.serializer(ioMessage);
            dos.write(buf);
            RequestBody body = RequestBody.create(MediaType.parse("application/octet-stream"), baos.toByteArray());
            Request request = new Request.Builder()
                    .url("http://"+ip+":"+port+"/game/rpc")
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
        } catch (Exception e) {

        }
    }
    private <T> T result(InputStream inputStream,Class<T> tClazz){
        try (DataInputStream dis = new DataInputStream(inputStream)) {
            int head = dis.readInt();
            if(head != -777888)
                new GenaryAppError(AppErrorCode.DATA_ERR);
            short resultCode = dis.readShort();
            if(resultCode == 404){
                short errorCode = dis.readShort();
                throw new RuntimeException(AppErrorDataTable.get(errorCode).getValue());
            }
            if(tClazz.isPrimitive()){
                return (T) checkType(tClazz,dis);
            }
            //TODO...String类型的
            byte[] buf = dis.readAllBytes();
            T t = ProtostuffUtils.deserializer(buf, tClazz);
            return t;
        }catch (Exception e){
            LoggerUtils.getLogicLog().error(e.getCause());
        }
        return null;
    }
    private Object checkType(Class<?> clazz,DataInputStream is) throws IOException {
        if(clazz.equals(short.class))
            return is.readShort();
        else if(clazz.equals(byte.class))
            return is.readByte();
        else if(clazz.equals(char.class))
            return is.readChar();
        else if(clazz.equals(boolean.class))
            return is.readBoolean();
        else if(clazz.equals(int.class))
            return is.readInt();
        else if(clazz.equals(long.class))
            return is.readLong();
        else if(clazz.equals(float.class))
            return is.readFloat();
        else if(clazz.equals(double.class))
            return is.readDouble();
        return null;
    }
}
