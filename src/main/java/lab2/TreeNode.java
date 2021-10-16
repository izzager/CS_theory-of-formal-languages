package lab2;

import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Data
public class TreeNode<T> {

    private T data;
    private TreeNode<T> parent;
    private List<TreeNode<T>> children;

    public TreeNode(T data) {
        this.data = data;
        this.children = new LinkedList<>();
    }

    public TreeNode<T> addChild(T child) {
        TreeNode<T> childNode = new TreeNode<T>(child);
        childNode.parent = this;
        this.children.add(childNode);
        return childNode;
    }

    public TreeNode<T> addChild(TreeNode<T> childNode) {
        if (childNode != null) {
            childNode.parent = this;
        }
        this.children.add(childNode);
        return childNode;
    }

    public void traverseTree() {
        List<TreeNode<T>> q = new ArrayList<>();
        q.add(this);
        while(!q.isEmpty()) {
            TreeNode<T> elem = q.get(0);
            System.out.println(elem.getData());
            q.addAll(elem.getChildren());
            q.remove(0);
        }
    }
    
}
