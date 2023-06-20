package pt.isec.tp_pd.data.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import pt.isec.tp_pd.data.controllers.UtilizadorController;
import pt.isec.tp_pd.data.models.Utilizador;
import pt.isec.tp_pd.data.repository.UtilizadorRepository;
import pt.isec.tp_pd.data.service.UtilizadorService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Component
public class UserAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UtilizadorRepository repo;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        List<Utilizador> utilizadors=null;

        System.out.println("user:"+username);
        if (username.equals("admin") && password.equals("admin")) {
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ADMIN"));
            return new UsernamePasswordAuthenticationToken(username, password, authorities);
        }else if((utilizadors=repo.findByUsernameWhere(username,password))!=null){
            System.out.println(utilizadors.toString());
            if(utilizadors.size()!=0) {
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("CLIENT"));
                return new UsernamePasswordAuthenticationToken(username, password, authorities);
            }else{
                throw new BadCredentialsException("Bad Credentials");
            }
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
