package ssodam.ssodam.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ssodam.ssodam.domain.Member;
import ssodam.ssodam.service.MemberService;

@Controller
@RequiredArgsConstructor
public class MyPageController {

    private final MemberService memberService;

    @GetMapping("/me")
    public String myPageHome(Model model, @AuthenticationPrincipal Member currentMember) {
        MemberForm memberForm = new MemberForm();
        memberForm.setName(currentMember.getUsername());
        model.addAttribute("memberForm", memberForm);
        return "mypage/me";
    }

    @PostMapping("/me")
    public String userEdit(MemberForm form, BindingResult result, @AuthenticationPrincipal Member currentMember) {
        if(result.hasErrors()) {
            return "redirect:/me";
        }
        memberService.updateName(currentMember.getUsername(), form.getName());
        currentMember.setUsername(form.getName());

        return "redirect:/me";
    }

    @GetMapping("/password")
    public String passwordView(Model model) {
        model.addAttribute("passwordForm", new PasswordForm());
        return "mypage/password";
    }

    @PostMapping("/password")
    public String passwordEdit(Model model,
                               PasswordForm form,
                               BindingResult result,
                               @AuthenticationPrincipal Member currentMember) {
        if (result.hasErrors()) {
            return "redirect:/password";
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (!encoder.matches(form.getPassword(), currentMember.getPassword())) {
            model.addAttribute("error", "현재 패스워드 불일치");
            return "mypage/passwordError";
        }
        if (form.getNewPassword().equals(form.getRetype())) {
            model.addAttribute("error", "새 패스워드 불일치");
            return "mypage/passwordError";
        }
        
        String encodedNewPwd = encoder.encode(form.getNewPassword());
        memberService.updatePassword(currentMember.getUsername(), encodedNewPwd);
        currentMember.setPassword(encodedNewPwd);
        return "redirect:/me";
    }

}
