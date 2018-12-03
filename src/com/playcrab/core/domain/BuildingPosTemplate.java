package com.playcrab.core.domain;

public class BuildingPosTemplate {
	
	private int pos;
	private int castleType;
	private int openLevel;
	private int buildEntId;
	private int buildingTypId;

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public int getCastleType() {
		return castleType;
	}

	public void setCastleType(int castleType) {
		this.castleType = castleType;
	}

	public int getOpenLevel() {
		return openLevel;
	}

	public void setOpenLevel(int openLevel) {
		this.openLevel = openLevel;
	}

	public int getBuildEntId() {
		return buildEntId;
	}

	public void setBuildEntId(int buildEntId) {
		this.buildEntId = buildEntId;
	}

	public int getBuildingTypId() {
		return buildingTypId;
	}

	public void setBuildingTypId(int buildingTypId) {
		this.buildingTypId = buildingTypId;
	}

}
