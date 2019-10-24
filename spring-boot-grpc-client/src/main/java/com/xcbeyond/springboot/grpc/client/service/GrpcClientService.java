package com.xcbeyond.springboot.grpc.client.service;

import com.google.protobuf.InvalidProtocolBufferException;
import com.xcbeyond.springboot.grpc.lib.*;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Auther: xcbeyond
 * @Date: 2019/3/7 09:10
 */
@Service
public class GrpcClientService {

    @GrpcClient("spring-boot-grpc-server")
    private UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub;
    @GrpcClient("spring-boot-grpc-server")
    private UserServiceGrpc.UserServiceStub userServiceStub;


    public String insertUserInfo(List<Map> users){
        List<BatchUser.User> userInfos = new ArrayList<>();
        users.forEach(item->{
            BatchUser.User user = BatchUser.User.newBuilder()
                    .setAge((Integer) item.get("age"))
                    .setName((String) item.get("name"))
                    .setIdNumber(((Integer) item.get("id_number")).longValue())
                    .build();
            userInfos.add(user);
        });
        BatchUser userInfo = BatchUser.newBuilder().addAllUsers(userInfos).build();

        Reply reply = userServiceBlockingStub.batchInsertUsers(userInfo);
        try {
            String name = reply.getResult().unpack(UserName.class).getName();
            System.out.println("reply name:"+name);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        System.out.println(reply.toString());
        return reply.getDes();
    }

    public String queryUser(String name){
        UserName userName = UserName.newBuilder().setName(name).build();
        Reply reply = userServiceBlockingStub.queryUserByName(userName);

        String resName = null;
        try {
            resName = reply.getResult().unpack(UserName.class).getName();
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        System.out.println("reply name:"+resName);
        System.out.println(reply.toString());

        return reply.getDes();
    }


    public String queryUsersByAge(Integer age){

        //final ManagedChannel channel = ManagedChannelBuilder.forAddress("127.0.0.1", 8001).usePlaintext(true).build();
/*        int port = 8001;
        String urlPath = "192.168.99.100";
        Channel channel;
        //设置grpc链接
        channel = NettyChannelBuilder.forAddress(urlPath, port).negotiationType(NegotiationType.PLAINTEXT).build();
        UserServiceGrpc.UserServiceBlockingStub userServerBlockingStub2 = UserServiceGrpc.newBlockingStub(channel);*/


        UserAge userAge = UserAge.newBuilder().setAge(age).build();

        Iterator<Reply> iterable = userServiceBlockingStub.queryUsersByAge(userAge);
        try {
            while (iterable.hasNext()) {
                Reply reply = iterable.next();
                BatchUser.User user = reply.getResult().unpack(BatchUser.User.class);
                System.out.println(user.getAge());
                System.out.println(user.getName());
                System.out.println(user.getIdNumber());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "SUCCESS";
    }


    public String queryUsersByNames(String[] nameArray){

        StreamObserver<Reply> replyStreamObserver = new StreamObserver<Reply>() {
            @Override
            public void onNext(Reply reply) {
                try {
                    System.out.println("##########");
                    System.out.println("name:"+reply.getResult().unpack(BatchUser.User.class).getName());
                    System.out.println("age:"+reply.getResult().unpack(BatchUser.User.class).getAge());
                    System.out.println("id_number:"+reply.getResult().unpack(BatchUser.User.class).getIdNumber());
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("onError");
            }

            @Override
            public void onCompleted() {
                System.out.println("onCompleted");
            }
        };

        StreamObserver<UserName> userNameStreamObserver = userServiceStub.queryUsersByNames(replyStreamObserver);
        for (String name:
                nameArray){
            userNameStreamObserver.onNext(UserName.newBuilder().setName(name).build());
        }
        userNameStreamObserver.onCompleted();

        return "SUCCESS";
    }

}
