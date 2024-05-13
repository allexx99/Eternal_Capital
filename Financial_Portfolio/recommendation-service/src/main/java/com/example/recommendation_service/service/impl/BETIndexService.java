package com.example.recommendation_service.service.impl;

import com.example.recommendation_service.dto.BETIndexDTO;
import com.example.recommendation_service.model.BETIndex;
import com.example.recommendation_service.repository.BETIndexRepo;
import com.example.recommendation_service.service.BETIndexServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.FileWriter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Service
public class BETIndexService implements BETIndexServiceInterface {
    private final BETIndexRepo BETIndexRepo;

    private final String BVB_URL = "https://m.bvb.ro/FinancialInstruments/Indices/IndicesProfiles.aspx?i=BET";
    private final String TABLE_ID = "gvTD";
    private final String[] DESIRED_COLUMNS = {"Simbol", "Pret", "Pondere (%)"};
    private final String CRAWLER_CACHE = "crawler_bvb_cache.json";

    @Autowired
    public BETIndexService(BETIndexRepo BETIndexRepo) {
        this.BETIndexRepo = BETIndexRepo;
    }

    @Override
    public BETIndex addStock(BETIndexDTO BETIndexDTO) {
        BETIndex BETIndex = BETIndexDTO.convertToModel(BETIndexDTO);
        BETIndexRepo.save(BETIndex);
        return BETIndex;
    }

    @Override
    public BETIndex getStockById(long id) {
        return BETIndexRepo.findById(id);
    }

    @Override
    public List<BETIndex> getStocks() {
        return BETIndexRepo.findAll();
    }

    public void readStocksOnline() {
        Map<String, Map<String, String>> result = crawl();
        if (result != null) {
            // Print the obtained data
            for (Map.Entry<String, Map<String, String>> entry : result.entrySet()) {
                String symbol = entry.getKey();
                Map<String, String> data = entry.getValue();
                System.out.println("Symbol: " + symbol);
                System.out.println("Data: " + data);
            }
        } else {
            System.out.println("Failed to fetch data from the website.");
        }
    }

    private Map<String, Map<String, String>> crawl() {
        try {
            Document doc = Jsoup.connect(BVB_URL).get();
            Element table = doc.getElementById(TABLE_ID);
            if (table != null) {
                Elements rows = table.select("tr");
                List<Integer> columnIndexes = new ArrayList<>();
                List<List<String>> data = new ArrayList<>();

                // Find column indexes for desired columns
                Element headerRow = rows.get(0);
                Elements thElements = headerRow.select("th");
                for (int i = 0; i < thElements.size(); i++) {
                    Element th = thElements.get(i);
                    String columnName = th.text();
                    for (String desiredColumn : DESIRED_COLUMNS) {
                        if (columnName.equals(desiredColumn)) {
                            columnIndexes.add(i);
                            break;
                        }
                    }
                }

                // Extract table data for desired columns
                for (int i = 1; i < rows.size(); i++) {
                    Element row = rows.get(i);
                    Elements tdElements = row.select("td");
                    List<String> rowData = new ArrayList<>();
                    for (int index : columnIndexes) {
                        if (index < tdElements.size()) {
                            rowData.add(tdElements.get(index).text());
                        }
                    }
                    data.add(rowData);
                }

//                BETIndexRepo.deleteAll();
                // Process and cache the data
                Map<String, Map<String, String>> result = new HashMap<>();
                for (List<String> rowData : data) {
                    if (rowData.size() >= DESIRED_COLUMNS.length) {

                        /*// create BETIndex object and save it to the database
                        BETIndexDTO BETIndexDTO = new BETIndexDTO();
                        BETIndexDTO.setSymbol(rowData.get(0));

                        // convert price and weight from string to float
                        String stockPrice = rowData.get(1).replace(",", ".");
                        String stockWeight = rowData.get(2).replace(",", ".");

                        BETIndexDTO.setPrice(Float.parseFloat(stockPrice));
                        BETIndexDTO.setWeight(Float.parseFloat(stockWeight));
                        BETIndex BETIndex = BETIndexDTO.convertToModel(BETIndexDTO);

                        BETIndexRepo.save(BETIndex);*/

                        // new save method

                        String symbol1 = rowData.get(0); // Assuming the first column is symbol

                        // convert price and weight from string to float
                        String stockPrice = rowData.get(1).replace(",", ".");
                        String stockWeight = rowData.get(2).replace(",", ".");

                        BETIndex betIndex = BETIndexRepo.findBySymbol(symbol1);
                        System.out.println(betIndex);
                        if(betIndex == null) {
                            // create BETIndex object and save it to the database
                            BETIndexDTO BETIndexDTO = new BETIndexDTO();
                            BETIndexDTO.setSymbol(rowData.get(0));

                            BETIndexDTO.setPrice(Float.parseFloat(stockPrice));
                            BETIndexDTO.setWeight(Float.parseFloat(stockWeight));
                            BETIndex BETIndex = BETIndexDTO.convertToModel(BETIndexDTO);

                            BETIndexRepo.save(BETIndex);
                        } else {
                            // update BETIndex object
                            betIndex.setPrice(Float.parseFloat(stockPrice));
                            betIndex.setWeight(Float.parseFloat(stockWeight));
                            BETIndexRepo.save(betIndex);
                        }

                        // new save method

                        String symbol = rowData.get(0); // Assuming the first column is symbol
                        Map<String, String> symbolData = new HashMap<>();
                        for (int i = 0; i < DESIRED_COLUMNS.length; i++) {
                            symbolData.put(DESIRED_COLUMNS[i], rowData.get(i));
                        }
                        result.put(symbol, symbolData);
                    }
                }
                cacheData(result);
                return result;
            } else {
                System.out.println("Table not found.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void cacheData(Map<String, Map<String, String>> data) {
        long crtTs = System.currentTimeMillis() / 1000;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(new CacheData(crtTs, data));

        try (FileWriter writer = new FileWriter(CRAWLER_CACHE)) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class CacheData {
        long ts;
        Map<String, Map<String, String>> data;

        public CacheData(long ts, Map<String, Map<String, String>> data) {
            this.ts = ts;
            this.data = data;
        }
    }
}
