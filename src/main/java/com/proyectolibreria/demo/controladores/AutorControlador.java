
package com.proyectolibreria.demo.controladores;


import com.proyectolibreria.demo.entidades.Autor;
import com.proyectolibreria.demo.errores.ErrorServicio;
import com.proyectolibreria.demo.servicios.AutorServicio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/autor")
public class AutorControlador {
    
    @Autowired
    private AutorServicio autorServicio;
    
    @GetMapping("/lista")
	public String lista(ModelMap modelo) {
		
		List<Autor> todos =autorServicio.listAll();
		
		modelo.addAttribute("autores", todos);
		
		return "autor-list";
	}
        
         @GetMapping("/form")
  public String editarAutor(Model model, @RequestParam(required = false) String id) {
    if (id != null) {
      Optional<Autor> optional = autorServicio.findById(id);
      if (optional.isPresent()) {
        model.addAttribute("autor", optional.get());
      } else {
        return "redirect:/autor/lista";
      }
    } else {
      model.addAttribute("autor", new Autor());
    }
    
    return "autor-form";
  }
    
    @GetMapping("/registro")
    public String formulario(){
        return "autor-form";
    }
    @PostMapping("/registro")
    public String guardar(ModelMap modelo, @RequestParam String nombre) throws ErrorServicio{
        try{
            autorServicio.guardarAutor(nombre);
            modelo.put("EXITO", "Autor guardado con exito");
            return "autor-form";
        }catch (ErrorServicio e){
            modelo.put("ERROR", "No se cargo correctamente el autor");
            return "autor-form";
        }
    }
    @GetMapping("/baja/{id}")
	public String baja(@PathVariable String id) {
				
		try {
			autorServicio.bajaAutor(id);
			return "redirect:/autor/lista";
		} catch (Exception e) {
			return "redirect:/";
		}
		
	}
	
	@GetMapping("/alta/{id}")
	public String alta(@PathVariable String id) {
		
		try {
			autorServicio.altaAutor(id);
			return "redirect:/autor/lista";
		} catch (Exception e) {
			return "redirect:/";
		}
	}
	
}