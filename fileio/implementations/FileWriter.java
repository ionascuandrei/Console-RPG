package fileio.implementations;

import fileio.interfaces.IWriter;

import java.io.BufferedWriter;
import java.io.IOException;

public final class FileWriter implements IWriter {
    private java.io.FileWriter fileWriter;
    private BufferedWriter bufferedWriter;

    public FileWriter(final String filePath) throws IOException {
        fileWriter = new java.io.FileWriter(filePath);
        bufferedWriter = new BufferedWriter(fileWriter);
    }

    @Override
    public void writeCharacter(final char variableToWrite) throws IOException {
        bufferedWriter.write(variableToWrite);
    }

    @Override
    public void writeBool(final boolean variableToWrite) throws IOException {
        bufferedWriter.write(String.valueOf(variableToWrite));
    }

    @Override
    public void writeInt(final int variableToWrite) throws IOException {
        bufferedWriter.write(String.valueOf(variableToWrite));
    }

    @Override
    public void writeLong(final long variableToWrite) throws IOException {
        bufferedWriter.write(String.valueOf(variableToWrite));
    }

    @Override
    public void writeFloat(final float variableToWrite) throws IOException {
        bufferedWriter.write(String.valueOf(variableToWrite));
    }

    @Override
    public void writeDouble(final double variableToWrite) throws IOException {
        bufferedWriter.write(String.valueOf(variableToWrite));
    }

    @Override
    public void writeWord(final String variableToWrite) throws IOException {
        bufferedWriter.write(variableToWrite);
    }

    @Override
    public void writeNewLine() throws IOException {
        bufferedWriter.newLine();
    }

    @Override
    public void close() throws IOException {
        bufferedWriter.close();
        fileWriter.close();
    }
}
