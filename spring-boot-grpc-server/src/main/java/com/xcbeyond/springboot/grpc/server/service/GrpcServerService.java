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

    @Override
    public void queryUsersByAge(UserAge request, StreamObserver<Reply> responseObserver) {
        System.out.println("queryUsersByAge...age:"+request.getAge());

        BatchUser.User user1 = BatchUser.User.newBuilder().setAge(request.getAge()).setName("zq01").setIdNumber(111).build();
        Any any1 = Any.pack(user1);
        Reply reply1 = Reply.newBuilder().setCode(0L).setDes("SUCCESS").setResult(any1).build();

        BatchUser.User user2 = BatchUser.User.newBuilder().setAge(request.getAge()).setName("zq02").setIdNumber(222).build();
        Any any2 = Any.pack(user2);
        Reply reply2 = Reply.newBuilder().setCode(0L).setDes("SUCCESS").setResult(any2).build();


        responseObserver.onNext(reply1);
        responseObserver.onNext(reply2);
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<UserName> queryUsersByNames(StreamObserver<Reply> responseObserver) {

        return new StreamObserver<UserName>() {
            @Override
            public void onNext(UserName userName) {
                System.out.println(userName.getName());
                Reply reply = constructReply(userName);
                responseObserver.onNext(reply);
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                System.out.println("#####complete######");
                responseObserver.onCompleted();
            }
        };
    }

    private Reply constructReply(UserName userName){
        BatchUser.User user1 = BatchUser.User.newBuilder().setAge(11).setName(userName.getName()).setIdNumber(111).build();
        Any any1 = Any.pack(user1);
        Reply reply1 = Reply.newBuilder().setCode(0L).setDes("SUCCESS").setResult(any1).build();
        return reply1;
    }

}