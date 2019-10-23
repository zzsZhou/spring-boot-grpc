package com.xcbeyond.springboot.grpc.client.controller;

import com.xcbeyond.springboot.grpc.client.service.GrpcClientService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Auther: xcbeyond
 * @Date: 2019/3/7 11:44
 */
@RestController
@RequestMapping("/v1/users")
public class GrpcClientController {
    @Resource
    private GrpcClientService grpcClientService;

    @PostMapping
    public String insertUserInfo(@RequestBody List<Map> users) {
        return grpcClientService.insertUserInfo(users);
    }


    @GetMapping("/{user_name}")
    public String queryUserInfo(@PathVariable("user_name")String userName){
        return grpcClientService.queryUser(userName);
    }
}
