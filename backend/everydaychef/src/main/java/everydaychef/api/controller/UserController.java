package everydaychef.api.controller;

import com.sun.net.httpserver.HttpsConfigurator;
import everydaychef.api.exceptions.ValidationException;
import everydaychef.api.model.Family;
import everydaychef.api.model.User;
import everydaychef.api.repository.FamilyRepository;
import everydaychef.api.repository.UserRepository;
import javassist.NotFoundException;
import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
public class UserController {

    final private UserRepository userRepository;
    final private FamilyRepository familyRepository;

    public UserController(UserRepository userRepository,
                          FamilyRepository familyRepository) {
        this.userRepository = userRepository;
        this.familyRepository = familyRepository;
    }

    private Optional<User> getUserById(String Id){
        return userRepository.findById(Integer.parseInt(Id));
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUser() throws NoSuchElementException {
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<User> getUser(@PathVariable String userId) throws NoSuchElementException {
        int Id = Integer.parseInt(userId);
        Optional<User> foundUser = userRepository
                .findById(Id);
        return foundUser
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }


    @GetMapping("/user/{userId}/family")
    public ResponseEntity<Family> getUserFamily(@PathVariable String userId) throws NotFoundException {
        int Id = Integer.parseInt(userId);

        return userRepository.findById(Id)
                .map(user -> new ResponseEntity<>(user.getFamily(), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @PostMapping("/user")
    public Boolean create(@RequestBody Map<String, String> body) throws ValidationException {
        String username = body.get("username");
        if (userRepository.existsByName(username)){
            throw new ValidationException("Username already existed");
        }
        String password = body.get("password");
        String encodedPassword = new BCryptPasswordEncoder().encode(password);
        userRepository.save(new User(username, encodedPassword, new Family(username + "'s family")));
        return true;
    }

    @PutMapping("/user/{userId}/family/{familyId}")
    public ResponseEntity<Boolean> inviteUserToFamily(@PathVariable String userId, @PathVariable String familyId,
                                    @RequestBody Map<String, String> body){
        Optional<Family> optionalFamily =  familyRepository.findById(Integer.parseInt(familyId));
        if(optionalFamily.isPresent()){
            Family family = optionalFamily.get();
            getUserById(userId).ifPresent(user -> {
                user.addInvitation(family);
            });
            return new ResponseEntity<>(true, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/user/{userId}/family")
    public ResponseEntity<Boolean> userLeaveFamily(@PathVariable String userId){
        Optional<User> userOpt = getUserById(userId);
        if(userOpt.isPresent()){
            User user = userOpt.get();
            user.setFamily(new Family(user.getName() + "'s Family"));
            return new ResponseEntity<>(true, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
    }

}
