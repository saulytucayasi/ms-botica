package com.example.ms_producto.feign;

import com.example.ms_producto.dto.CategoriaDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-catalogo-service", path = "/categorias")
public interface CatalogoFeign {
    @GetMapping("/{id}")
    @CircuitBreaker(name = "categoriaListarPorIdCB", fallbackMethod = "fallbackCategoriaListarById")
    public ResponseEntity<CategoriaDto> buscarCategoria(@PathVariable Integer id);

    default ResponseEntity<CategoriaDto> fallbackCategoriaListarById(Integer id, Exception e) {
        CategoriaDto categoriaDto = new CategoriaDto();
        categoriaDto.setNombre("Servicio no disponible de categoria");
        return ResponseEntity.ok(categoriaDto);
    }
}
