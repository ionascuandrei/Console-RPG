package fileio.interfaces;

import java.io.IOException;

/**
 * Abstract way to write contents to a file.
 *
 * @author Mihai Burduselu
 */
public interface IWriter {
    /**
     * Writes a char in the buffered memory.
     */
    void writeCharacter(char variableToWrite) throws IOException;

    /**
     * Writes a boolean in the buffered memory.
     */
    void writeBool(boolean variableToWrite) throws IOException;

    /**
     * Writes an integer in the buffered memory.
     */
    void writeInt(int variableToWrite) throws IOException;

    /**
     * Writes a long integer in the buffered memory.
     */
    void writeLong(long variableToWrite) throws IOException;

    /**
     * Writes a float in the buffered memory.
     */
    void writeFloat(float variableToWrite) throws IOException;

    /**
     * Write a double in the buffered memory.
     */
    void writeDouble(double variableToWrite) throws IOException;

    /**
     * Writes a word in the buffered memory.
     */
    void writeWord(String variableToWrite) throws IOException;

    /**
     * Writes a new line in the buffered memory.
     **/
    void writeNewLine() throws IOException;

    /**
     * Close the writer.
     *
     * @throws IOException if an I/O exception occurred
     */
    void close() throws IOException;
}
