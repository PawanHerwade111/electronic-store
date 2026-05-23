package com.electronic.store.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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
@Table(name = "role")
public class Role {
	
	@Id
	private String roleId;
	private String name;//ADMIN, NORMAL
	@ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
	private List<User> users = new ArrayList<>();

}
