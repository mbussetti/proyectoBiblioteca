
package com.proyectolibreria.demo.controladores;


import com.proyectolibreria.demo.entidades.Cliente;
import com.proyectolibreria.demo.errores.ErrorServicio;
import com.proyectolibreria.demo.servicios.ClienteServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/")
public class PortalControlador {
    
    @Autowired
    private ClienteServicio clienteServicio;
      
    
@GetMapping("/")
public String index(ModelMap modelo){
    List <Cliente> clientesActivos=clienteServicio.listarTodos();
    
    modelo.addAttribute("clientes",clientesActivos);
    
return "index.html";
} 

@PreAuthorize("hasAnyRole('ROLE_USUARIO')")
@GetMapping("/inicio")
public String inicio(){
    return "inicio.html";
}


@GetMapping("/login")
    public String login(@RequestParam(required = false) String error, @RequestParam(required = false) String logout, ModelMap model) {
        if (error != null) {
            model.put("error", "Usuario o clave incorrectos");
        }
        if (logout != null) {
            model.put("logout", "Ha salido correctamente.");
        }
        return "login-cliente.html";
    }
    
@GetMapping("/registro")
    public String registro(ModelMap modelo) {
       
        return "registro-cliente.html";
    }
 
@PostMapping("/registrar")
    public String registrar(ModelMap modelo, MultipartFile archivo,@RequestParam(required = false) Long documento, @RequestParam String nombre, @RequestParam String apellido,@RequestParam(required = false) Long telefono, @RequestParam String mail, @RequestParam String clave1, @RequestParam String clave2 ) {

        try {
            clienteServicio.registrar(archivo,documento, nombre, apellido,telefono, mail, clave1, clave2);
        } catch (ErrorServicio ex) {
            
            modelo.put("error", ex.getMessage());
            modelo.put("documento", documento);
            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            modelo.put("telefono", telefono);
            modelo.put("mail", mail);
            modelo.put("clave1", clave1);
            modelo.put("clave2", clave2);
            
            return "registro-cliente.html";
        }
        modelo.put("titulo", "Bienvenido a BookZone");
        modelo.put("descripcion", "Tu usuario fue registrado de manera satisfactoria");
        return "exito.html";
    }
}
