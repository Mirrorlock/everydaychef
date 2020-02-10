package everydaychef.entities

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id


@Entity
class User{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null
    var email: String? = null
    var token: String? = null
    var username: String? = null
}