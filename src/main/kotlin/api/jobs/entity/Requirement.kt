package api.jobs.entity

import jakarta.persistence.*
import jakarta.validation.constraints.Size
import java.util.UUID

@Entity
@Table(name = "requirement")
data class Requirement(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID?,

    @Column(nullable = false, length = 32)
    val stack: String,

//    @Column(name = "score")
//    @Embedded
//    val level: LevelStructure?,
    
    val min: Int,

    val max: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    val job: Job?
)

//@Embeddable
//class LevelStructure(
//    val min: Int,
//    val max: Int?
//)
