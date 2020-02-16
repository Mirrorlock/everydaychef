package everydaychef.api.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    private String name;
    private String password;
    private char account_type;

    @ManyToOne
    private Family family;

    @ManyToMany
    private Set<Recipe> likedRecipes;

    @ManyToMany
        @JoinTable(name="user_invitations",
                joinColumns = @JoinColumn(name="user_id"),
                inverseJoinColumns= @JoinColumn(name="family_id"))

    private Set<Family> familyInviters;

    public User() {
    }

    public User(String name, String password, Family family){
        this.name = name;
        this.password = password;
        this.family = family;
        likedRecipes = new HashSet<Recipe>();
    }

    public User(String name, char account_type, Family family){
        this.name = name;
        this.account_type = account_type;
        this.family = family;
        likedRecipes = new HashSet<Recipe>();
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Family getFamily() {
        return family;
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    public Set<Recipe> getLikedRecipes() {
        return likedRecipes;
    }

    public void setLikedRecipes(Set<Recipe> likedRecipes) {
        this.likedRecipes = likedRecipes;
    }

    public char getAccount_type() {
        return account_type;
    }

    public void setAccount_type(char account_type) {
        this.account_type = account_type;
    }

    public void setInvitations(Set<Family> invitations) {
        this.familyInviters = invitations;
    }

    public void addInvitation(Family inviter){
        this.familyInviters.add(inviter);
    }

    public Set<Family> getInvitations() {
        return familyInviters;
    }
}
