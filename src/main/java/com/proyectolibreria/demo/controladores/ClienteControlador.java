
package com.proyectolibreria.demo.controladores;


import com.proyectolibreria.demo.entidades.Cliente;
import com.proyectolibreria.demo.errores.ErrorServicio;
import com.proyectolibreria.demo.servicios.ClienteServicio;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cliente")
public class ClienteControlador {
    
    @Autowired
    private ClienteServicio clienteServicio;
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO')")
    @GetMapping("/editar-perfil")
    public String editarPerfil(HttpSession session, @RequestParam String id, ModelMap model) {

        Cliente login = (Cliente) session.getAttribute("usuariosession");
        if (login == null || !login.getId().equals(id)) {
            return "redirect:/inicio";
        }

        try {
            Cliente cliente = clienteServicio.buscarPorId(id);
            model.addAttribute("perfil", cliente);
        } catch (ErrorServicio e) {
            model.addAttribute("error", e.getMessage());
        }
        return "perfil.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @PostMapping("/actualizar-perfil")
    public String registrar(ModelMap modelo, HttpSession session, MultipartFile archivo, @RequestParam String id, @RequestParam Long documento, @RequestParam String nombre, @RequestParam String apellido,@RequestParam Long telefono, @RequestParam String mail, @RequestParam String clave, @RequestParam String clave2 ) {

       Cliente cliente = null;
        try {

            Cliente login = (Cliente) session.getAttribute("usuariosession");
            if (login == null || !login.getId().equals(id)) {
                return "redirect:/inicio";
            }

            cliente =clienteServicio.buscarPorId(id);
            clienteServicio.modificar(archivo, id, documento,nombre, apellido,telefono, mail, clave, clave2);
            session.setAttribute("usuariosession", cliente);

            return "redirect:/inicio";
        } catch (ErrorServicio ex) {
         
            modelo.put("error", ex.getMessage());
            modelo.put("perfil", cliente);

            return "perfil.html";
        }

    }

    
        @GetMapping("/lista")
  public String listarPersonas(Model model) {

      model.addAttribute("clientes", clienteServicio.listarTodos());
    

    return "cliente-list";
  }
    
      
  @GetMapping("/baja/{id}")
	public String baja(@PathVariable String id) {
				
		try {
			clienteServicio.deshabilitar(id);
			return "redirect:/cliente/lista";
		} catch (Exception e) {
			return "redirect:/";
		}
		
	}


}
