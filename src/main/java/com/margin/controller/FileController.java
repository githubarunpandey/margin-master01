package com.margin.controller;

import com.margin.Serivce.CsvReader;
import com.margin.Serivce.CsvWriter;
import com.margin.Serivce.FinalMargin;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class FileController {

    @PostMapping(value = "/check")
    public void check( @RequestParam("file") List<MultipartFile> files, HttpServletResponse response) throws ServletException, IOException {
        for (MultipartFile file : files) {
            System.out.println(file.getOriginalFilename());
        }
        response.sendError(200, "working");
    }

    @PostMapping(value = "/calculateMargin", produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE })
    public void processCsvFile(@NonNull @RequestParam("file") List<MultipartFile> files, @RequestParam("segment") String segment, HttpServletResponse response) throws IOException {

        Map<String, Map<String, String>> clientMargin = null;
        Map<String, Map<String, String>> intraday_01 = null;
        Map<String, Map<String, String>> intraday_02 = null;
        Map<String, Map<String, String>> intraday_03 = null;
        Map<String, Map<String, String>> intraday_04 = null;
        Map<String, Double> netwise = null;
        CsvReader csvReader = new CsvReader();
        String resultFileName = segment+"_ICCLCOLL_6670_"+ date() +"_0001.csv";

        for (MultipartFile file : files)
        {
            String filename = file.getOriginalFilename().substring(0, file.getOriginalFilename().length() - 4);
            String type = file.getOriginalFilename().substring(file.getOriginalFilename().length() - 3);
            if (!type.equalsIgnoreCase("csv"))
                throw new FileNotFoundException("Invalid file type");

            System.out.println(filename);
            if (filename.toLowerCase().contains("clientmarginutil"))
                clientMargin = csvReader.getClientMargins(file);
            if (filename.toLowerCase().contains("intraday") && filename.endsWith("_01"))
                intraday_01 = csvReader.getIntraday(file, segment);
            if (filename.toLowerCase().contains("intraday") && filename.endsWith("_02"))
                intraday_02 = csvReader.getIntraday(file, segment);
            if (filename.toLowerCase().contains("intraday") && filename.endsWith("_03"))
                intraday_03 = csvReader.getIntraday(file, segment);
            if (filename.toLowerCase().contains("intraday") && filename.endsWith("_04"))
                intraday_04 = csvReader.getIntraday(file, segment);
            if (filename.toLowerCase().contains("netwise"))
                netwise = csvReader.getMTM(file, segment);
        }

        List<String[]> finalMargin = (new FinalMargin()).calculateMargin(clientMargin, intraday_01, intraday_02, intraday_03, intraday_04, netwise, segment);
        System.out.println(finalMargin.size());

        response.setContentType("text/csv");
        response.addHeader("Content-Disposition","attachment; filename="+resultFileName);
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, remember-me");
        new CsvWriter().exportCsv(response.getWriter(), finalMargin);
    }

    @PostMapping(value = "/calculateMCXMargin", produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE })
    public void processMcxMargin(@NonNull @RequestParam("file") MultipartFile[] files, HttpServletResponse response) throws IOException {
        Map<String, List<Map<String, Double>>> dailyMargin = null;
        Map<String, Double> peakMargin_01 = null;
        Map<String, Double> peakMargin_02 = null;
        Map<String, Double> peakMargin_03 = null;
        Map<String, Double> peakMargin_04 = null;
        Map<String, Double> peakMargin_05 = null;
        Map<String, Double> peakMargin_06 = null;
        Map<String, Double> peakMargin_07 = null;
        Map<String, Double> peakMargin_08 = null;
        Map<String, Double> eod = null;
        Map<String, Double> netwise = null;
        CsvReader csvReader = new CsvReader();
        String resultFileName = "MCX_ICCLCOLL_6670_"+ date() +"_0001.csv";

        for (MultipartFile file : files)
        {
            String filename = file.getOriginalFilename().substring(0, file.getOriginalFilename().length() - 4);
            String type = file.getOriginalFilename().substring(file.getOriginalFilename().length() - 3);
            if (!type.equalsIgnoreCase("csv"))
                throw new FileNotFoundException("Invalid file type");

            System.out.println(filename);
            if (filename.toLowerCase().contains("dailymargin"))
                dailyMargin = csvReader.getDailyMargin(file);
            if (filename.toLowerCase().contains("peakmargin") && filename.endsWith("_01"))
                peakMargin_01 = csvReader.getPeakMargins(file);
            if (filename.toLowerCase().contains("peakmargin") && filename.endsWith("_02"))
                peakMargin_02 = csvReader.getPeakMargins(file);
            if (filename.toLowerCase().contains("peakmargin") && filename.endsWith("_03"))
                peakMargin_03 = csvReader.getPeakMargins(file);
            if (filename.toLowerCase().contains("peakmargin") && filename.endsWith("_04"))
                peakMargin_04 = csvReader.getPeakMargins(file);
            if (filename.toLowerCase().contains("peakmargin") && filename.endsWith("_05"))
                peakMargin_05 = csvReader.getPeakMargins(file);
            if (filename.toLowerCase().contains("peakmargin") && filename.endsWith("_06"))
                peakMargin_06 = csvReader.getPeakMargins(file);
            if (filename.toLowerCase().contains("peakmargin") && filename.endsWith("_07"))
                peakMargin_07 = csvReader.getPeakMargins(file);
            if (filename.toLowerCase().contains("peakmargin") && filename.endsWith("_08"))
                peakMargin_08 = csvReader.getPeakMargins(file);
            if (filename.toLowerCase().contains("netwise")) {
                netwise = csvReader.getMTM(file, "MCX");
                eod = csvReader.getNetPositions(file, dailyMargin);
            }
        }

        List<String[]> finalMargin = (new FinalMargin()).calculateMCXMargin(netwise, eod, peakMargin_01, peakMargin_02,
                                                                            peakMargin_03, peakMargin_04, peakMargin_05, peakMargin_06, peakMargin_07,peakMargin_08);

        response.setContentType("text/csv");
        response.addHeader("Content-Disposition","attachment; filename="+resultFileName);
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, remember-me");
        new CsvWriter().exportCsv(response.getWriter(), finalMargin);
    }

    private String date()
    {
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("ddMMyyyy");
        Date date = null;
        try {
            System.out.println(String.valueOf(LocalDate.now()));
            date = inputFormat.parse(String.valueOf(LocalDate.now()));
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputFormat.format(date);
    }

}
