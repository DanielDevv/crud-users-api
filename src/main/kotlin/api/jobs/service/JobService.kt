package api.jobs.service

import api.jobs.Exception.JobNotFoundException
import api.jobs.dto.*
import api.jobs.entity.Job
import api.jobs.entity.Requirement
import api.jobs.repository.JobRepository
import api.jobs.repository.RequirementRepository
import api.users.repository.UserRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.util.UUID


@Service
class JobService(val jobRepository: JobRepository, val requirementRepository: RequirementRepository, val userRepository: UserRepository) {

    fun createJob(job: JobDTO): JobDTO {

        val jobEntity = Job(null, job.name, job.description, job.salary)

        val savedJob = jobRepository.save(jobEntity)

        val requirements = job.requirements.map { rq -> Requirement(null, rq.stack, rq.level.min, rq.level.min, savedJob) }

        requirementRepository.saveAll(requirements)

        return job.copy(id = jobEntity.id)
    }

    fun getAllJobs(page: Int, size: Int): Pagination {

        return jobRepository.findAll(
            PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "name")
            )
        )
            .let {

             val jobs = it.map {

                 val requirements = it.requirements.map { rq -> RequirementDTO(rq.stack, levelStructure(rq.min, rq.max)) }.toSet()

                 JobDTO(it.id, it.name, it.description, it.salary, requirements)

             }
                val totalElements = jobRepository.count().toInt()

                Pagination(PageDTO(jobs.toList(), page, size, totalElements), it.hasNext())

        }
    }

//    fun getUsersMatchJob(jobId: UUID ,page: Int, size: Int, sort: String): Pagination {
//
//        val specification = jobRepository.findById(jobId).orElseThrow { JobNotFoundException("No job found for the passed in Id: $jobId") }
//
//        specification.let {
//            // pegar os campos para filtrar stack / level minimo
//
//            val fields = it.requirements.map { rq-> mapOf("stack" to rq.stack, "levelMin" to rq.level?.min) }
//
//            val pageable: Pageable = Pageable.ofSize(size)
//
//            // chamar o findAll com o specifications como param
//            userRepository.findAll(spec = fields, pageable = pageable)
//
//        }
//    }

    fun getJob(jobId: UUID): JobDTO {
        val job = jobRepository.findById(jobId).orElseThrow { JobNotFoundException("No job found for the passed in Id: $jobId") }

        val requirements = job.requirements.map { rq -> RequirementDTO(rq.stack, levelStructure(rq.min, rq.max)) }.toSet()

        return JobDTO(jobId, job.name, job.description, job.salary, requirements)
    }

    fun updateJob(jobId: UUID, job: JobDTO): JobDTO {
        val jobExists = jobRepository.findById(jobId).orElseThrow { JobNotFoundException("No job found for the passed in Id: $jobId") }

        return jobExists.let {

            val requirements = job.requirements.map { rq -> Requirement(null, rq.stack, rq.level.min, rq.level.min, jobExists) }.toSet()

            val savedJob = it.copy(
                jobId,
                job.name,
                job.description,
                job.salary,
                requirements
            )

            jobRepository.save(savedJob)
            requirementRepository.saveAll(savedJob.requirements)

            JobDTO(jobId, savedJob.name, savedJob.description, savedJob.salary, job.requirements)
        }
    }

    fun deleteJob(jobId: UUID) {
        jobRepository.findById(jobId).orElseThrow { JobNotFoundException("No job found for the passed in Id: $jobId") }

        jobRepository.deleteById(jobId)
        requirementRepository.deleteRequirementByJobId(jobId)
    }
}