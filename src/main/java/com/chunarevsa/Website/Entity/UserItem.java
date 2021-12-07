package com.chunarevsa.Website.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

// USER_ITEM принадлежащие конкретному User через UserInventory/InventoryUnit
// По сути это копия ITEM, но без цен (т.е без возможности его купить другим пользователем). 
// Изменяя ITEM USER_ITEM не будет меняться
@Entity
@Table(name = "USER_ITEM")
public class UserItem extends DateAudit { // TODO: возможно без base
	
	@Id
	@Column (name = "USER_ITEM_ID")
	@GeneratedValue(strategy =  GenerationType.SEQUENCE, generator = "user_item_seq")
	@SequenceGenerator(name = "user_item_seq", allocationSize = 1)
	private Long id;

	@Column(name = "ITEM_ID", nullable = false)
	private String itemId;

	@Column(name = "NAME", nullable = false)
	private String name;

	@Column(name = "TYPE", nullable = false)
	private String type;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "IS_ACTIVE", nullable = false)
	private Boolean active;

	@JsonIgnore
	@OneToOne(optional = false, mappedBy = "userItem")
	private InventoryUnit inventoryUnit;

	public UserItem() {
		super();
	}

	public UserItem(Long id, String itemId, String name, String type, String description, Boolean active, InventoryUnit inventoryUnit) {
		this.id = id;
		this.itemId = itemId;
		this.name = name;
		this.type = type;
		this.description = description;
		this.active = active;
		this.inventoryUnit = inventoryUnit;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getItemId() {
		return this.itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
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

	public InventoryUnit getInventoryUnit() {
		return this.inventoryUnit;
	}

	public void setInventoryUnit(InventoryUnit inventoryUnit) {
		this.inventoryUnit = inventoryUnit;
	}

	@Override
	public String toString() {
		return "{" +
			" id='" + getId() + "'" +
			", itemId='" + getItemId() + "'" +
			", name='" + getName() + "'" +
			", type='" + getType() + "'" +
			", description='" + getDescription() + "'" +
			", active='" + isActive() + "'" +
			", inventoryUnit='" + getInventoryUnit() + "'" +
			"}";
	}

} 
