
package com.proyectolibreria.demo.repositorios;


import com.proyectolibreria.demo.entidades.Libro;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


  @Repository
public interface LibroRepositorio extends JpaRepository <Libro, String>{
     @Query("select p from Libro p where p.titulo LIKE :q or p.isbn LIKE :q or p.anio LIKE :q or p.autor.nombre LIKE :q")
    List<Libro> findAllByQ(@Param("q") String q);
    
    @Query("select p from Libro p where p.autor.nombre = :q")
    List<Libro> findAllByAutores(@Param("q") String q);
}  

