package com.margin.Serivce;

import com.margin.entity.LotSize;
import com.opencsv.CSVReader;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvReader {

    String DELIMITER = ",";

    public Map<String, Map<String, String>> getClientMargins(MultipartFile file) throws IOException {

        Map<String, Map<String, String>> dataValues = new HashMap<>();
        BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
        CSVReader csvReader = new CSVReader(fileReader);
        String line;

        while ((line = fileReader.readLine()) != null) {
            String[] values = line.split(DELIMITER);
            Map<String, String> data = new HashMap<>();
            data.put("CM ID", values[0]);
            data.put("Member Id", values[1]);
            data.put("Client Id", values[2]);
            data.put("margin", values[9]);
            dataValues.put(values[2], data);
        }
        csvReader.close();
        return dataValues;
    }

    public Map<String, Map<String, String>> getIntraday(MultipartFile file, String segment) throws IOException {

        Map<String, Map<String, String>> dataValues = new HashMap<>();
        BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
        CSVReader csvReader = new CSVReader(fileReader);
        String line;

        while ((line = fileReader.readLine()) != null) {
            String[] values = line.split(DELIMITER);
            Map<String, String> data = new HashMap<>();
            data.put("TRADE_DATE", values[0]);
            if(segment.equalsIgnoreCase("FO"))
            {
                data.put("TOTAL_MARGIN", values[9]);
                data.put("CLIENT_TYPE", values[10]);
            }
            if(segment.equalsIgnoreCase("EQ") || segment.equalsIgnoreCase("CM"))
            {
                data.put("TOTAL_MARGIN", values[9]);
                data.put("CLIENT_TYPE", values[8]);
            }
            if(segment.equalsIgnoreCase("CD"))
            {
                data.put("TOTAL_MARGIN", values[8]);
                data.put("CLIENT_TYPE", values[9]);
            }
            dataValues.put(values[1], data);
        }
        csvReader.close();
        return dataValues;
    }

    public Map<String, Double> getMTM(MultipartFile file, String segment) throws IOException {

        Map<String, Double> dataValues = new HashMap<>();
        BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
        CSVReader csvReader = new CSVReader(fileReader);
        String line;

        while ((line = fileReader.readLine()) != null) {
            String[] values = line.split(DELIMITER);
            if (dataValues.containsKey(values[1]))
                if (segment.equalsIgnoreCase("MCX"))
                    dataValues.replace(values[1], dataValues.get(values[1]), dataValues.get(values[1])+ Math.abs(values[13].isEmpty() ? 0.0 : Double.parseDouble(values[13])));
                else
                    dataValues.replace(values[1], dataValues.get(values[1]), dataValues.get(values[1])+ Math.abs(Double.parseDouble(values[12])));
            else
                if (segment.equalsIgnoreCase("MCX"))
                    dataValues.put(values[1], Math.abs(values[13].isEmpty() ? 0.0 : Double.parseDouble(values[13])));
                else
                    dataValues.put(values[1], Math.abs(Double.parseDouble(values[12])));
        }
        csvReader.close();
        return dataValues;
    }

    public Map<String, List<Map<String, Double>>> getDailyMargin(MultipartFile file) throws IOException {
        Map<String, List<Map<String, Double>>> dataValues = new HashMap<>();
        BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
        CSVReader csvReader = new CSVReader(fileReader);
        String line;

        while ((line = fileReader.readLine()) != null) {
            Map<String, Double> data = new HashMap<>();
            String[] values = line.split(DELIMITER);
            List<Map<String, Double>> listValues = new ArrayList<>();
            double margin = Double.parseDouble(values[7]) + Double.parseDouble(values[8]) + Double.parseDouble(values[10]) +Double.parseDouble(values[12]) + Double.parseDouble(values[14]);
            data.put(values[4], margin);

            if(dataValues.containsKey(values[3]))
            {
                listValues = dataValues.get(values[3]);
                listValues.add(data);
                dataValues.replace(values[3], dataValues.get(values[3]), listValues);
            }
            else
            {
                listValues.add(data);
                dataValues.put(values[3], listValues);
            }
        }
        csvReader.close();
        return dataValues;
    }

    public Map<String, Double> getNetPositions(MultipartFile file, Map<String, List<Map<String, Double>>> dailyMargin) throws IOException {
        Map<String, Double> dataValues = new HashMap<>();
        BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
        CSVReader csvReader = new CSVReader(fileReader);
        String line;
        LotSize lotSize = new LotSize();

        while ((line = fileReader.readLine()) != null) {
            String[] values = line.split(DELIMITER);
            String lot = values[2];
            double eod, ltp, margin = 0.0;
            double size = lotSize.getLotSize(lot);
            double qty =values[23].isEmpty() ? 0.0 : Double.parseDouble(values[23]);
            List<Map<String, Double>> dailyMList = dailyMargin.get(lot);

            if (dataValues.containsKey(values[1])) {
                eod = dataValues.get(values[1]);

                ltp = getLTP(file, values[1], values[2]);

                for (Map<String, Double> dailyM : dailyMList)
                {
                    String date = dailyM.keySet().iterator().next();
                    if(date.equalsIgnoreCase(values[7].replaceAll(" ", "")))
                        margin = dailyM.get(date);
                }

                eod += qty * size * ltp * margin / 100;
            }
            else {
                ltp = getLTP(file, values[1], values[2]);

                for (Map<String, Double> dailyM : dailyMList)
                {
                    String date = dailyM.keySet().iterator().next();
                    if(date.equalsIgnoreCase(values[7].replaceAll(" ", "")))
                        margin = dailyM.get(date);
                }

                eod = qty * size * ltp * margin / 100;
            }

            dataValues.put(values[1], eod);
        }
        csvReader.close();
        return dataValues;
    }

    public Map<String, Double> getPeakMargins(MultipartFile file) throws IOException {

        Map<String, Double> dataValues = new HashMap<>();
        BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
        CSVReader csvReader = new CSVReader(fileReader);
        String line;

        while ((line = fileReader.readLine()) != null) {
            String[] values = line.split(DELIMITER);
            dataValues.put(values[4], Double.valueOf(values[8]));
        }

        csvReader.close();
        return dataValues;
    }

    private double getLTP(MultipartFile file, String clientId, String lot) throws IOException {

        BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
        CSVReader csvReader = new CSVReader(fileReader);
        String line;
        double ltp = 0.0;
        while ((line = fileReader.readLine()) != null) {
            String[] values = line.split(DELIMITER);

            if(values[1].equalsIgnoreCase(clientId) && values[2].equalsIgnoreCase(lot))
                ltp = Double.parseDouble(values[14]);
        }

        csvReader.close();
        return ltp;
    }
}
