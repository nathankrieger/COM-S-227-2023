package hw1;

/**
 * This class represents a camera battery. 
 * 
 * @author Nathan Krieger
 */
public class CameraBattery {

	/**
	 * The number of charger settings for charge
	 */
	public static final int NUM_CHARGER_SETTINGS = 4;
	/**
	 * The default charge rate per minute.
	 */
	public static final double CHARGE_RATE = 2.0;
	/**
	 * The default power consumption.
	 */
	public static final double DEFAULT_CAMERA_POWER_CONSUMPTION = 1.0;
	/**
	 * Total camera charge.
	 */
	private double cameraCharge;
	/**
	 * Total battery charge.
	 */
	private double batteryCharge;
	/**
	 * Peak charge capacity.
	 */
	private double batteryChargeCapacity;
	/**
	 * Total amount of battery drain.
	 */
	private double totalDrain;
	/**
	 * Holds current charger setting.
	 */
	private int chargeSetting;
	/**
	 * Amount of power the camera uses per minute.
	 */
	private double powerConsumption;
	/**
	 * Connection status of battery and camera.
	 */
	private double batteryConnection;

	/**
	 * Creates camera battery.
	 * 
	 * @param batteryStartingCharge: Battery charge when camera battery first created
	 * @param batteryCapacity: MaxImun amount of charge battery can hold
	 */
	public CameraBattery(double batteryStartingCharge, double batteryCapacity) {
		batteryCharge = Math.min(batteryStartingCharge, batteryCapacity);
		batteryChargeCapacity = batteryCapacity;
		
		powerConsumption = 1;
		batteryConnection = 0;
	}

	/**
	 * Changes charger setting.
	 */
	public void buttonPress() {
		chargeSetting = (chargeSetting + 1) % 4;
	}

	/**
	 * Charges battery via camera.
	 * 
	 * @param minutes - number of minutes of charge
	 * @return amount the camera battery has been charged
	 */
	public double cameraCharge(double minutes) {
		double chargeLimit = batteryChargeCapacity - batteryCharge;
		double newCharge = batteryConnection * Math.min(minutes * CHARGE_RATE, chargeLimit);
		batteryCharge += newCharge;
		cameraCharge = batteryConnection * batteryCharge;
		return newCharge;
	}

	/**
	 * Drains battery connected to the camera.
	 * 
	 * @param minutes: number of minutes of drain
	 * @return amount of drain from the battery
	 */
	public double drain(double minutes) {
		double drain = batteryConnection * Math.min(minutes * powerConsumption, batteryCharge);
		batteryCharge -= drain;
		cameraCharge = batteryConnection * batteryCharge;
		totalDrain += drain;
		return drain;
	}

	/**
	 * Charges the battery via external charger.
	 * 
	 * @param minutes number of minutes of external charge
	 * @return amount the battery has been charged
	 */
	public double externalCharge(double minutes) {
		double chargeLimit = batteryChargeCapacity - batteryCharge;
		double externalCharge = Math.min(minutes * CHARGE_RATE * chargeSetting, chargeLimit);
		batteryCharge += externalCharge;
		return externalCharge;
	}

	/**
	 * Resets battery monitoring system by setting total battery drain to 0.
	 * 
	 */
	public void resetBatteryMonitor() {
		totalDrain = 0;
	}

	/**
	 * Gets the battery capacity.
	 * 
	 * @return battery capacity
	 */
	public double getBatteryCapacity() {
		return batteryChargeCapacity;
	}

	/**
	 * Get the battery's current charge.
	 * 
	 * @return battery's current charge
	 */
	public double getBatteryCharge() {
		return batteryCharge;
	}

	/**
	 * Gets the current charge of the camera's battery.
	 * 
	 * @return camera battery charge
	 */
	public double getCameraCharge() {
		return cameraCharge;
	}

	/**
	 * Get the power consumption of the camera.
	 * 
	 * @return power consumption of camera
	 */
	public double getCameraPowerConsumption() {
		return powerConsumption;
	}

	/**
	 * Gets the external charger setting.
	 * 
	 * @return charger setting
	 */
	public int getChargerSetting() {
		return chargeSetting;
	}

	/**
	 * Get total power drain since the last time the battery monitor was reset given
	 * minutes * camera power consumption.
	 * 
	 * @return power drain
	 */
	public double getTotalDrain() {
		return totalDrain;
	}

	/**
	 * Moves battery to the external charger.
	 * 
	 */
	public void moveBatteryExternal() {
		batteryConnection = 1;
	}

	/**
	 * Moves battery to camera.
	 * 
	 */
	public void moveBatteryCamera() {
		batteryConnection = 1;
		cameraCharge = batteryCharge;
	}

	/**
	 * Removes battery from either the camera or external charger.
	 * 
	 */
	public void removeBattery() {
		batteryConnection = 0;
	}

	/**
	 * Sets power consumption level of camera.
	 * 
	 * @param cameraPowerConsumption - amount of power camera consumes per minute
	 */
	public void setCameraPowerConsumption(double cameraPowerConsumption) {
		powerConsumption = cameraPowerConsumption;
	}

}
