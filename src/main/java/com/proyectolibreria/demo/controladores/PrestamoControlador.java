
package com.proyectolibreria.demo.controladores;


import com.proyectolibreria.demo.entidades.Cliente;
import com.proyectolibreria.demo.entidades.Libro;
import com.proyectolibreria.demo.entidades.Prestamo;
import com.proyectolibreria.demo.errores.ErrorServicio;
import com.proyectolibreria.demo.repositorios.LibroRepositorio;
import com.proyectolibreria.demo.servicios.ClienteServicio;
import com.proyectolibreria.demo.servicios.LibroServicio;
import com.proyectolibreria.demo.servicios.PrestamoServicio;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/prestamo")
public class PrestamoControlador {
    @Autowired
    private PrestamoServicio prestamoServicio;
    @Autowired
    private ClienteServicio clienteServicio;
     @Autowired
    private LibroServicio libroServicio;
    
     @Autowired
    private LibroRepositorio libroRepositorio;
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USUARIO')")
    @GetMapping("/form")
  public String crearPrestamo(HttpSession session,ModelMap modelo,@RequestParam String id) throws ErrorServicio {

    
      Cliente login = (Cliente) session.getAttribute("usuariosession");
        if (login == null || !login.getId().equals(id)) {
            return "redirect:/inicio";
        }
       try {
            Cliente cliente = clienteServicio.buscarPorId(id);
            modelo.addAttribute("cliente", cliente);
          
        } catch (ErrorServicio e) {
            modelo.addAttribute("error", e.getMessage());
        }
      
        return "prestamo-form.html";
       
  }
@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USUARIO')")
  @PostMapping("/save")
  public String guardarPrestamo(ModelMap modelo,HttpSession session,@RequestParam String id,@RequestParam Date fechaPrestamo,@RequestParam Date fechaDevolucion ,@RequestParam Cliente cliente,@RequestParam String idLibro) throws ErrorServicio {
  
  try {

         prestamoServicio.crearPrestamo(id,fechaPrestamo,fechaDevolucion,cliente, idLibro);
        } catch (ErrorServicio ex) {
            
            modelo.put("error", ex.getMessage());
            modelo.put("fechaPrestamo", fechaPrestamo);
            modelo.put("fechaDevolucion", fechaDevolucion);
            modelo.put("cliente", cliente);
             List<Libro> libros = libroRepositorio.findAll();
            modelo.put("libros", libros);
            modelo.put("idLibro", idLibro);
            
            
            return "prestamo-form.html";
        }
        modelo.put("titulo", "Prestamo Generado con Exito");
        modelo.put("descripcion", "Ahora puedes comenzar a disfrutar de tu libro!");
        return "exito.html";

         

  } 
  
  
   @GetMapping("/list")
	public String lista(ModelMap modelo) {
		
		List<Prestamo> todos =prestamoServicio.listAll();
		
		modelo.addAttribute("prestamos", todos);
		
		return "prestamo-list";
	}
        
}

