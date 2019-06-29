package wgn.myspringboot;


import com.alibaba.druid.util.StringUtils;

import java.io.*;
import java.util.Objects;

/**
 * Company        :   mamahao.com
 * author         :   wangguannan
 * Date           :   2019/6/28
 * Time           :   5:04 PM
 * Description    :
 */
public class TestStream {


    public static void main(String[] args) {

        try {

            File src = new File("/Users/wangguannan/Downloads/src.txt");
            FileReader fr = new FileReader(src);
            BufferedReader br = new BufferedReader(fr);
            String s;
            StringBuilder sb = new StringBuilder(10248);
            Node head = new Node();
            head.setLevel(0);
            head.setLeftHigh(0);
            head.setRightHigh(0);
            while ((s = br.readLine()) != null) {

                //System.out.println("-----------"+s);
                head = deal(head, s);
            }

            Node node = head.getMinNode();
            for(String t:node.getValues()) {
                System.out.println(t);
                sb.append(t + " ");
            }
            while (Objects.nonNull(node.getAfter())) {
                node = node.getAfter();
                for(String t:node.getValues()) {
                    System.out.println(t);
                    sb.append(t + " ");
                }
            }

            File des = new File("/Users/wangguannan/Downloads/des.txt");
            FileWriter fw = new FileWriter(des);
            //System.out.println(sb);
            fw.write(sb.toString());
            br.close();
            fr.close();
            fw.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

    }

    private static Node deal(Node head, String s) {

        String[] strs=s.split("\\s+");

        for(String ss:strs) {
            if (!StringUtils.isEmpty(ss)) {
                //System.out.println("======="+ss);
                head= add(head, ss);
            }

        }

        return head;
    }

    private static Node add(Node head, String word) {
        Node curNode = null;
        if (Objects.nonNull(head.getAfter())) {
            curNode = head.getAfter();
            head = recursion(head, curNode, word);
        } else {
            Node node = new Node();
            node.setName(word);
            node.getValues().add(word);
            node.setLevel(1);

            node.setMin(true);
            head.setLeftHigh(1);
            head.setRightHigh(1);
            head.setAfter(node);
            head.setMinNode(node);

        }
        return head;
    }


    private static Node recursion(Node head, Node curNode, String name) {

        if (compare(curNode.getName(), name) > 0) {
            if (Objects.nonNull(curNode.getLeft())) {
                recursion(head, curNode.getLeft(), name);
            } else {
                Node nl = new Node();
                nl.setName(name);
                nl.getValues().add(name);
                nl.setLevel(curNode.getLevel() + 1);
                nl.setAfter(curNode);
                nl.setPre(curNode.getPre());

                curNode.setLeft(nl);
                curNode.setPre(nl);
                if (curNode.isMin()) {
                    curNode.setMin(false);
                    nl.setMin(true);
                    head.setMinNode(nl);
                }

                turn(nl, head);
            }

        } else if (compare(curNode.getName(), name) < 0) {
            if (Objects.nonNull(curNode.getRight())) {
                recursion(head, curNode.getRight(), name);
            } else {
                Node nr = new Node();
                nr.setName(name);
                nr.getValues().add(name);
                nr.setLevel(curNode.getLevel() + 1);
                nr.setPre(curNode);
                nr.setAfter(curNode.getAfter());

                curNode.setRight(nr);
                curNode.setAfter(nr);
                turn(nr, head);


            }

        }else if(compare(curNode.getName(),name)==0){
            for(String t:curNode.getValues()){
                if(Objects.equals(t,name)){
                    return head;
                }
            }
            curNode.getValues().add(name);
        }

        return head;
    }

    private static Node turn(Node curNode, Node head) {

        if (compare(head.getAfter().getName(), curNode.getName()) > 0 && curNode.getLevel() - 1 > head.getRightHigh()) {
            head = turnRight(head);
        } else if (compare(head.getAfter().getName(), curNode.getName()) < 0 && curNode.getLevel() - 1 > head.getLeftHigh()) {
            head = turnLeft(head);
        }

        return head;
    }

    private static Node turnRight(Node head) {
        Node curNode = head.getAfter();
        Node left = curNode.getLeft();
        curNode.setLeft(left.getRight());
        left.setRight(curNode);
        head.setAfter(left);
        head.setRightHigh(head.getRightHigh() + 1);
        return head;
    }


    private static Node turnLeft(Node head) {
        Node curNode = head.getAfter();
        Node right = curNode.getRight();
        curNode.setRight(right.getLeft());
        right.setLeft(curNode);
        head.setAfter(right);
        head.setLeftHigh(head.getLeftHigh() + 1);
        return head;
    }

    private static int compare(String a, String b) {
        return a.substring(a.length() - 3>0?a.length() - 3:0, a.length()).compareTo(b.substring(b.length() - 3>0?b.length()-3:0, b.length()));
    }


}


