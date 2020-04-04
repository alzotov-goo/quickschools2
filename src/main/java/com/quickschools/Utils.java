package com.quickschools;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

public class Utils{
    public static String up(String str)
    {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String removeLastChar(String str) {
        return str.substring(0, str.length()-2);
    }

    public static boolean empty(String[] strs) {
        return (strs==null || strs.length == 0);
    }

    //c = a intersect b; a = a-b;
    //return common elemnets and remove them from first set
    public static <T> Set<T> extract(Set<T> a, Set<T> b)
    {
        HashSet<T> rz = new HashSet<>();
        rz.addAll(a);
        rz.retainAll(b);
        a.removeAll(b);
        return rz;
    }

    public static <T> Set<T> copy(Set<T> a)
    {
        HashSet<T> rz = new HashSet<>();
        rz.addAll(a);
        return rz;
    }

    public static <T> Stack<T> copyToStack(Set<T> a)
    {
        Stack<T> rz = new Stack<>();
        rz.addAll(a);
        return rz;
    }
}
