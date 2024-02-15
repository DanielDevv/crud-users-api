package api.users.entity

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "stacks")
data class Stack(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID?,

    @Column(length = 32, nullable = false)
    var name: String,

    @Column(nullable = false)
    var score: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User?
)