package com.example.recommendation_service.controller;

import com.example.recommendation_service.model.BETIndex;
import com.example.recommendation_service.service.impl.BETIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class BETIndexController {

    private final BETIndexService BETIndexService;

    @Autowired
    public BETIndexController(BETIndexService BETIndexService) {
        this.BETIndexService = BETIndexService;
    }

    /*// add BETIndex
    @PostMapping(value = "/addStock")
    public ResponseEntity<BETIndex> addStock(@RequestBody BETIndexDTO BETIndexDTO) {
        return ResponseEntity.ok(stockService.addStock(BETIndexDTO));
    }*/

    // get all stocks
    @GetMapping(value = "/getStocks")
    public ResponseEntity<List<BETIndex>> getStocks() {
        List<BETIndex> stocks = BETIndexService.getStocks();
        return new ResponseEntity<>(stocks, HttpStatus.OK);
    }

    @GetMapping(value = "/readStocksOnline")
    public ResponseEntity<List<BETIndex>> readStocksOnline() {
        BETIndexService.readStocksOnline();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
