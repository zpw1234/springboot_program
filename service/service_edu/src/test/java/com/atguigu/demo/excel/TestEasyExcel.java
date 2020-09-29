package com.atguigu.demo.excel;

import com.alibaba.excel.EasyExcel;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;

public class TestEasyExcel {
    public static void main(String[] args) {
        //实现excel写操作
        //1 设置写入文件夹的地址和excel名称
        String filename = "F:\\write.xlsx";
        //2.调用easyexcel
//        EasyExcel.write(filename,DemoData.class).sheet("学生列表").doWrite(getData());
        EasyExcel.read(filename,DemoData.class,new ExcelListener()).sheet().doRead();
    }

    //创建方法返回list集合
    private static List<DemoData> getData(){
        List<DemoData> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            DemoData demoData = new DemoData();
            demoData.setSno(i);
            demoData.setName("lucy"+i);
            list.add(demoData);
        }
        return list;
    }
}
