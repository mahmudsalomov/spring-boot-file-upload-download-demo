package uz.pdp.springbootfileuploaddownloaddemo.component;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.SecureRandom;

@Component
public class Generator {


    public String generate(){
        SecureRandom random = new SecureRandom();
        return new BigInteger(30, random).toString(32).toUpperCase();
    }

}
