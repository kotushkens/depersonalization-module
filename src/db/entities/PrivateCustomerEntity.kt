package db.entities

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity(name = "private_customers")
@Table(name="private_customers", schema = "account_service")
class PrivateCustomerEntity(
    @Id
    @Column(name = "dboId")
    val dboId: Long = 1L,
    @Column(name="first_name")
    val firstName: String = "",
    @Column(name="middle_name")
    val middleName: String? = "",
    @Column(name="last_name")
    val lastName: String = "",
    @Column(name="main_phone_number")
    val mainPhoneNumber: String = "",
    @Column(name="optional_phone_number")
    val optionalPhoneNumber: String? = "",
    @Column(name="inn")
    val inn: String? = "",
    @Column(name="passport")
    val passport: String? = "",
) : Serializable {
}