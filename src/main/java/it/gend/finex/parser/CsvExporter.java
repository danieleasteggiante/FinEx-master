package it.gend.finex.parser;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class CsvExporter {
    public static File writeCSV(String content, File output) throws IOException {
        FileWriter fileWriter = new FileWriter(output);
        fileWriter.write(content);
        fileWriter.close();
        return output;
    }

}
