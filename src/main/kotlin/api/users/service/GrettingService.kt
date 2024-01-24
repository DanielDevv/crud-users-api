package api.users.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class GrettingService {

    @Value("\${message}")
    lateinit var message: String

    fun retrieveGretting (name: String): String = "$name, $message"
}