
package com.proyectolibreria.demo.repositorios;


import com.proyectolibreria.demo.entidades.Cliente;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepositorio extends JpaRepository<Cliente,String>{
   
     @Query("SELECT c FROM Cliente c WHERE c.mail = :mail")
    public Cliente buscarPorMail(@Param("mail") String mail);
   
}
