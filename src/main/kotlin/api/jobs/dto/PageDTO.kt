package api.jobs.dto

data class Pagination(
    val data: PageDTO,
    val hasNext: Boolean
)

data class PageDTO(
    val records: List<JobDTO>,
    val page: Int,
    val pageSize: Int,
    val total: Int
)
