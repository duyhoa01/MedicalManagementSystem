package com.medical.repository;

import com.medical.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository underTest;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }

    @Test
    void itShouldCheckIfStudentSelectExistsEmail(){
        //givenf
        String userName="user1";
        User user=new User();
        underTest.save(user);
        //when
        Boolean expected=underTest.selectExistsUserName(userName);
        //then
        assertThat(expected).isTrue();
    }

    @Test
    void itShouldCheckWhenStudentSelectEmailDoesNotExists(){
        //givenf
        String userName="user1";
        //when
        Boolean expected=underTest.selectExistsUserName(userName);

        //then
        assertThat(expected).isFalse();
    }

}
