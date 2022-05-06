package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Person {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(name = "first_name")
	@NotNull(message = "field is empty")
	private String firstName;
	@Column(name = "second_name")
	@NotNull(message = "field is empty")
	private String secondName;
	@Max(value = 130, message = "1<age<130")
	@Min(value = 1, message = "1<age<130")
	private int age;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "document_id", unique = true)
	private Documents documents;

	@ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinTable(name = "person_hobbies", joinColumns =
	@JoinColumn(name = "person_id"), inverseJoinColumns = @JoinColumn(name = "hobbies_id")
	)
	private Set<Hobbies> hobbies = new HashSet<>();


}
