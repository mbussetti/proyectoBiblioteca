
package com.proyectolibreria.demo.repositorios;


import com.proyectolibreria.demo.entidades.Foto;
import org.springframework.data.jpa.repository.JpaRepository;



   public interface FotoRepositorio extends JpaRepository<Foto, String> {
}
