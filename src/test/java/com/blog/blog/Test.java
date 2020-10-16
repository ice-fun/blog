package com.blog.blog;

import java.util.ArrayList;
import java.util.List;

public class Test {

    @org.junit.jupiter.api.Test
    public void test() {
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("12");
        list.remove("2");
        for (String s : list) {
            System.out.println(s);
        }
    }
}
