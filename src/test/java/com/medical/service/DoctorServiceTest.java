package com.medical.service;


import com.medical.model.Doctor;
import com.medical.model.User;
import com.medical.repository.DoctorRepository;
import com.medical.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.verify;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class DoctorServiceTest {

    @InjectMocks
    private DoctorService underTest;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FileService fileService;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void canRegisterDoctor(){

        //given
        PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();

        MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());

        Doctor doctor=new Doctor();
        User user=new User();
        user.setUsername("admintext");
        user.setPassword(passwordEncoder.encode("12345678"));
        doctor.setUser(user);
        doctor.setImage(doctor.getUser().getUsername());
        doctor.setExperience(4.5f);

        given(fileService.StoreFile(any(),anyString()))
                .willReturn(doctor.getUser().getUsername());

        //when
        underTest.registerDoctor(doctor,firstFile);

        //then
        ArgumentCaptor<Doctor> studentArgumentCaptor=
                ArgumentCaptor.forClass(Doctor.class);
        verify(doctorRepository)
                .save(studentArgumentCaptor.capture());
        Doctor capturedStudent= studentArgumentCaptor.getValue();
        assertThat(capturedStudent).isEqualTo(doctor);
    }

}
