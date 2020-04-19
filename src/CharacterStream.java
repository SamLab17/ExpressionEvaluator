import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A class which allows for reading from a String character by character.
 * This class can be used very similarly to a Scanner, InputStream, or Iterator.
 *
 * @author Samuel Laberge, 2020
 */
public class CharacterStream extends InputStream implements Iterator<Character> {

    private String characters;
    private int index;

    /**
     * Creates a character input stream given a String of characters.
     *
     * @param characters String to iterate over
     */
    public CharacterStream(String characters) {
        this.characters = characters;
        index = 0;
    }

    /**
     * Checks to see if there are any characters left in this stream
     *
     * @return true iff there are any characters left
     */
    @Override
    public boolean hasNext() {
        return index < characters.length();
    }

    /**
     * Returns the next character in the input stream, advances the input stream
     *
     * @return the next character
     * @throws NoSuchElementException if there are no characters remaining
     */
    public char nextChar() {
        if (!hasNext())
            throw new NoSuchElementException("No more characters in stream");
        return characters.charAt(index++);
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws NoSuchElementException if the iteration has no more elements
     */
    @Override
    public Character next() {
        return nextChar();
    }

    /**
     * Returns the next character in the input stream without advancing.
     *
     * @return the next character
     * @throws NoSuchElementException if there are no characters remaining
     */
    public char peekChar() {
        if (!hasNext())
            throw new NoSuchElementException("No more characters in stream");
        return characters.charAt(index);
    }

    /**
     * Reads the next byte of data from the input stream. The value byte is
     * returned as an <code>int</code> in the range <code>0</code> to
     * <code>255</code>. If no byte is available because the end of the stream
     * has been reached, the value <code>-1</code> is returned. This method
     * blocks until input data is available, the end of the stream is detected,
     * or an exception is thrown.
     * <p>
     * This makes the assumption that the provided string contains only ASCII
     * characters. Otherwise, the values returned by this method may not be strictly
     * between 0 and 255. This should be a fine assumption for an expression evaluator.
     *
     * @return the next byte of data, or <code>-1</code> if the end of the
     * stream is reached.
     */
    @Override
    public int read() {
        if (!hasNext())
            return -1;
        return next();
    }

}
