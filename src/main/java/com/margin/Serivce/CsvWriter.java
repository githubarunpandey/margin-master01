package com.margin.Serivce;

import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.Writer;
import java.util.List;

public class CsvWriter {

    public void exportCsv(Writer writer, List<String[]> data)
    {

        CSVWriter csvWriter = new CSVWriter(writer);
        csvWriter.writeAll(data);
    }
}
