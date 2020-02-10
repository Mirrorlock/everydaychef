package everydaychef

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("api")
class APIController {
    @GetMapping("/greeting")
    fun greeting(@RequestParam(value = "name", defaultValue = "World") name: String?): String {
        return "Hi there, ${name}!"
    }

    @GetMapping("/greeting/protected")
    fun greetingProtected(@RequestParam(value = "name", defaultValue = "World") name: String?): String {
        return "Hi there from protected url, ${name}!"
    }

}