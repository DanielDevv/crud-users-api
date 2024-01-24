package api.users.entity

import api.users.validator.MaxCharacters
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "Users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID?,

    var nickname: String?,

    @Column(unique = true, length = 32, nullable = false)
    var name: String,

    @Column(nullable = false)
    var birth_date: LocalDateTime,

    @MaxCharacters
    var stack: MutableSet<String>?
)