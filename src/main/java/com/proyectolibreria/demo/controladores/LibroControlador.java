
package com.proyectolibreria.demo.controladores;

import com.proyectolibreria.demo.entidades.Libro;
import com.proyectolibreria.demo.servicios.AutorServicio;
import com.proyectolibreria.demo.servicios.EditorialServicio;
import com.proyectolibreria.demo.servicios.LibroServicio;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/libro")
public class LibroControlador {
  @Autowired
  private LibroServicio libroServicio;

  @Autowired
  private AutorServicio autorServicio;
  
    @Autowired
  private EditorialServicio editorialServicio;
  
  @GetMapping("/list")
  public String listarLibros(Model model, @RequestParam(required = false) String q) {
    if (q != null) {
      model.addAttribute("libros", libroServicio.listAllByQ(q));
    } else {
      model.addAttribute("libros", libroServicio.listAll());
    }

    return "libros-lista";
  }
  
  
  @GetMapping("/form")
  public String crearLibro(Model model) {
      
   
    
    
   model.addAttribute("autores", autorServicio.listAll());
  
   model.addAttribute("editoriales", editorialServicio.listAll());
   
   return "libro-formulario";
  }

  @PostMapping("/save")
  public String guardarLibro(Model modelo,RedirectAttributes redirectAttributes,MultipartFile archivo,@RequestParam Long isbn, @RequestParam String titulo, @RequestParam Integer anio,@RequestParam String idAutor, @RequestParam String idEditorial, @RequestParam Integer ejemplares, @RequestParam String sinapsis ) {
    try {
      libroServicio.guardar1(archivo, isbn, titulo, anio, ejemplares, idAutor, idEditorial, sinapsis);
      redirectAttributes.addFlashAttribute("EXITO", "Libro guardado con éxito");
    } catch (Exception e) {
       modelo.addAttribute("error", e.getMessage());
            modelo.addAttribute("isbn", isbn);
            modelo.addAttribute("titulo", titulo);
            modelo.addAttribute("anio", anio);
            modelo.addAttribute("ejemplares", ejemplares);
            
            modelo.addAttribute("sinapsis", sinapsis);
       
    }
    return "redirect:/libro/list";
  }
   @GetMapping("/delete")
  public String eliminarLibro(@RequestParam(required = true) String id) {
    libroServicio.deleteById(id);
    return "redirect:/libro/list";
  }

}
