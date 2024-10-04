package com.geospatial.desafio.domain.service;

import com.geospatial.desafio.api.mapper.PersonMapper;
import com.geospatial.desafio.api.model.request.PersonRequest;
import com.geospatial.desafio.api.model.request.PersonUpdatePatchRequest;
import com.geospatial.desafio.api.model.request.PersonUpdateRequest;
import com.geospatial.desafio.api.model.response.AgeResponse;
import com.geospatial.desafio.api.model.response.PersonResponse;
import com.geospatial.desafio.api.model.response.SalaryResponse;
import com.geospatial.desafio.domain.exception.BusinessException;
import com.geospatial.desafio.domain.exception.EntityAlreadyExistsException;
import com.geospatial.desafio.domain.exception.PersonNotFoundException;
import com.geospatial.desafio.domain.model.Person;
import com.geospatial.desafio.domain.model.enums.PersonAge;
import com.geospatial.desafio.domain.model.enums.PersonSalary;
import com.geospatial.desafio.domain.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@Service
public class PersonService {
	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private PersonMapper personMapper;

	private static final String MSG_PERSON_ALREADY_EXISTS = "Person with ID (%d) already exists.";
	private static final BigDecimal INITIAL_SALARY = BigDecimal.valueOf(1558.00);
	private static final BigDecimal SALARY_INCREASE_RATE = BigDecimal.valueOf(0.18);
	private static final BigDecimal ADDITIONAL_INCREASE = BigDecimal.valueOf(500.00);
	private static final BigDecimal MIN_SALARY = BigDecimal.valueOf(1302.00);

	public PersonResponse create(PersonRequest personRequest) {
		this.validateId(personRequest);

		Person person = personRepository.save(personMapper.personRequestForPerson(personRequest));

		return personMapper.personForPersonResponse(person);
	}

	private void validateId(PersonRequest personRequest) {
		if (personRequest.getId() == null) {
			personRequest.setId(personRepository.findMaxId() + 1);
		} else {
			if (personRepository.existsById(personRequest.getId())) {
				throw new EntityAlreadyExistsException(String.format(MSG_PERSON_ALREADY_EXISTS, personRequest.getId()));
			}
		}
	}

	public PersonResponse getById(Long id) {
		return personMapper.personForPersonResponse(this.findById(id));
	}

	public AgeResponse getAge(Long personId, String output) {
		Person person = findById(personId);
		PersonAge personAge = PersonAge.fromString(output);

		LocalDate currentDate = LocalDate.now();

		if (person.getBirthDate() == null) {
			throw new BusinessException("The date of birth cannot be null.");
		}

		AgeResponse ageResponse = new AgeResponse();
		Long calculatedAge = 0L;
		String message = null;
		Period period = Period.between(person.getBirthDate(), currentDate);

		switch (personAge) {
			case DAYS:
				calculatedAge = ChronoUnit.DAYS.between(person.getBirthDate(), currentDate);
				message = "Age in days: %d";
				break;
			case MONTHS:
				calculatedAge = period.toTotalMonths();
				message = "Age in months: %d";
				break;
			case YEARS:
				calculatedAge = Long.valueOf(period.getYears());
				message = "Age in years: %d";
				break;
			default:
				throw new BusinessException("Invalid output format.");
		}

		ageResponse.setDescription(String.format(message, calculatedAge));
		ageResponse.setValue(calculatedAge);

		return ageResponse;
	}

	public SalaryResponse getSalary(Long personId, String output) {
		Person person = findById(personId);
		PersonSalary personSalary = PersonSalary.fromString(output);

		Integer yearsInCompany = person.getYearsInCompany();

		BigDecimal salary = this.calculateSalary(yearsInCompany);

		SalaryResponse salaryResponse = new SalaryResponse();
		BigDecimal calculatedSalary;
		String message = null;

		switch (personSalary) {
			case MIN:
				calculatedSalary = salary.divide(MIN_SALARY, 2, RoundingMode.CEILING);
				message = "Minimum salary: %.2f";
				break;
			case FULL:
				calculatedSalary = salary;
				message = "Salary: R$ %.2f";
				break;
			default:
				throw new IllegalArgumentException("Invalid output format.");
		}

		salaryResponse.setDescription(String.format(message, calculatedSalary));
		salaryResponse.setValue(calculatedSalary.setScale(2, RoundingMode.CEILING));

		return salaryResponse;
	}

	private BigDecimal calculateSalary(int years) {
		BigDecimal salary = INITIAL_SALARY;
		for (int i = 0; i < years; i++) {
			salary = salary.add(salary.multiply(SALARY_INCREASE_RATE)).add(ADDITIONAL_INCREASE);
		}
		return salary;
	}

	public List<PersonResponse> findAll() {
		Sort sort = Sort.by(Sort.Direction.ASC, "name");
		return personMapper.personListForPersonListResponse(this.listAll(sort));
	}

	public PersonResponse alter(@Valid PersonUpdateRequest personUpdateRequest, Long id) {
		Person currentPerson = findById(id);

		Person person = this.update(personMapper.personUpdateRequestForPerson(personUpdateRequest), currentPerson);

		return personMapper.personForPersonResponse(person);
	}

	public PersonResponse alter(Map<String, Object> originData, Long personId, HttpServletRequest request) {
		Person currentPerson = findById(personId);

		PersonUpdatePatchRequest personUpdateRequest = personMapper.mapToPersonUpdatePatchRequest(originData);

		personMapper.copyPersonUpdatePatchRequestForPerson(personUpdateRequest, currentPerson);

		personRepository.save(currentPerson);

		return personMapper.personForPersonResponse(currentPerson);
	}

	@Transactional
	public Person save(Person person) {
		return personRepository.save(person);
	}

	@Transactional
	public void remove(Long id) {
		try {
			personRepository.deleteById(id);
			personRepository.flush();
		} catch (EmptyResultDataAccessException e) {
			throw new PersonNotFoundException(id);
		}
	}

	@Transactional
	private Person update(Person person, Person currentPerson) {
		personMapper.copyPersonForCurrentPerson(person, currentPerson);

		currentPerson = personRepository.save(currentPerson);

		return currentPerson;
	}

	private List<Person> listAll(Sort sort) {
		return personRepository.findAll(sort);
	}

	public Person findById(Long id) {
		return personRepository.findById(id).orElseThrow(() -> new PersonNotFoundException(id));
	}
}