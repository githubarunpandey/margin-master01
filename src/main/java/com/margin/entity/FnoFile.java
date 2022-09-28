package com.margin.entity;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "ANT_UPLOAD_MAILDATA_FNO")
public class FnoFile {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	
	private Long id;
	private String type;
	private double ratioUpperValue;
	private double ratioLowerValue;
	private double adjustmentValue;
	private double existedLot;
	private double adjustedLot;
	private double existedStkPrice;
	private double adjustedStkPrice;
	private double entryPrice;
	private double existedEntryValue;
	private double adjustedEntryPrice;
	private String scrip;
	private LocalDate createdAt;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public double getRatioUpperValue() {
		return ratioUpperValue;
	}
	public void setRatioUpperValue(double ratioUpperValue) {
		this.ratioUpperValue = ratioUpperValue;
	}
	public double getRatioLowerValue() {
		return ratioLowerValue;
	}
	public void setRatioLowerValue(double rationLowerValue) {
		this.ratioLowerValue = rationLowerValue;
	}
	public double getAdjustmentValue() {
		return adjustmentValue;
	}
	public void setAdjustmentValue(double adjustmentValue) {
		this.adjustmentValue = adjustmentValue;
	}
	public double getExistedLot() {
		return existedLot;
	}
	public void setExistedLot(double existedLot) {
		this.existedLot = existedLot;
	}
	public double getAdjustedLot() {
		return adjustedLot;
	}
	public void setAdjustedLot(double adjustedLot) {
		this.adjustedLot = adjustedLot;
	}
	public double getExistedStkPrice() {
		return existedStkPrice;
	}
	public void setExistedStkPrice(double existedStkPrice) {
		this.existedStkPrice = existedStkPrice;
	}
	public double getAdjustedStkPrice() {
		return adjustedStkPrice;
	}
	public void setAdjustedStkPrice(double adjustedStkPrice) {
		this.adjustedStkPrice = adjustedStkPrice;
	}
	public double getEntryPrice() {
		return entryPrice;
	}
	public void setEntryPrice(double entryPrice) {
		this.entryPrice = entryPrice;
	}
	public double getExistedEntryValue() {
		return existedEntryValue;
	}
	public void setExistedEntryValue(double existedEntryValue) {
		this.existedEntryValue = existedEntryValue;
	}
	public double getAdjustedEntryPrice() {
		return adjustedEntryPrice;
	}
	public void setAdjustedEntryPrice(double adjustedEntryPrice) {
		this.adjustedEntryPrice = adjustedEntryPrice;
	}
	public String getScrip() {
		return scrip;
	}
	public void setScrip(String scrip) {
		this.scrip = scrip;
	}
	
	@Override
	public String toString() {
		return "Fno [id=" + id + ", type=" + type + ", ratioUpperValue=" + ratioUpperValue + ", rationLowerValue="
				+ ratioLowerValue + ", adjustmentValue=" + adjustmentValue + ", existedLot=" + existedLot
				+ ", adjustedLot=" + adjustedLot + ", existedStkPrice=" + existedStkPrice + ", adjustedStkPrice="
				+ adjustedStkPrice + ", entryPrice=" + entryPrice + ", existedEntryValue=" + existedEntryValue
				+ ", adjustedEntryPrice=" + adjustedEntryPrice + ", scrip=" + scrip + "]";
	}
	public LocalDate getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDate createdAt) {
		this.createdAt = createdAt;
	}
	
	
	
}