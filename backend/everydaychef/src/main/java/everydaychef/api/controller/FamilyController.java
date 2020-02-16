package everydaychef.api.controller;


import everydaychef.api.model.Family;
import everydaychef.api.repository.FamilyRepository;
import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class FamilyController {

    private final FamilyRepository familyRepository;

    public FamilyController(FamilyRepository familyRepository) {
        this.familyRepository = familyRepository;
    }

    @GetMapping("/family")
    public ResponseEntity<List<Family>> index(){
        return new ResponseEntity<>( familyRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/family/{id}")
    public ResponseEntity<Family> show(@PathVariable String id){
        int familyId = Integer.parseInt(id);
        return familyRepository.findById(familyId)
                .map(family -> new ResponseEntity<>(family, HttpStatus.OK))
                .orElse(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }
//
//    @PostMapping("/family/search")
//    public List<Family> search(@RequestBody Map<String, String> body){
//        String searchTerm = body.get("text");
//        return familyRepository.findBy(searchTerm, searchTerm);
//    }

    @PostMapping("/family")
    public ResponseEntity<Family> create(@RequestBody Map<String, String> body){
        String name = body.get("name");
        return new ResponseEntity<>(familyRepository.save(new Family(name)), HttpStatus.OK);
    }

    @PutMapping("/family/{id}")
    public ResponseEntity<Family> update(@PathVariable String id, @RequestBody Map<String, String> body){
        int familyId = Integer.parseInt(id);
        Family family = familyRepository.findById(familyId).orElse(new Family());
        family.setName(body.get("name"));
        return new ResponseEntity<>(familyRepository.save(family), HttpStatus.OK);
    }

    @DeleteMapping("/family/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable String id){
        int familyId = Integer.parseInt(id);
        familyRepository.findById(familyId).ifPresent(family -> {
            family.getUsers().forEach(user->user.setFamily(new Family(user.getName() + "'s Family")));
        });
        // TODO(ADD ERROR HANDLING OF THE METHOD...)
        familyRepository.deleteById(familyId);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

}
