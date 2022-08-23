package com.rjgf.threecost.crawdata.rpc.client;

import com.rjgf.threecost.crawdata.rpc.CalculateService;
import lombok.extern.slf4j.Slf4j;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;


@Slf4j
public class ClientService {


    public static void main(String[] args) {
        log.info("客户端启动");
        CalculateService calculateService = (CalculateService) Proxy.newProxyInstance(CalculateService.class.getClassLoader(),
                new Class<?>[]{CalculateService.class}, new MyClientServiceHandler());
        String add = calculateService.add(122, 221);
        log.info("客户端的输出add结果是 ----- " + add);
    }
}



class MyClientServiceHandler implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(8000));
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        oos.writeUTF("com.rjgf.threecost.crawdata.rpc.server.CalculateServiceImpl");
        oos.writeUTF(method.getName());

        oos.writeObject(method.getParameterTypes());
        oos.writeObject(args);
        return ois.readObject();
    }
}
