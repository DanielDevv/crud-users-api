package api.jobs.entity

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "job")
data class Job(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID?,

    @Column(length = 500, nullable = false, unique = true)
    val name: String,

    //@Basic(fetch = FetchType.LAZY)
    @Lob
    @Column(nullable = false)
    val description: String?,

    @Column(nullable = false)
    val salary: Int,

    @OneToMany(
        mappedBy = "job",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    val requirements: Set<Requirement> = setOf()

) {
    override fun hashCode(): Int {
        if(id != null) return id.hashCode()

        return super.hashCode()
    }
}
