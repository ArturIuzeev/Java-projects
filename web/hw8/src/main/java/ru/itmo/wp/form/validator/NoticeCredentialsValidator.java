//package ru.itmo.wp.form.validator;
//
//import org.springframework.validation.Errors;
//import ru.itmo.wp.form.UserCredentials;
//import ru.itmo.wp.service.NoticeService;
//import ru.itmo.wp.service.UserService;
//
//public class NoticeCredentialsValidator {
//
//    private final NoticeService noticeService;
//
//    public NoticeCredentialsValidator(NoticeService noticeService) {
//        this.noticeService = noticeService;
//    }
//
//    public boolean supports(Class<?> clazz) {
//        return NoticeCredentialsValidator.class.equals(clazz);
//    }
//
//    public void validate(Object target, Errors errors) {
//        if (!errors.hasErrors()) {
//            NoticeCredentialsValidator text = (NoticeCredentialsValidator) target;
//            if (!noticeService.isLoginFree(registerForm.getLogin())) {
//                errors.rejectValue("login", "login.is-in-use", "Login is in use already");
//            }
//        }
//    }
//}
