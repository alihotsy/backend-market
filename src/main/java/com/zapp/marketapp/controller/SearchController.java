package com.zapp.marketapp.controller;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.zapp.marketapp.service.SearchService;
import com.zapp.marketapp.utils.SearchFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/search")
@CrossOrigin(origins = "http://127.0.0.1:5173/")
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/{type}")
    public ResponseEntity<MappingJacksonValue> searchByType(
            @RequestParam String q,
            @PathVariable String type
    ) throws Exception {

        return ResponseEntity.ok(searchService.searchByType(q,type));
    }

    @GetMapping("/global")
    public ResponseEntity<MappingJacksonValue> globalSearch(
            @RequestParam String q
    ) {
        return ResponseEntity.ok(searchService.globalResults(q));
    }
}
