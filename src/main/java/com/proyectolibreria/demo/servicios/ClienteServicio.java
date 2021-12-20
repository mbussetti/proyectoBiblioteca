
package com.proyectolibreria.demo.servicios;

import com.proyectolibreria.demo.entidades.Cliente;
import com.proyectolibreria.demo.entidades.Foto;
import com.proyectolibreria.demo.enums.Rol;
import com.proyectolibreria.demo.errores.ErrorServicio;
import com.proyectolibreria.demo.repositorios.ClienteRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ClienteServicio implements UserDetailsService {
  
    @Autowired    
  private ClienteRepositorio clienteRepositorio;
    
    @Autowired
    private FotoServicio fotoServicio;
   
    
  @Transactional

  public Cliente registrar(MultipartFile archivo, Long documento,String nombre,String apellido,Long telefono,String mail,String clave,String clave2) throws ErrorServicio{
     
      validar(documento,nombre,apellido,telefono,mail,clave,clave2);
      
       Cliente cliente = new Cliente();

            cliente.setDocumento(documento);
            cliente.setNombre(nombre);
            cliente.setApellido(apellido);
            cliente.setTelefono(telefono);
            cliente.setMail(mail);
           
        String encriptada = new BCryptPasswordEncoder().encode(clave);
        cliente.setClave(encriptada);
        
        cliente.setAlta(new Date());
            
        Foto foto=fotoServicio.guardar(archivo);
        cliente.setFoto(foto);
        
        cliente.setRol(Rol.USUARIO);
            
            
      return clienteRepositorio.save(cliente);
  }
  
  @Transactional
  public void validar(Long documento,String nombre,String apellido,Long telefono,String mail,String clave,String clave2) throws ErrorServicio {
     if (documento == null) {
      throw new ErrorServicio(" El documento no puede estar vacío");
    }
      
     if (nombre.isEmpty()  ) {
      throw new ErrorServicio(" El nombre no puede estar vacío");
    }
      if (apellido.isEmpty()  ) {
      throw new ErrorServicio(" El apellido no puede estar vacío");
    }
    if (telefono== null) {
      throw new ErrorServicio(" El telefono no puede estar vacío");
    }
    if (mail == null || mail.isEmpty()) {
            throw new ErrorServicio("El mail no puede ser nulo");
        }

    if (clave== null || clave.isEmpty() || clave.length() <= 6) {
            throw new ErrorServicio("La clave del usuario no puede ser nula y tiene que tener mas de seis digitos");
        }
    if (!clave.equals(clave2)) {
            throw new ErrorServicio("Las claves deben ser iguales");
        }
  }
  
  @Transactional
    public void modificar(MultipartFile archivo,String id, Long documento,String nombre,String apellido,Long telefono,String mail,String clave,String clave2) throws ErrorServicio {

        
        validar(documento,nombre,apellido,telefono,mail,clave,clave2);

        Optional<Cliente> respuesta = clienteRepositorio.findById(id);
        if (respuesta.isPresent()) {

            Cliente cliente = respuesta.get();
            cliente.setDocumento(documento);
            cliente.setNombre(nombre);
            cliente.setApellido(apellido);
            cliente.setTelefono(telefono);
            cliente.setMail(mail);
            String encriptada = new BCryptPasswordEncoder().encode(clave);
            cliente.setClave(encriptada);

            String idFoto = null;
            if (cliente.getFoto() != null) {
                idFoto = cliente.getFoto().getId();
            }

            Foto foto = fotoServicio.actualizar(idFoto, archivo);
            cliente.setFoto(foto);

            clienteRepositorio.save(cliente);
        } else {

            throw new ErrorServicio("No se encontró el usuario solicitado");
        }

    }
    
    @Transactional
    public void deshabilitar(String id) throws ErrorServicio {

        Optional<Cliente> respuesta = clienteRepositorio.findById(id);
        if (respuesta.isPresent()) {

            Cliente cliente = respuesta.get();
            cliente.setBaja(new Date());
            clienteRepositorio.save(cliente);
        } else {

            throw new ErrorServicio("No se encontró el usuario solicitado");
        }

    }

    @Transactional
    public void habilitar(String id) throws ErrorServicio {

        Optional<Cliente> respuesta = clienteRepositorio.findById(id);
        if (respuesta.isPresent()) {

            Cliente cliente = respuesta.get();
            cliente.setBaja(null);
            clienteRepositorio.save(cliente);
        } else {

            throw new ErrorServicio("No se encontró el usuario solicitado");
        }

    }
    
    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        Cliente cliente = clienteRepositorio.buscarPorMail(mail);
        if (cliente != null) {
            List<GrantedAuthority> permisos = new ArrayList<>();
                        
            GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_"+ cliente.getRol());
            permisos.add(p1);
         
            //Esto me permite guardar el OBJETO USUARIO LOG, para luego ser utilizado
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", cliente);

            User user = new User(cliente.getMail(), cliente.getClave(), permisos);
            return user;

        } else {
            return null;
        }

    }
   

@Transactional(readOnly = true)
	public List<Cliente> listarTodos() {
		return clienteRepositorio.findAll();
	}

   @Transactional
    public void cambiarRol(String id) throws ErrorServicio{
    
    	Optional<Cliente> respuesta = clienteRepositorio.findById(id);
    	
    	if(respuesta.isPresent()) {
    		
    		Cliente cliente = respuesta.get();
    		
    		if(cliente.getRol().equals(Rol.USUARIO)) {
    			
    		cliente.setRol(Rol.ADMIN);
    		
    		}else if(cliente.getRol().equals(Rol.ADMIN)) {
    			cliente.setRol(Rol.USUARIO);
    		}
    	}
    }  
    
    @Transactional
      public Cliente findById(Cliente cliente) {
    Optional<Cliente> optional = clienteRepositorio.findById(cliente.getId());
    if (optional.isPresent()) {
      cliente = optional.get();
    }
    return cliente;
  }

     @Transactional
  public Optional<Cliente> findById(String id) {
    return clienteRepositorio.findById(id);
  }

   @Transactional(readOnly=true)
    public Cliente buscarPorId(String id) throws ErrorServicio {

        Optional<Cliente> respuesta = clienteRepositorio.findById(id);
        if (respuesta.isPresent()) {

           Cliente cliente = respuesta.get();
            return cliente;
        } else {

            throw new ErrorServicio("No se encontró el usuario solicitado");
        }

    }

}
