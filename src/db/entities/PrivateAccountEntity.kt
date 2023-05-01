package db.entities

import java.io.Serializable
import java.time.LocalDate
import javax.persistence.*

@Entity(name = "private_accounts")
@Table(name="private_accounts", schema = "account_service")
class PrivateAccountEntity(
    @Id
    @Column(name = "id")
    val id: Long = 1L,
    @Column(name="dboid")
    val dboId: Long = 1L,
    @Column(name="accountnumber")
    val accountNumber: Long = 1L,
    @Column(name="bic")
    val bic: Long = 1L,
    @Column(name="status")
    val status: String = "",
    @Column(name="balance")
    val balance: Double = 1.1,
    @Column(name="updatedat")
    val updatedAt: LocalDate? = null
) : Serializable