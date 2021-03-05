package uz.pdp.springbootfileuploaddownloaddemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import uz.pdp.springbootfileuploaddownloaddemo.component.Generator;
import uz.pdp.springbootfileuploaddownloaddemo.config.Config;
import uz.pdp.springbootfileuploaddownloaddemo.entity.Temp;
import uz.pdp.springbootfileuploaddownloaddemo.payload.ApiResponse;
import uz.pdp.springbootfileuploaddownloaddemo.repository.TempRepository;

import javax.mail.internet.MimeMessage;

@Service
public class TempService {


    @Autowired
    private Config config;

    @Autowired
    private TempRepository tempRepository;

    @Autowired
    private Generator generator;


    public boolean existsByEmail(String email){
        return tempRepository.existsByEmail(email);
    }

    public ApiResponse check_active(String email,String code_string, String code_html){

        System.out.println(email);
        System.out.println(code_string);
        System.out.println(code_html);

        if (existsByEmail(email)){
            Temp temp=tempRepository.findByEmail(email);
            System.out.println(temp.toString());
            if (temp.getActive() && temp.getCode_string().equals(code_string) && temp.getCode_html().equals(code_html)){
                temp.setActive(false);
                tempRepository.save(temp);
                return new ApiResponse("Success",true);
            }
            else {
                return new ApiResponse("Bu emailning urinishi tugagan! (1 marta fayl yuklash mumkin)",false);
            }
        } return new ApiResponse("Bu emailga kod jo'natilmagan",false);
    }



    public ApiResponse saveAndSend(String email){
        try {

            if (!existsByEmail(email)){
                JavaMailSender mailSender=config.getJavaMailSender();

                String code_string=generator.generate();
                String code_html=generator.generate();

                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setFrom("pdpemailtest@gmail.com");
                mailMessage.setTo(email);
                mailMessage.setSubject("Verification String");
                mailMessage.setText(code_string);



                MimeMessage mimeMessage = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
                String htmlMsg = "<h1>"+ code_html+"</h1>";
                mimeMessage.setContent(htmlMsg, "text/html");
                helper.setText(htmlMsg, true);
                helper.setSubject("Verification HTML");
                helper.setFrom("pdpemailtest@gmail.com");
                helper.setTo(email);


                Temp temp=new Temp();

                temp.setCode_string(code_string);
                temp.setCode_html(code_html);
                temp.setEmail(email);

                mailSender.send(mailMessage);
                mailSender.send(mimeMessage);
                temp.setActive(true);
                tempRepository.save(temp);
                return new ApiResponse("Jo'natildi",true);

            } else {
                return new ApiResponse("Bu emailga verifikatsiya kodi jo'natilgan",false);
            }


        }catch (Exception e){
            return new ApiResponse("Error",false);
        }

    }
}
