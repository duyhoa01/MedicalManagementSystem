package com.medical;

import com.cloudinary.Cloudinary;
import com.medical.mapper.DoctorMapper;
import com.medical.mapper.PatientMapper;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class MedicalManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(MedicalManagementSystemApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE");
            }
        };
    }

    @Bean
    public ModelMapper modelMapper() {
        // Tạo object và cấu hình
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }

    @Bean
    public Cloudinary cloudinaryConfig() {
        Cloudinary cloudinary = null;
        Map config = new HashMap();
        config.put("cloud_name", "drotiisfy");
        config.put("api_key", "473216843688577");
        config.put("api_secret", "2RaoEKTrTLFkVXeBEDKSFBaaMqg");
        cloudinary = new Cloudinary(config);
        return cloudinary;
    }

    @Bean
    public PagedResourcesAssembler<?> pagedResourcesAssembler(){
        HateoasPageableHandlerMethodArgumentResolver resolver = new HateoasPageableHandlerMethodArgumentResolver();
        PagedResourcesAssembler<?> assembler = new PagedResourcesAssembler<>(resolver, null);
        return assembler;
    }


    @Bean
    public PatientMapper patientMapper(){
        return new PatientMapper();
    }

    @Bean
    public DoctorMapper doctorMapper(){return new DoctorMapper();}

}
