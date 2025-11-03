package com.diwali.sales_analytics.controller;

import com.diwali.sales_analytics.service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import com.diwali.sales_analytics.model.Sale;

@RestController
@RequestMapping("/api/sales")
@CrossOrigin(origins = "*")
public class SalesController {

    @Autowired
    private SalesService salesService;

    @GetMapping("/all")
    public List<Sale> getAllSales() {
        return salesService.getAllSales();
    }

    @GetMapping("/total-by-state")
    public Map<String, Double> getTotalByState() {
        return salesService.getTotalSalesByState();
    }

    @GetMapping("/top-state")
    public String getTopState() {
        return salesService.getTopPerformingState();
    }

    @GetMapping("/by-zone")
    public Map<String, Double> getSalesByZone() {
        return salesService.getTotalSalesByZone();
    }

    @GetMapping("/by-gender")
    public Map<String, Double> getSalesByGender() {
        return salesService.getTotalSalesByGender();
    }

    @GetMapping("/insights")
    public Map<String, Object> getInsights() {
        return salesService.getSalesInsights();
    }
}