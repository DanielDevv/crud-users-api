package api.users.controller

import api.users.service.GrettingService
import mu.KLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/grettings")
class GrettingController(val grettingService: GrettingService) {

    companion object : KLogging()

    @GetMapping("/{name}")
    fun retrieveGretting(@PathVariable("name") name: String): String {
        logger.info("Name is $name")
        return grettingService.retrieveGretting(name)
    }
}