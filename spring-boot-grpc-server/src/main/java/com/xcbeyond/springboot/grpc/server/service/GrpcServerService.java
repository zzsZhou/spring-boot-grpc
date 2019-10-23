package com.xcbeyond.springboot.grpc.server.service;

import com.google.protobuf.Any;
import com.xcbeyond.springboot.grpc.lib.*;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

/**
 * @Auther: xcbeyond
 * @Date: 2019/3/6 18:15
 */
@GrpcService
public class GrpcServerService extends UserServiceGrpc.UserServiceImplBase {
    @Override
    public void queryUserByName(UserName userName, StreamObserver<Reply> responseObserver) {
        UserName userName1 = UserName.getDefaultInstance();
        //UserName.parseFrom()
        System.out.println("queryUserByName...userName:"+userName.getName());

        UserName userName2 = UserName.newBuilder().setName("queryUserByName").build();
        Any any = Any.pack(userName2);
        Reply reply = Reply.newBuilder().setCode(0L).setDes("SUCCESS").setResult(any).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void batchInsertUsers(BatchUser batchUser, StreamObserver<Reply> responseObserver) {
        System.out.println("batchInsertUsers...");
        int i = 1;
        for (BatchUser.User user:
                batchUser.getUsersList()){
            System.out.println("user"+i);
            System.out.println(user.getAge());
            System.out.println(user.getName());
            System.out.println(user.getIdNumber());
            i++;
        }

        UserName userName = UserName.newBuilder().setName("batchInsertUsers").build();
        Any any = Any.pack(userName);
        Reply reply = Reply.newBuilder().setCode(0L).setDes("SUCCESS").setResult(any).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

}