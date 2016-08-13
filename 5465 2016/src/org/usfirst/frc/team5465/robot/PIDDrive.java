package org.usfirst.frc.team5465.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;

public class PIDDrive {
	private ADXRS450_Gyro gyro;
	private double setPoint = 0;
	private double currentValue = 0;
	private RobotDrive robot;
	
	public PIDDrive(ADXRS450_Gyro gyro, RobotDrive robot)
	{
		this.gyro = gyro;
		this.robot = robot;
	}
	
	private void updateGyro()
	{
		currentValue = gyro.getAngle();
	}
	
	public void updateSetPoint()
	{
		setPoint = gyro.getAngle();
	}
	
	private void drive(double speed)
	{
		updateGyro();
		double difference = setPoint-currentValue;
		double motorPower = difference/180;
		robot.drive(speed, motorPower);
	}
}
