package diploma.trackingApp.util;

import diploma.trackingApp.models.User;
import diploma.trackingApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    private final UserService userService;

    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;


        // Проверяем, что пользователь с такой электронной почтой еще не зарегистрирован
        if (userService.findByEmail(user.getEmail()).isPresent()) {
            errors.rejectValue("email", "", "Пользователь с такой электронной почтой уже зарегистрирован");
        }

        // Проверяем, что пользователь с таким паролем еще не зарегистрирован
        if (userService.findByPassword(user.getPassword()).isPresent()) {
            errors.rejectValue("password", "", "Пользователь с паролем уже зарегистрирован");
        }
    }
}
