
package com.proyectolibreria.demo.repositorios;


import com.proyectolibreria.demo.entidades.Autor;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AutorRepositorio extends JpaRepository <Autor, String>{

     @Query("select p from Autor p where p.nombre LIKE :q")
      List<Autor> findAll(@Param("q") String q);
  
      @Query("SELECT a from Autor a WHERE a.alta = true ")
	public List<Autor> buscarAlta();
    
}
