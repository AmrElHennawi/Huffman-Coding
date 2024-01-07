public class Node implements Comparable<Node> {
    protected int freq;
    private Node left;
    private Node right;

    Node(Node left, Node right){
        this.left = left;
        this.right = right;
        if(left == null || right == null) //Leaf
            return;
        this.freq = left.freq + right.freq;
    }

    public Node getLeft(){
        return left;
    }

    public Node getRight(){
        return right;
    }

    @Override
    public int compareTo(Node node){
        return Integer.compare(freq,node.freq);
    }
}
