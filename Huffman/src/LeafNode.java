public class LeafNode extends Node{
    private final char character;

    LeafNode(char character,int freq){
        super(null,null);
        this.freq = freq;
        this.character = character;
    }

    public char getCharacter() {
        return character;
    }

}
