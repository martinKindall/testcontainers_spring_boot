package com.codigomorsa.integracion.repository

import com.codigomorsa.integracion.model.Libro
import org.springframework.data.repository.CrudRepository

interface LibroRepository: CrudRepository<Libro, Int> {

    fun findByName(name: String): Libro?
}