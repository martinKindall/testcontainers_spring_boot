package com.codigomorsa.integracion.service

import com.codigomorsa.integracion.model.Libro
import com.codigomorsa.integracion.repository.LibroRepository
import org.springframework.stereotype.Service

@Service
class LibroService(
        val libroRepository: LibroRepository
) {

    fun findAll(): List<Libro> {
        return libroRepository.findAll().toList()
    }

    fun findByName(name: String): Libro? {
        return libroRepository.findByName(name)
    }
}