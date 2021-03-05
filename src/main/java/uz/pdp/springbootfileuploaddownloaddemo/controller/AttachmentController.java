package uz.pdp.springbootfileuploaddownloaddemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uz.pdp.springbootfileuploaddownloaddemo.payload.ApiResponse;
import uz.pdp.springbootfileuploaddownloaddemo.repository.TempRepository;
import uz.pdp.springbootfileuploaddownloaddemo.service.AttachmentService;
import uz.pdp.springbootfileuploaddownloaddemo.service.TempService;

import java.net.MalformedURLException;
import java.util.UUID;

@RestController
@RequestMapping("/api/attachment")
public class AttachmentController {
    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private TempService tempService;

    @PostMapping("/auth_save")
    public HttpEntity<?> dbSave(MultipartHttpServletRequest file,
                                @RequestParam("email") String email,
                                @RequestParam("code_string") String code_string,
                                @RequestParam("code_html") String code_html,
                                @RequestParam("dors") String dors)
    {

        ApiResponse apiResponse=tempService.check_active(email, code_string, code_html);
        if (apiResponse.isSuccess()){
            if (dors.equals("1")){
                return dbSave(file,email);
            } else {
                return serverSave(file,email);
            }
        }
        return ResponseEntity.status(409).body(apiResponse);


    }


    @PostMapping("/dbSave")
    public HttpEntity<?> dbSave(MultipartHttpServletRequest request, String email){
        ApiResponse apiResponse=attachmentService.dbSave(request,email);
        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
    }

    @PostMapping("/serverSave")
    public HttpEntity<?> serverSave(MultipartHttpServletRequest request, String email){
        ApiResponse apiResponse=attachmentService.serverSave(request, email);
        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
    }

    @GetMapping("/fromDBOrServer/{id}")
    public HttpEntity<?> getFromDBOrServer(@PathVariable("id")UUID uuid) throws MalformedURLException {
        return attachmentService.getFromDBOrServer(uuid);
    }

    @GetMapping("/fromServer/{id}")
    public HttpEntity<?> fromServer(@PathVariable("id")UUID uuid){
        return null;
    }

    @GetMapping("/all")
    public ResponseEntity all(){
        return ResponseEntity.ok(attachmentService.findAll());
    }
}
