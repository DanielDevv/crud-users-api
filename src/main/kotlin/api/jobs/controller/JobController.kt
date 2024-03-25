package api.jobs.controller

import api.jobs.dto.JobDTO
import api.jobs.dto.Pagination
import api.jobs.service.JobService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/v1/jobs")
@Validated
class JobController(val jobService: JobService) {

    @PostMapping
    fun createJob(@RequestBody @Valid job: JobDTO): ResponseEntity<JobDTO> {
        val createdJob = jobService.createJob(job)

        return ResponseEntity.status(201).body(createdJob)
    }

    @GetMapping
    fun getAllJobs(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "15") size: Int
    ): ResponseEntity<Pagination> {
        val jobs = jobService.getAllJobs(page, size)

        if(jobs.hasNext) return ResponseEntity.status(206).body(jobs)

        return ResponseEntity.status(200).body(jobs)
    }

    @GetMapping("/{job_id}")
    fun getJob(@PathVariable("job_id") jobId: UUID): ResponseEntity<JobDTO> {
        val job = jobService.getJob(jobId)

        return ResponseEntity.status(200).body(job)
    }

    @PutMapping("/{job_id}")
    fun updateJob(@PathVariable("job_id") jobId: UUID, @RequestBody @Valid job: JobDTO): ResponseEntity<JobDTO> {
        val updatedJOb = jobService.updateJob(jobId, job)

        return ResponseEntity.status(200).body(updatedJOb)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{job_id}")
    fun deleteJob(@PathVariable("job_id") jobId: UUID) = jobService.deleteJob(jobId)

    @GetMapping("/{job_id}/match")
    fun getMatchUsers(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "15") size: Int,
        @RequestParam(defaultValue = "name") sort: String,
        @PathVariable("job_id") jobId: UUID
    ): ResponseEntity<Pagination> {
        val jobs = jobService.getAllJobs(page, size)

        if(jobs.hasNext) return ResponseEntity.status(206).body(jobs)

        return ResponseEntity.status(200).body(jobs)
    }
}