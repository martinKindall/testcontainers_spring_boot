package com.codigomorsa.integracion

import com.codigomorsa.integracion.model.Libro
import com.codigomorsa.integracion.repository.LibroRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class IntegracionApplicationTests {

	@Autowired
	lateinit var libroRepository: LibroRepository

	@Autowired
	lateinit var mockMvc: MockMvc

	@BeforeEach
	fun setup() {
		libroRepository.deleteAll()
	}

	@Test
	fun findAllBooksTest() {
		val url = "/libro"
		mockMvc.get(url).andExpect {
			status { isOk() }
			content { jsonPath("$.size()") { value(0) } }
		}

		libroRepository.save(Libro(name = "test book"))
		libroRepository.save(Libro(name = "test book 2"))

		mockMvc.get(url).andExpect {
			status { isOk() }
			content { jsonPath("$.size()") { value(2) } }
			content { jsonPath("$[0].name") { value("test book") } }
			content { jsonPath("$[1].name") { value("test book 2") } }
		}
	}

	@Test
	fun saveBookTest() {
		val url = "/libro"

		Assertions.assertThat(libroRepository.findAll()).isEmpty()

		mockMvc.post(url) {
			contentType = MediaType.APPLICATION_JSON
			content = "{\"name\": \"libro de prueba\"}"
		}.andExpect {
			status { isOk() }
		}

		Assertions.assertThat(libroRepository.findByName("libro de prueba")).isNotNull
		Assertions.assertThat(libroRepository.findAll().toList().size).isEqualTo(1)
	}

	companion object {

		@Container
		val mysql = MySQLContainer(
				DockerImageName.parse("mysql:8.0.30")
						.asCompatibleSubstituteFor("mysql")
		)
				.withDatabaseName("testdb")
				.withPassword("test1234")

		@JvmStatic
		@DynamicPropertySource
		fun databaseProperties(registry: DynamicPropertyRegistry) {
			registry.add("spring.datasource.url") {
				mysql.jdbcUrl + "?useSSL=false"
			}
			registry.add("spring.datasource.username", mysql::getUsername)
			registry.add("spring.datasource.password", mysql::getPassword)
		}
	}
}
