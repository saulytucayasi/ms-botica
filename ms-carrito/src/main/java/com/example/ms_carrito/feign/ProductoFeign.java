package com.example.ms_carrito.feign;

import com.example.ms_carrito.dto.ProductoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-producto-service", fallback = ProductoFeignFallback.class)
public interface ProductoFeign {
    
    @GetMapping("/productos/{id}")
    ResponseEntity<ProductoDto> obtenerProducto(@PathVariable Integer id);
}