package Bean;

public class Plane {
//	飞机识别码
	private String id;
//	飞机航班号
	private String number;
//	经度
	private String longitude;
//	纬度
	private String latitude;
//	高度
	private String height;
//	垂直空速
	private String airSpeedVertical;
//	水平空速
	private String airSpeedHorizontal;
//	地速
	private String groundspeed;
//	航向
	private String course;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getAirSpeedVertical() {
		return airSpeedVertical;
	}
	public void setAirSpeedVertical(String d) {
		this.airSpeedVertical = d;
	}
	public String getAirSpeedHorizontal() {
		return airSpeedHorizontal;
	}
	public void setAirSpeedHorizontal(String airSpeedHorizontal) {
		this.airSpeedHorizontal = airSpeedHorizontal;
	}
	public String getGroundspeed() {
		return groundspeed;
	}
	public void setGroundspeed(String groundspeed) {
		this.groundspeed = groundspeed;
	}
	public String getCourse() {
		return course;
	}
	public void setCourse(String course) {
		this.course = course;
	}
}
