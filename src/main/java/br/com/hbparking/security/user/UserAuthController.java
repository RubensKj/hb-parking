package br.com.hbparking.security.user;

import br.com.hbparking.security.jwt.JwtProvider;
import br.com.hbparking.security.jwt.JwtResponse;
import br.com.hbparking.security.role.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@CrossOrigin(origins = {"http://localhost:4200", "http://192.168.33.216:4200", "https://192.168.33.216:4200"})
@RestController
@RequestMapping("/api/auth")
public class UserAuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final RoleService roleService;
    private final JwtProvider jwtProvider;

    @Autowired
    public UserAuthController(AuthenticationManager authenticationManager, UserService userService, RoleService roleService, JwtProvider jwtProvider) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.roleService = roleService;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authUserInApplication(@Valid @RequestBody UserDTO userDTO) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return ResponseEntity.ok(new JwtResponse(jwtProvider.generateJWTTokenFromAuthentitation(authentication)));
    }

    @PostMapping("/logout")
    @PreAuthorize("hasRole('USER') or hasRole('GESTOR') or hasRole('SISTEMA')")
    public ResponseEntity<String> disAuthenticateUser(HttpServletRequest request) {
        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
        try {
            if (request.getHeader("Authorization") != null) {
                request.removeAttribute("Authorization");
                request.logout();
            }
        } catch (ServletException e) {
            return new ResponseEntity<>("Não foi possivel deslogar da aplicação", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>("Deslogado com sucesso!", HttpStatus.OK);
    }

    @PostMapping("/create")
    public UserDTO createUser(@Valid @RequestBody UserDTO userDTO) {
        userDTO.setPassword(this.userService.encryptUserDTOPassword(userDTO.getPassword()));
        User user = this.userService.save(userDTO);
        return UserDTO.of(user);
    }
}
