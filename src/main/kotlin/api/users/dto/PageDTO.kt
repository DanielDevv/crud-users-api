package api.users.dto

data class Pagination(
    val data: PageDTO,
    val hasNext: Boolean
)

data class PageDTO(
    val records: List<UserDTO>,
    val page: Int,
    val page_size: Int,
    val total: Int
)