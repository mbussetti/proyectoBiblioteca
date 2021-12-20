
package com.proyectolibreria.demo.servicios;


import com.proyectolibreria.demo.entidades.Editorial;
import com.proyectolibreria.demo.errores.ErrorServicio;
import com.proyectolibreria.demo.repositorios.EditorialRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EditorialServicio {
   
    
  @Autowired    
  private EditorialRepositorio editorialRepositorio;
  
  @Transactional
  public Editorial guardar(String nombre) throws ErrorServicio{
      validar(nombre);
      
      Editorial editorial=new Editorial();
      
      editorial.setNombre(nombre);
      editorial.setAlta(true);
      
      return editorialRepositorio.save(editorial);
  }
  
  @Transactional
   public void modificarNombreEditorial(String id,String NombreNuevo) throws ErrorServicio{
      
    
        Optional<Editorial>respuesta=editorialRepositorio.findById(id);
        if(respuesta.isPresent()){
            Editorial editorial=respuesta.get();
            editorial.setNombre(NombreNuevo);
        editorialRepositorio.save(editorial);
        }else{
            throw new ErrorServicio("No se encuentra laeditorial");     }
        
       
       }
   @Transactional
     public Editorial buscarEditorialPorId(String id) throws Exception {
       
          Editorial editorial =editorialRepositorio.getById(id);
           
            if (editorial == null) {
                throw new Exception("No se econtró el autor para el id indicado");
            }
            return editorial;
       
       
    }
      @Transactional
     public Editorial  bajaEditorial (String id) throws ErrorServicio{
          Optional<Editorial>respuesta=editorialRepositorio.findById(id);
        if(respuesta.isPresent()){
           Editorial editorial=respuesta.get();
            editorial.setAlta(false);
         return editorialRepositorio.save(editorial);
        }else{
            throw new ErrorServicio("No se encuentra la editorial con ese identificador");     }
        
       
     }
     @Transactional
     public Editorial altaEditorial (String id) throws ErrorServicio{
          Optional<Editorial>respuesta=editorialRepositorio.findById(id);
        if(respuesta.isPresent()){
                Editorial editorial=respuesta.get();
            editorial.setAlta(true);
         return editorialRepositorio.save(editorial);
        }else{
            throw new ErrorServicio("No se encuentra la editorial con ese identificador");     }
        
       
     }
    
     
     @Transactional(readOnly = true)
	public List<Editorial> listarAlta() {
		return editorialRepositorio.buscarAlta();
	}
      
        @Transactional
      public List<Editorial> listAll() {
    
          return editorialRepositorio.findAll();
  }
     
      @Transactional
     public List<Editorial> listAll(String q) {
    return editorialRepositorio.findAll("%" + q + "%");
  }
   public void validar(String nombre) throws ErrorServicio{
       if(nombre==null || nombre.isEmpty()){
           throw new ErrorServicio ("el nombre no puede ser nulo");
       }
      
    }
     @Transactional
      public  Editorial findById(Editorial editorial) {
    Optional<Editorial> optional = editorialRepositorio.findById(editorial.getId());
    if (optional.isPresent()) {
      editorial = optional.get();
    }
    return editorial;
  }

     @Transactional
  public Optional<Editorial> findById(String id) {
    return editorialRepositorio.findById(id);
  }
  
}
