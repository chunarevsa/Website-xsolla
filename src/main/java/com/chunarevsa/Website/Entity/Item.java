package com.chunarevsa.Website.Entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "ITEM")
public class Item extends Base {

	@Id
	@Column (name = "ITEM_ID")
	@GeneratedValue(strategy =  GenerationType.SEQUENCE, generator = "item_seq")
	@SequenceGenerator(name = "item_seq", allocationSize = 1)
	private Long id;
	
	@Column(name = "NAME", nullable = false)
	private String name;

	@Column(name = "TYPE", nullable = false)
	private String type;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "IS_ACTIVE", nullable = false)
	private Boolean active;

	@OneToMany (cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "ITEM_ID")
	private Set<Price> prices = new HashSet<>();

	@JsonIgnore
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "USER_ID", insertable = false, updatable = false)
	private User user;

	public Item() {
		super();
	}

	public Item(Long id, 
					String name, 
					String type, 
					String description, 
					Boolean active, 
					Set<Price> prices,
					User user) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.description = description;
		this.active = active;
		this.prices = prices;
		this.user = user;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean isActive() {
		return this.active;
	}

	public Boolean getActive() {
		return this.active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Set<Price> getPrices() {
		return this.prices;
	}

	public void setPrices(Set<Price> prices) {
		this.prices = prices;
	}


	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "{" +
			" id='" + getId() + "'" +
			", name='" + getName() + "'" +
			", type='" + getType() + "'" +
			", description='" + getDescription() + "'" +
			", active='" + isActive() + "'" +
			", prices='" + getPrices() + "'" +
			", user='" + getUser() + "'" +
			"}";
	}

}
