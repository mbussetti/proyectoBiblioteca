
package com.proyectolibreria.demo.servicios;



import com.proyectolibreria.demo.entidades.Cliente;
import com.proyectolibreria.demo.entidades.Libro;
import com.proyectolibreria.demo.entidades.Prestamo;
import com.proyectolibreria.demo.errores.ErrorServicio;
import com.proyectolibreria.demo.repositorios.ClienteRepositorio;
import com.proyectolibreria.demo.repositorios.LibroRepositorio;
import com.proyectolibreria.demo.repositorios.PrestamoRepositorio;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PrestamoServicio {

    @Autowired
    private PrestamoRepositorio prestamoRepositorio;

    @Autowired
    private ClienteRepositorio clienteRepositorio;
    @Autowired
    private ClienteServicio clienteServicio;

    @Autowired
    private LibroServicio libroServicio;

    @Autowired
    private LibroRepositorio libroRepositorio;

    public Prestamo crearPrestamo(String id, Date fechaPrestamo,Date fechaDevolucion,Cliente cliente,String idLibro) throws ErrorServicio {

        validar(fechaDevolucion,cliente,idLibro);
       
           
            Prestamo prestamo = new Prestamo();
            prestamo.setId(id);
            prestamo.setFechaPrestamo(fechaPrestamo);
            prestamo.setFechaDevolucion(fechaDevolucion);
            prestamo.setAlta(true);
            prestamo.setCliente(cliente);
            
         
            Libro libro=libroRepositorio.getById(idLibro);
            prestamo.setLibro(libro);
        
       
      return prestamoRepositorio.save(prestamo);
   
    }
   
    
    @Transactional
  public Prestamo guardar(Prestamo prestamo) throws ErrorServicio {
    
         if (prestamo.getFechaDevolucion() == null) {
            throw new ErrorServicio(" La fecha de devolucion no puede estar vacía");
        }
      
        if (prestamo.getCliente()== null) {
            throw new ErrorServicio(" El nombre del cliente  no puede estar vacío");
        }else {
            prestamo.setCliente(clienteServicio.findById(prestamo.getCliente()));
        }
      
    if (prestamo.getLibro() == null) {
      throw new ErrorServicio(" El libro no puede ser nulo");
    } else {
      prestamo.setLibro(libroServicio.findById(prestamo.getLibro()));
              }
    
    return prestamoRepositorio.save(prestamo);
  }
          
    @Transactional
    public void bajaPrestamo(String id) throws ErrorServicio {
        
        Optional<Prestamo> respuesta = prestamoRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Prestamo prestamo = respuesta.get();
               if(prestamo.getFechaDevolucion().equals(new Date())){
               
                   prestamoRepositorio.delete(prestamo);
            } else {
                throw new ErrorServicio("La fecha actual no coincide con la fecha de devolucion");
            }
        }else{
          throw new ErrorServicio("No hay prestamos registrados con esa identificacion");

        }
       

    }
   public Optional<Prestamo> findById(String id) {
    return prestamoRepositorio.findById(id);
  }
            
    @Transactional
    public List<Prestamo> listAll() {

        return prestamoRepositorio.findAll();
    }
    
    
  @Transactional
   public void modificarPrestamo( String id,Date fechaDevolucion) throws ErrorServicio {

      

        Optional<Prestamo> respuesta = prestamoRepositorio.findById(id);
        if (respuesta.isPresent()) {

            Prestamo prestamo = respuesta.get();
            prestamo.setFechaDevolucion(fechaDevolucion);
           
            prestamoRepositorio.save(prestamo);
        } else {

            throw new ErrorServicio("No se encontró el prestamo solicitado");
        }

    }
      

 

    public void validar (Date fechaDevolucion, 
            Cliente cliente, String idlibro) throws ErrorServicio {

        
        if (fechaDevolucion == null) {
            throw new ErrorServicio(" La fecha de devolucion no puede estar vacía");
        }
      
        if (cliente == null) {
            throw new ErrorServicio(" El nombre del cliente  no puede estar vacío");
        }
        if (idlibro.isEmpty()) {
            throw new ErrorServicio(" El nombre del libro no puede estar vacío");
        }

    }
}
