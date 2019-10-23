package com.xcbeyond.springboot.grpc.client.service;

import com.google.protobuf.InvalidProtocolBufferException;
import com.xcbeyond.springboot.grpc.lib.BatchUser;
import com.xcbeyond.springboot.grpc.lib.Reply;
import com.xcbeyond.springboot.grpc.lib.UserName;
import com.xcbeyond.springboot.grpc.lib.UserServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

}
