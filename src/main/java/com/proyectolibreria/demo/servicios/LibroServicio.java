
package com.proyectolibreria.demo.servicios;


import com.proyectolibreria.demo.entidades.Foto;
import com.proyectolibreria.demo.entidades.Libro;
import com.proyectolibreria.demo.errores.ErrorServicio;
import com.proyectolibreria.demo.repositorios.LibroRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class LibroServicio {
    @Autowired    
  private LibroRepositorio libroRepositorio;
    @Autowired
    private AutorServicio autorServicio;
    @Autowired
    private EditorialServicio editorialServicio;
     @Autowired
    private FotoServicio fotoServicio;
  
  @Transactional
  public Libro guardar1(MultipartFile archivo,Long isbn,String titulo,Integer anio,Integer ejemplares,String idAutor, String idEditorial, String sinapsis) throws ErrorServicio{
      validar(titulo,isbn,anio,ejemplares);
      
       Libro libro = new Libro();

            libro.setIsbn(isbn);
            libro.setTitulo(titulo);
            libro.setAnio(anio);
            libro.setEjemplares(ejemplares);
            libro.setEjemplaresPrestados(null);
            libro.setEjemplaresRestantes(null);
            libro.setSinapsis(sinapsis);
            libro.setAlta(true);
            Foto foto=fotoServicio.guardar(archivo);
            libro.setFoto(foto);
            libro.setAutor(autorServicio.findById(libro.getAutor()));
            libro.setEditorial(editorialServicio.findById(libro.getEditorial()));
            
            
      return libroRepositorio.save(libro);
  }
   @Transactional
  public Libro guardar(Libro libro) throws ErrorServicio {
     if (libro.getIsbn() == null) {
      throw new ErrorServicio(" El ISBN no puede estar vacío");
    }
      
     if (libro.getTitulo().isEmpty() || libro.getTitulo() == null ) {
      throw new ErrorServicio(" El titulo no puede estar vacío");
    }
    if (libro.getAnio() == null) {
      throw new ErrorServicio(" El año no puede estar vacío");
    }
     if (libro.getEjemplares() == null) {
      throw new ErrorServicio(" La cantidad de ejemplares no puede estar vacía");
    }
    
    if (libro.getAutor() == null) {
      throw new ErrorServicio(" El autor no puede ser nulo");
    } else {
      libro.setAutor(autorServicio.findById(libro.getAutor()));
    }
     if (libro.getEditorial()== null) {
      throw new ErrorServicio(" La editorial no puede ser nula");
    } else {
      libro.setEditorial(editorialServicio.findById(libro.getEditorial()));
    }
     
    
    return libroRepositorio.save(libro);
  }
  
  @Transactional
   public void modificarLibro(MultipartFile archivo,String id,Long isbn,String titulo,Integer anio,Integer ejemplares,Integer ejemplaresPrestados,String sinapsis) throws ErrorServicio{
      
    
        Optional<Libro>respuesta=libroRepositorio.findById(id);
        if(respuesta.isPresent()){
            Libro libro=respuesta.get();
            libro.setIsbn(isbn);
            libro.setTitulo(titulo);
            libro.setAnio(anio);
            libro.setEjemplares(ejemplares);
            libro.setEjemplaresPrestados(ejemplaresPrestados);
            libro.setEjemplaresRestantes(ejemplares-ejemplaresPrestados);            
            libro.setAlta(true);
             String idFoto = null;
            if (libro.getFoto() != null) {
                idFoto = libro.getFoto().getId();
            }

            Foto foto = fotoServicio.actualizar(idFoto, archivo);
            libro.setFoto(foto);
            libro.setSinapsis(sinapsis);
            
       libroRepositorio.save(libro);
        }else{
            throw new ErrorServicio("No se encuentra ellibro");     }
        
       
       }
    public Optional<Libro> findById(String id) {
    return libroRepositorio.findById(id);
  }
    public List<Libro> listAll() {
    return libroRepositorio.findAll();
  }

  public List<Libro> listAllByQ(String q) {
    return libroRepositorio.findAllByQ("%" + q + "%");
  }

  public List<Libro> listAllbyAutores(String nombre) {
    return libroRepositorio.findAllByAutores(nombre);
  }
   @Transactional
      public Libro findById(Libro libro) {
    Optional<Libro> optional = libroRepositorio.findById(libro.getId());
    if (optional.isPresent()) {
      libro = optional.get();
    }
    return libro;
  }

    
     public void bajaLibro (String id) throws ErrorServicio{
          Optional<Libro>respuesta=libroRepositorio.findById(id);
        if(respuesta.isPresent()){
            Libro libro=respuesta.get();
            libro.setAlta(false);
        libroRepositorio.save(libro);
        }else{
            throw new ErrorServicio("No se encuentra el libro con ese identificador");     }
        
       
     }
       @Transactional
  public void delete(Libro libro) {
   libroRepositorio.delete(libro);
  }

  
  @Transactional
  public void deleteById(String id) {
    Optional<Libro> optional = libroRepositorio.findById(id);
    if (optional.isPresent()) {
      libroRepositorio.delete(optional.get());
      
    }

  }
   public void validar(String titulo,Long isbn,Integer anio,Integer ejemplares) throws ErrorServicio{
       if(titulo==null || titulo.isEmpty()){
           throw new ErrorServicio ("el nombre no puede ser nulo");
       }
            if (isbn == null) {
                throw new ErrorServicio("Debe indicar el ISBN");
            }
            if(anio==null){
           throw new ErrorServicio ("el año no puede ser nulo");
       }
            if(ejemplares==null){
           throw new ErrorServicio ("el numero de ejemplares no puede ser nulo");
       }
      
    }
     
}
