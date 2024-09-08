package bootstrap;

import pojo.User;
import proxy.ProxyFactory;
import service.UserService;

public class CustomerMain {
    public static void main(String[] args) {
        UserService userService = ProxyFactory.getProxy(UserService.class);
        String userName = userService.getUserName(new User(1L,"name"));
        System.out.println(userName);
    }
}
