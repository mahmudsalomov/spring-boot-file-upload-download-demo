package uz.pdp.springbootfileuploaddownloaddemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springbootfileuploaddownloaddemo.component.Generator;
import uz.pdp.springbootfileuploaddownloaddemo.config.Config;
import uz.pdp.springbootfileuploaddownloaddemo.payload.ApiResponse;
import uz.pdp.springbootfileuploaddownloaddemo.service.TempService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@RestController
@RequestMapping("/api/mail")
public class EmailController {

    @Autowired
    private Config config;

    @Autowired
    private TempService tempService;

    @Autowired
    private Generator generator;
    @GetMapping("/send")
    public HttpEntity<?> send(@RequestParam String email) throws MessagingException {
        ApiResponse apiResponse=tempService.saveAndSend(email);
        return ResponseEntity.status(200).body(apiResponse);
    }

}
