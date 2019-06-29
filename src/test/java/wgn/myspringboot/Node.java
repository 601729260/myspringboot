package wgn.myspringboot;

import java.util.ArrayList;
import java.util.List;

/**
 * Company        :   mamahao.com
 * author         :   wangguannan
 * Date           :   2019/6/28
 * Time           :   5:36 PM
 * Description    :
 */
public class Node{
    private Node left;
    private Node right;
    private Node pre;
    private Node after;
    private List<String> values=new ArrayList<>();
    private String name;
    private Integer level;
    private Integer leftHigh;
    private Integer rightHigh;
    private Node minNode;
    private boolean min;

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public Node getPre() {
        return pre;
    }

    public void setPre(Node pre) {
        this.pre = pre;
    }

    public Node getAfter() {
        return after;
    }

    public void setAfter(Node after) {
        this.after = after;
    }



    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getLeftHigh() {
        return leftHigh;
    }

    public void setLeftHigh(Integer leftHigh) {
        this.leftHigh = leftHigh;
    }

    public Integer getRightHigh() {
        return rightHigh;
    }

    public void setRightHigh(Integer rightHigh) {
        this.rightHigh = rightHigh;
    }

    public Node getMinNode() {
        return minNode;
    }

    public void setMinNode(Node minNode) {
        this.minNode = minNode;
    }

    public boolean isMin() {
        return min;
    }

    public void setMin(boolean min) {
        this.min = min;
    }
}

