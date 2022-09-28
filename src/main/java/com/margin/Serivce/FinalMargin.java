package com.margin.Serivce;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FinalMargin {

    public List<String[]> calculateMargin (Map<String, Map<String, String>> clientMargin,
                                           Map<String, Map<String, String>> intraday_01,
                                           Map<String, Map<String, String>> intraday_02,
                                           Map<String, Map<String, String>> intraday_03,
                                           Map<String, Map<String, String>>intraday_04,
                                           Map<String, Double> netwise, String segment) {

        List<String[]> result = new ArrayList<>();

        for (String clientId: clientMargin.keySet()) {
            String[] rowData =  new String[15];
            rowData[1] = segment;
            rowData[2] = clientMargin.get(clientId).get("CM ID");
            rowData[3] = clientMargin.get(clientId).get("Member Id");
            rowData[5] = clientId;
            rowData[14] = "A";

            double client_margin = Double.parseDouble(clientMargin.get(clientId).get("margin"));
            double mtm = (netwise == null) ? 0.0 : netwise.getOrDefault(clientId, 0.0);
            double margin1 = 0.0, margin2 = 0.0, margin3 = 0.0, margin4 = 0.0;

            if(intraday_01.containsKey(clientId))
            {
                margin1 = Double.parseDouble(intraday_01.get(clientId).get("TOTAL_MARGIN"));
//                rowData[0] = intraday_01.get(clientId).get("TRADE_DATE");
//                rowData[6] = intraday_01.get(clientId).get("CLIENT_TYPE");
            }

            if (intraday_02.containsKey(clientId))
            {
                margin2 = Double.parseDouble(intraday_02.get(clientId).get("TOTAL_MARGIN"));
//                rowData[0] = intraday_02.get(clientId).get("TRADE_DATE");
//                rowData[6] = intraday_02.get(clientId).get("CLIENT_TYPE");
            }

            if (intraday_03.containsKey(clientId))
            {
                margin3 = Double.parseDouble(intraday_03.get(clientId).get("TOTAL_MARGIN"));
//                rowData[0] = intraday_03.get(clientId).get("TRADE_DATE");
//                rowData[6] = intraday_03.get(clientId).get("CLIENT_TYPE");
            }

            if (intraday_04.containsKey(clientId))
            {
                margin4 = Double.parseDouble(intraday_04.get(clientId).get("TOTAL_MARGIN"));
//                rowData[0] = intraday_04.get(clientId).get("TRADE_DATE");
//                rowData[6] = intraday_04.get(clientId).get("CLIENT_TYPE");
            }

                rowData[0] = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MMM-yy"));
                rowData[6] = "C";

            double margin = getMaxTotalMargin( margin1, margin2, margin3, margin4);
            client_margin = Math.max(client_margin, margin);

            rowData[7] = calFinalMargin(client_margin, mtm).toString();

            result.add(rowData);
        }

        return result;
    }


    public final List<String[]> calculateMCXMargin(Map<String, Double> netwise, Map<String, Double> eodMargin,
                                                   Map<String, Double> peakMargin1, Map<String, Double> peakMargin2,
                                                   Map<String, Double> peakMargin3, Map<String, Double> peakMargin4, Map<String, Double> peakMargin5,
                                                   Map<String, Double> peakMargin6, Map<String, Double> peakMargin7, Map<String, Double> peakMargin8) {
        List<String[]> result = new ArrayList<>();

        for (String clientId: eodMargin.keySet()) {
            String[] rowData =  new String[15];
            rowData[0] = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MMM-yy"));
            rowData[1] = "MCX";
            rowData[2] = "6670";
            rowData[3] = "667001";
            rowData[5] = clientId;
            rowData[14] = "A";


            double mtm = netwise.getOrDefault(clientId, 0.0);
            double eod = eodMargin.get(clientId);

            double peak = getMaxTotalMargin(peakMargin1.getOrDefault(clientId, 0.0), peakMargin2.getOrDefault(clientId, 0.0), peakMargin3.getOrDefault(clientId, 0.0), peakMargin4.getOrDefault(clientId, 0.0),
                    peakMargin5.getOrDefault(clientId, 0.0), peakMargin6.getOrDefault(clientId, 0.0), peakMargin7.getOrDefault(clientId, 0.0), peakMargin8.getOrDefault(clientId, 0.0));

            eod = Math.max(eod, peak);

            rowData[7] = calFinalMargin(eod, mtm).toString();
            result.add(rowData);
        }

        return result;
    }


    private static Double calFinalMargin(Double margin, Double mtm) {
        double finalMargin = 0.0;

        finalMargin = margin + mtm;
        finalMargin += finalMargin/100;

        return finalMargin;
    }

    private double getMaxTotalMargin(double... peak) {
        double total = 0.0;
        int c=0;
        for (double p : peak)
        {
            total = Math.max(total, p);
            c++;
        }

        return total;
    }

}
