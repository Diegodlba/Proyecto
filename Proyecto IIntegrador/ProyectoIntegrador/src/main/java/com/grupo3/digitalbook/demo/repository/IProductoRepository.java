package com.grupo3.digitalbook.demo.repository;

import com.grupo3.digitalbook.demo.entity.Producto;
import com.grupo3.digitalbook.demo.service.impl.ProductoService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IProductoRepository extends JpaRepository<Producto, Long> {




}
