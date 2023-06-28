package com.lc;


import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;

/**
 * @Author Lc
 * @Date 2023/6/27
 * @Description
 */

public class testEasyExcel {

    public static void main(String[] args) {
        String fileName = "C:\\Users\\Lc\\Desktop\\尚医通\\Testexcel.xlsx";
        //写入excel
//        doExcel();
        //读excel
      simpleRead(fileName);
    }


    public static void simpleRead(String fileName) {
        EasyExcel.read(fileName, User.class, new UserDataListener()).sheet().doRead();
    }

    private static void doExcel(String fileName) {
        ArrayList<User> userArrayList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setName("老王"+i);
            user.setAge(i+18);
            userArrayList.add(user);
        }
        // 注意 simpleWrite在数据量不大的情况下可以使用（5000以内，具体也要看实际情况），数据量大参照 重复多次写入

        // 写法1 JDK8+
        // since: 3.0.0-beta1

        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        EasyExcel.write(fileName, User.class)
                .sheet("用户信息")
                .doWrite(userArrayList);
    }
}
