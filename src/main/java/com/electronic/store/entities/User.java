package com.electronic.store.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

	@Id
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	private String userId;

	@Column(name = "user_name")
	private String name;

	@Column(name = "user_email", unique = true)
	private String email;

	@Column(name = "user_password", length = 10)
	private String password;

	private String gender;

	@Column(length = 1000)
	private String about;

	@Column(name = "user_image_name")
	private String imageName;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<Order> orders = new ArrayList<>();
}
