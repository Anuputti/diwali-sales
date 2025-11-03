package com.diwali.sales_analytics.service;

import com.diwali.sales_analytics.model.Sale;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SalesService {

    public List<Sale> getAllSales() {
        List<Sale> sales = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("/diwali_sales.csv")))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length > 12 && !data[12].isEmpty()) {
                    String userId = data[0];
                    String custName = data[1];
                    String state = data[7];
                    String zone = data[8];
                    String gender = data[3];
                    double amount = Double.parseDouble(data[12]);
                    sales.add(new Sale(userId, custName, state, zone, gender, amount));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sales;
    }

    public Map<String, Double> getTotalSalesByState() {
        return getAllSales().stream()
                .collect(Collectors.groupingBy(Sale::getState, Collectors.summingDouble(Sale::getAmount)));
    }

    public String getTopPerformingState() {
        return getTotalSalesByState().entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("No Data");
    }

    public Map<String, Double> getTotalSalesByZone() {
        return getAllSales().stream()
                .collect(Collectors.groupingBy(Sale::getZone, Collectors.summingDouble(Sale::getAmount)));
    }

    public Map<String, Double> getTotalSalesByGender() {
        return getAllSales().stream()
                .collect(Collectors.groupingBy(Sale::getGender, Collectors.summingDouble(Sale::getAmount)));
    }

    public Map<String, Object> getSalesInsights() {
        Map<String, Object> insights = new HashMap<>();
        insights.put("totalSales", getAllSales().stream().mapToDouble(Sale::getAmount).sum());
        insights.put("topState", getTopPerformingState());
        insights.put("totalStates", getTotalSalesByState().size());
        insights.put("zones", getTotalSalesByZone().size());
        insights.put("genders", getTotalSalesByGender().size());
        return insights;
    }
}