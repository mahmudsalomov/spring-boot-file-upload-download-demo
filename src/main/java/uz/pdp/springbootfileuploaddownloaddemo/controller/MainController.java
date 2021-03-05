package uz.pdp.springbootfileuploaddownloaddemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import uz.pdp.springbootfileuploaddownloaddemo.service.AttachmentService;

@Controller
public class MainController {

    @Autowired
    private AttachmentService attachmentService;

    @GetMapping("/")
    public String main(Model model){
        model.addAttribute("images", attachmentService.findAll());
        return "main";
    }

    @GetMapping("/generator")
    public String generator(){
        return "generator";
    }
}
