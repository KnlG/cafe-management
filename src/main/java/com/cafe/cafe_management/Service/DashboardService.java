package com.cafe.cafe_management.Service;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface DashboardService {

    ResponseEntity<Map<String, Object>> getCount();

}
