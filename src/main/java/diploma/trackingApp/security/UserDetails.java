package diploma.trackingApp.security;

//принято использовать специальный класс, класс-обёртку для сущности, чтобы удобнее работать
//и не работать сразу с сущностью. Реализует специальный интерфейс. Стандартные методы используются
//чтобы Спринг Секьюрити нормально работал. ДЛЯ СТАНДАРТА!
import diploma.trackingApp.models.Role;
import diploma.trackingApp.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;

public class UserDetails implements org.springframework.security.core.userdetails.UserDetails {

    private final User user;

    @Autowired
    public UserDetails(User user) {
        this.user = user;
    }

    //нужен для авторизации(ДЛЯ РОЛЕЙ И ДОСТУПА, ПРАВ, КОТОРЫЕ ЕСТЬ У ПОЛЬЗОВАТЕЛЯ)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
       /* String role = user.getRoles().stream().findFirst().orElse("ROLE_ADMIN");
        //Возвращаем роль пользователя(может быть ADMIN, STUDENT, WORKER)
        return Collections.singleton(new SimpleGrantedAuthority(role));*/
        // Получаем первую роль пользователя из Set или возвращаем ROLE_ADMIN, если Set пустой
        Role role = user.getRoles().stream().findFirst().orElse(Role.ROLE_ADMIN);

        // Создаем и возвращаем объект SimpleGrantedAuthority с ролью пользователя
        return Collections.singleton(new SimpleGrantedAuthority(role.toString()));
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    //нужно, чтобы получать данные аутентифицированного пользователя
    public User getUser(){
        return this.user;
    }
}
