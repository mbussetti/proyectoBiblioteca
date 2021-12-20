
package com.proyectolibreria.demo.servicios;



import com.proyectolibreria.demo.entidades.Autor;
import com.proyectolibreria.demo.errores.ErrorServicio;
import com.proyectolibreria.demo.repositorios.AutorRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AutorServicio {
     @Autowired    
  private AutorRepositorio autorRepositorio;
  
  @Transactional
  public Autor guardarAutor(String nombre) throws ErrorServicio{
      validar(nombre);
      
      Autor autor=new Autor();
      
      autor.setNombre(nombre);
      autor.setAlta(true);
      
      return autorRepositorio.save(autor);
  }
  
  @Transactional
   public void modificarNombreAutor(String id,String NombreNuevo) throws ErrorServicio{
      
    
        Optional<Autor>respuesta=autorRepositorio.findById(id);
        if(respuesta.isPresent()){
            Autor autor=respuesta.get();
            autor.setNombre(NombreNuevo);
        autorRepositorio.save(autor);
        }else{
            throw new ErrorServicio("No se encuentra el autor");     }
        
       
       }

   @Transactional
      public Autor findById(Autor autor) {
    Optional<Autor> optional = autorRepositorio.findById(autor.getId());
    if (optional.isPresent()) {
      autor = optional.get();
    }
    return autor;
  }

     @Transactional
  public Optional<Autor> findById(String id) {
    return autorRepositorio.findById(id);
  }
     @Transactional
     public Autor bajaAutor (String id) throws ErrorServicio{
          Optional<Autor>respuesta=autorRepositorio.findById(id);
        if(respuesta.isPresent()){
           Autor autor=respuesta.get();
            autor.setAlta(false);
         return autorRepositorio.save(autor);
        }else{
            throw new ErrorServicio("No se encuentra el autor con ese identificador");     }
        
       
     }
     @Transactional
     public Autor altaAutor (String id) throws ErrorServicio{
          Optional<Autor>respuesta=autorRepositorio.findById(id);
        if(respuesta.isPresent()){
           Autor autor=respuesta.get();
            autor.setAlta(true);
         return autorRepositorio.save(autor);
        }else{
            throw new ErrorServicio("No se encuentra el autor con ese identificador");     }
        
       
     }
   
     @Transactional(readOnly = true)
	public List<Autor> listarAlta() {
		return autorRepositorio.buscarAlta();
	}
      
        @Transactional
      public List<Autor> listAll() {
    
          return autorRepositorio.findAll();
  }
     
      @Transactional
     public List<Autor> listAll(String q) {
    return autorRepositorio.findAll("%" + q + "%");
  }

   public void validar(String nombre) throws ErrorServicio{
       if(nombre==null || nombre.isEmpty()){
           throw new ErrorServicio ("el nombre no puede ser nulo");
       }
      
    }
    
}
