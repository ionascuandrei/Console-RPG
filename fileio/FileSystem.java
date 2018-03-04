package fileio;

import fileio.implementations.FileReader;
import fileio.implementations.FileWriter;
import fileio.interfaces.IReader;
import fileio.interfaces.IWriter;

import java.io.FileNotFoundException;
import java.io.IOException;

public final class FileSystem implements IReader, IWriter {
    private FileReader fileReader;
    private FileWriter fileWriter;

    public FileSystem(
            final String inputFile,
            final String outputFile) throws IOException {
        initReader(inputFile);
        initWriter(outputFile);
    }

    private void initReader(
            final String inputFile) throws FileNotFoundException {
        fileReader = new FileReader(inputFile);
    }

    private void initWriter(final String outputFile) throws IOException {
        fileWriter = new FileWriter(outputFile);
    }

    public boolean nextBool() throws IOException {
        return fileReader.nextBool();
    }

    public int nextInt() throws IOException {
        return fileReader.nextInt();
    }

    public long nextLong() throws IOException {
        return fileReader.nextLong();
    }

    public float nextFloat() throws IOException {
        return fileReader.nextFloat();
    }

    public double nextDouble() throws IOException {
        return fileReader.nextDouble();
    }

    public String nextWord() throws IOException {
        return fileReader.nextWord();
    }

    private void closeReader() throws IOException {
        fileReader.close();
    }

    public void writeCharacter(final char variableToWrite) throws IOException {
        fileWriter.writeCharacter(variableToWrite);
    }

    public void writeBool(final boolean variableToWrite) throws IOException {
        fileWriter.writeBool(variableToWrite);
    }

    public void writeInt(final int variableToWrite) throws IOException {
        fileWriter.writeInt(variableToWrite);
    }

    public void writeLong(final long variableToWrite) throws IOException {
        fileWriter.writeLong(variableToWrite);
    }

    public void writeFloat(final float variableToWrite) throws IOException {
        fileWriter.writeFloat(variableToWrite);
    }

    public void writeDouble(final double variableToWrite) throws IOException {
        fileWriter.writeDouble(variableToWrite);
    }

    public void writeWord(final String variableToWrite) throws IOException {
        fileWriter.writeWord(variableToWrite);
    }

    public void writeNewLine() throws IOException {
        fileWriter.writeNewLine();
    }

    private void closeWriter() throws IOException {
        fileWriter.close();
    }

    public void close() throws IOException {
        closeReader();
        closeWriter();
    }
}
