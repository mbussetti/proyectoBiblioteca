
package com.proyectolibreria.demo.controladores;

import com.proyectolibreria.demo.entidades.Cliente;
import com.proyectolibreria.demo.entidades.Prestamo;
import com.proyectolibreria.demo.servicios.ClienteServicio;
import com.proyectolibreria.demo.servicios.PrestamoServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;



 @Controller
@RequestMapping("/admin")
public class AdminControlador {

	@Autowired ClienteServicio clienteServicio;
	@Autowired PrestamoServicio prestamoServicio;
        
        
        @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@GetMapping("/dashboard")
	public String inicioAdmin(ModelMap modelo) {
		
		List<Cliente> clientes = clienteServicio.listarTodos();
		
		modelo.put("clientes", clientes);
                
                List<Prestamo> prestamos = prestamoServicio.listAll();
		
		modelo.put("prestamos", prestamos);
		
		return "inicioAdmin.html";
	}
	
	
	
	@GetMapping("/deshabilitar/{id}")
	public String deshabilitar(ModelMap modelo, @PathVariable String id) {
		try {
			clienteServicio.deshabilitar(id);
			return "redirect:/admin/dashboard";
		}catch(Exception e) {
			modelo.put("error", "No fue posible deshabilitar");
			return "inicioAdmin";
		}
	}
	
	@GetMapping("/cambiar_rol/{id}")
	public String cambiarRol(ModelMap modelo, @PathVariable String id) {
		try {
			clienteServicio.cambiarRol(id);
			modelo.put("exito", "Rol modificado con Exito!");
			return "redirect:/admin/dashboard";
		}catch(Exception e) {
			modelo.put("error", "No fue posible reasignar el rol");
			return "inicioAdmin";
		}
	}
	
	  
}
