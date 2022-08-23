package com.rjgf.threecost.crawdata.rpc.server;

import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

@Slf4j
public class PublishServer {

    public static void main(String[] args) throws Exception {
        log.info("服务端启动");
        ServerSocket ss = new ServerSocket(8000);
        Socket socket = null;

        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;

        while(true){
             log.info("等待连接");
             socket = ss.accept();
             log.info("已连接");
             ois = new ObjectInputStream(socket.getInputStream());
             oos = new ObjectOutputStream(socket.getOutputStream());

            String serviceName = ois.readUTF();
            String methodName = ois.readUTF();
            Class<?>[] parameterTypes = (Class<?>[]) ois.readObject();
            Object[] parameters = (Object[]) ois.readObject();

            Class<?> aClass = Class.forName(serviceName);
            Method method = aClass.getMethod(methodName, parameterTypes);
            oos.writeObject(method.invoke(aClass.newInstance(),parameters));

            oos.close();
            ois.close();
            socket.close();
        }
    }
}
