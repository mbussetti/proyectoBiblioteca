
package com.proyectolibreria.demo.controladores;


import com.proyectolibreria.demo.entidades.Editorial;
import com.proyectolibreria.demo.errores.ErrorServicio;
import com.proyectolibreria.demo.servicios.EditorialServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

  @Controller
@RequestMapping("/editorial")

public class EditorialControlador {
  
    @Autowired
    private EditorialServicio editorialServicio;
    
    @GetMapping("/lista")
	public String lista(ModelMap modelo) {
		
		List<Editorial> todos =editorialServicio.listAll();
		
		modelo.addAttribute("editoriales", todos);
		
		return "editorial-list";
	}
    
    @GetMapping("/registro")
    public String formulario(){
        return "editorial-form";
    }
    
    @PostMapping("/registro")
    public String guardar(ModelMap modelo, @RequestParam String nombre) throws ErrorServicio{
        try{
            editorialServicio.guardar(nombre);
            modelo.put("EXITO", "Editorial guardada con exito");
            return "editorial-form";
        }catch (ErrorServicio e){
            modelo.put("ERROR", "No se cargo correctamente la editorial");
            return "editorial-form";
        }
    }
    @GetMapping("/baja/{id}")
	public String baja(@PathVariable String id) {
				
		try {
			editorialServicio.bajaEditorial(id);
			return "redirect:/editorial/lista";
		} catch (Exception e) {
			return "redirect:/";
		}
		
	}
	
	@GetMapping("/alta/{id}")
	public String alta(@PathVariable String id) {
		
		try {
			editorialServicio.altaEditorial(id);
			return "redirect:/editorial/lista";
		} catch (Exception e) {
			return "redirect:/";
		}
	}
	
} 

