import java.util.NoSuchElementException;

public class CharacterStream {

    String characters;
    int index;

    public CharacterStream(String characters){
        this.characters = characters;
        index = 0;
    }

    public boolean hasNext(){
        return index < characters.length();
    }

    public char nextChar(){
        if(!hasNext())
            throw new NoSuchElementException("No more characters in stream");
        return characters.charAt(index++);
    }

    public char peekChar() {
        if(!hasNext())
            throw new NoSuchElementException("No more characters in stream");
        return characters.charAt(index);
    }
}
