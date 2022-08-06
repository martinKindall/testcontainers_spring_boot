package com.codigomorsa.integracion.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class LibrosController {

    @GetMapping("/libro")
    fun getAllLibros(): String {
        return "Mis libros"
    }
}
