package api.users.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID?,

    @Column(length = 32)
    var nickname: String?,

    @Column(unique = true, nullable = false)
    var name: String,

    @Column(nullable = false)
    var birthDate: LocalDateTime,

    @OneToMany(
        mappedBy = "user",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    var stack: Set<Stack> = setOf()
) {
    override fun hashCode(): Int {
        if (id != null) return id.hashCode()

        return super.hashCode()
    }
}