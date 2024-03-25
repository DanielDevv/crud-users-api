package api.jobs.repository

import api.jobs.entity.Requirement
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface RequirementRepository : CrudRepository<Requirement, UUID> {
    @Query("DELETE FROM requirement WHERE job_id = ?1", nativeQuery = true)
    fun deleteRequirementByJobId(jobId: UUID)
}