package com.knowswift.myspringboot.utils;

import java.util.*;

/**
 * @author Li Yao Bing
 * @Company https://www.knowswift.com/
 * @Date 2020/6/30
 **/


public class GroupListUtils {

    public interface GroupBy<T> {
        T groupBy(Object obj);
    }

    public static <T extends Comparable<T>, D> Map<T, Set<D>> group(Collection<D> coll, GroupBy<T> val) {
        if (coll == null || coll.isEmpty()) {
            return null;
        }
        if (val == null) {
            return null;
        }
        Iterator<D> iterator = coll.iterator();
        HashMap<T, Set<D>> map = new HashMap<>();
        while (iterator.hasNext()) {
            D d = iterator.next();
            T t = val.groupBy(d);

            if (map.containsKey(t)) {
                map.get(t).add(d);
            } else {
                Set<D> ds = new HashSet<>();
                ds.add(d);
                map.put(t, ds);
            }
        }
        return map;
    }
}
