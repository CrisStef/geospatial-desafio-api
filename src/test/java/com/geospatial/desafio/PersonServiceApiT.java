package com.geospatial.desafio;

import com.geospatial.desafio.domain.model.Person;
import com.geospatial.desafio.domain.repository.PersonRepository;
import com.geospatial.desafio.util.DatabaseCleaner;
import com.geospatial.desafio.util.ResourceUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class PersonServiceApiT {

	@LocalServerPort
	private int port;

	@Autowired
	private DatabaseCleaner databaseCleaner;

	@Autowired
	private PersonRepository personRepository;

	private String jsonInsertPerson;

	private String jsonUpdatePerson;

	private final int PERSON_ID_INVALID = 100;

	@Before
	public void setUp() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.port = port;
		RestAssured.basePath = "/person";

		this.jsonInsertPerson = ResourceUtils.getContentFromResource("/json/person-mock-json.json");
		this.jsonUpdatePerson = ResourceUtils.getContentFromResource("/json/person-update-mock-json.json");

		databaseCleaner.clearTables();
		insertData();
	}

	@Test
	public void returnStatusOkWhenGetPersonTest() {
		given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.statusCode(HttpStatus.OK.value());
	}

	@Test
	public void returnStatusCreatedWhenInsertPersonTest() {
		given()
			.body(jsonInsertPerson)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.CREATED.value());
	}

	@Test
	public void returnStatusOkWhenUpdatePersonTest() {
		given()
			.pathParam("personId", 1)
			.body(jsonInsertPerson)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.put("/{personId}")
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("name", equalTo("Ana Maria"));
	}

	@Test
	public void returnStatusNotFoundWhenUpdatePersonNonExistentTest() {
		given()
			.pathParam("personId", PERSON_ID_INVALID)
			.body(jsonInsertPerson)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.put("/{personId}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}

	@Test
	public void returnStatusCreatedWhenUpdatePatchPersonTest() {
		given()
			.pathParam("personId", 1)
			.body(jsonUpdatePerson)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.patch("/{personId}")
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("name", equalTo("João da Silva Ribeiro"));
	}

	@Test
	public void returnStatusCreatedWhenUpdatePatchPersonNonExistentTest() {
		given()
			.pathParam("personId", PERSON_ID_INVALID)
			.body(jsonUpdatePerson)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.patch("/{personId}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}

	@Test
	public void returnStatusOkWhenGetPersonExistsTest() {
		given()
			.pathParam("personId", 1)
			.accept(ContentType.JSON)
		.when()
			.get("/{personId}")
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("name", equalTo("José da Silva"));
	}

	@Test
	public void returnStatusNotFoundWhenGetPersonNonExistentTest() {
		given()
			.pathParam("personId", PERSON_ID_INVALID)
			.accept(ContentType.JSON)
		.when()
			.get("/{personId}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}

	@Test
	public void returnStatusOkWhenGetPersonAgeDaysTest() {
		given()
			.pathParam("personId", 1)
			.queryParam("output", "DAYS")
			.accept(ContentType.JSON)
		.when()
			.get("/{personId}/age/")
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("description", containsString("days"));
	}

	@Test
	public void returnStatusOkWhenGetPersonAgeMonthsTest() {
		given()
			.pathParam("personId", 1)
			.queryParam("output", "Months")
			.accept(ContentType.JSON)
		.when()
			.get("/{personId}/age/")
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("description", containsString("months"));
	}

	@Test
	public void returnStatusOkWhenGetPersonAgeYearsTest() {
		given()
			.pathParam("personId", 1)
			.queryParam("output", "Years")
			.accept(ContentType.JSON)
		.when()
			.get("/{personId}/age/")
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("description", containsString("years"));
	}

	@Test
	public void returnStatusNotFoundWhenGetPersonNonExistentAgeTest() {
		given()
			.pathParam("personId", PERSON_ID_INVALID)
			.queryParam("output", "Months")
			.accept(ContentType.JSON)
		.when()
			.get("/{personId}/age/")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}

	@Test
	public void returnStatusBadRequestWhenGetPersonAgeTest() {
		given()
			.pathParam("personId", 1)
			.queryParam("output", "aa")
			.accept(ContentType.JSON)
		.when()
			.get("/{personId}/age/")
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value());
	}

	@Test
	public void returnStatusNoContentWhenDeletePersonTest() {
		given()
			.pathParam("personId", 1)
			.accept(ContentType.JSON)
		.when()
			.delete("/{personId}")
		.then()
			.statusCode(HttpStatus.NO_CONTENT.value());
	}

	@Test
	public void returnStatusNotFoundWhenDeletePersonNonExistentTest() {
		given()
			.pathParam("personId", PERSON_ID_INVALID)
			.accept(ContentType.JSON)
		.when()
			.delete("/{personId}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}

	@Test
	public void returnStatusOkWhenGetPersonSalaryTest() {
		given()
			.pathParam("personId", 1)
			.queryParam("output", "MIN")
			.accept(ContentType.JSON)
		.when()
			.get("/{personId}/salary")
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("value", notNullValue());
	}

	@Test
	public void returnStatusBadRequestWhenGetPersonSalaryTest() {
		given()
			.pathParam("personId", 1)
			.queryParam("output", "aaa")
			.accept(ContentType.JSON)
		.when()
			.get("/{personId}/salary")
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value());
	}

	@Test
	public void returnStatusNotFoundWhenGetPersonSalaryTest() {
		given()
				.pathParam("personId", PERSON_ID_INVALID)
				.queryParam("output", "aaa")
				.accept(ContentType.JSON)
				.when()
				.get("/{personId}/salary")
				.then()
				.statusCode(HttpStatus.NOT_FOUND.value());
	}

	private void insertData() {
		Person person = new Person();
		person.setId(1L);
		person.setName("José da Silva");
		person.setBirthDate(LocalDate.of(2000, 4, 6));
		person.setAdmissionDate(LocalDate.of(2000, 5, 10));
		personRepository.save(person);
	}
}