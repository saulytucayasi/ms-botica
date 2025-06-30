package com.example.ms_movimientos.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "ms-inventario", path = "/inventario")
public interface InventarioFeign {
    
    @PutMapping("/actualizar-stock")
    ResponseEntity<Object> actualizarStock(@RequestBody Map<String, Object> request);
}