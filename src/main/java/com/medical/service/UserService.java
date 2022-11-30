package com.medical.service;

import com.medical.dtos.UserResponeDTO;
import com.medical.model.Message;
import com.medical.model.User;
import com.medical.repository.MessageRepository;
import com.medical.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.SendFailedException;
import java.util.*;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if(user==null){
            throw new UsernameNotFoundException("User not found in database");
        }
        return user;
    }

    public User saveUser(User user){
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public User getUserByUserName(String username){
        return userRepository.findByEmail(username);
    }

    public boolean selectExistsUserName(String username){
        return userRepository.selectExistsUserName(username);
    }

    public User addUser(User user) throws SendFailedException {
        Boolean existsUser = userRepository
                .selectExistsUserName(user.getUsername());
        if(existsUser){
            throw new IllegalStateException("người dùng đã tồn tại");
        }
        if(user.getStatus()==false){
            String token = UUID.randomUUID().toString();
            String link = "http://localhost:8080/api/auth/confirm?token=" + token;
            emailService.sendConfimAccount(
                    user.getEmail(),
                    buildEmail(user.getFirstName(), link));
            user.setToken(token);
        }
        return userRepository.save(user);
    }

    public User updateUser(User user, Long id, MultipartFile image){
        try{
            User upUser=userRepository.findById(id).get();

            upUser.setStatus(user.getStatus());
            upUser.setPhoneNumber(user.getPhoneNumber());
            upUser.setSex(user.getSex());
            upUser.setLastName(user.getLastName());
            upUser.setFirstName(user.getFirstName());
            upUser.setAge(user.getAge());
//            upUser.setEmail(user.getEmail());

            if(image != null){
                String url = fileService.uploadFile(image);
                upUser.setImage(url);
            }

            return userRepository.save(upUser);

        } catch (Exception e){

        }
        return null;
    }

    public boolean deleteUser(Long id){
        try{
            User user = userRepository.findById(id).get();

            if(user == null) {
                return false;
            }

            userRepository.delete(user);

            return true;
        } catch (Exception e){

        }

        return false;
    }

    public boolean changeassword(String password, String username, String oldPassword){
        try{
            User user = userRepository.findByEmail(username);

            if(passwordEncoder.matches(oldPassword,user.getPassword())){
                user.setPassword(password);
                userRepository.save(user);
                return true;
            }

        } catch (Exception e){

        }
        return false;
    }

    public boolean blockUser(String username){
        try{
            User user = userRepository.findByEmail(username);
            user.setStatus(false);
            userRepository.save(user);

            return true;
        } catch (Exception e){

        }
        return false;
    }

    public boolean enableUser(String username){
        try{
            User user = userRepository.findByEmail(username);
            user.setStatus(true);
            userRepository.save(user);

            return true;
        } catch (Exception e){

        }
        return false;
    }

    public String confirmToken(String token) {
        User user = userRepository.findByToken(token);
        if(user==null){
            throw new IllegalStateException("token không chính xác hoặc tài khoản đã được kích hoạt");
        }
        user.setStatus(true);
        user.setToken("");
        userRepository.save(user);
        return "Tài khoản được kích hoạt thành công";
    }

    private String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }

    public Set<UserResponeDTO> getListUserChat(Long id){
        List<Message> messages = messageRepository.findListUserChat(id);
        Set<UserResponeDTO> users  = new HashSet<>();
        for (Message m:
             messages) {
            if(m.getSender().getId() == id){
                System.out.println("sender"+ m.getId());
                users.add(mapper.map(m.getReceiver(),UserResponeDTO.class));
            } if(m.getReceiver().getId() == id ){
                System.out.println("receiver"+ m.getId());
                users.add(mapper.map(m.getSender(),UserResponeDTO.class));
            }
        }
        return users;
    }

    public String getNameById(Long id){
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()){
            throw new NoSuchElementException("id user không tồn tại");
        }
        return user.get().getFirstName()+" "+user.get().getLastName();
    }
}
