package org.usfirst.frc.team5465.robot;

import edu.wpi.first.wpilibj.*;

public class RobotDrive 
{
	protected static Victor leftSide;
	protected static Victor rightSide;
	private double setPoint = 0;
	private double currentValue = 0;
	private ADXRS450_Gyro gyro;
	
	public RobotDrive(int leftPort, int rightPort) 
	{
		leftSide = new Victor(leftPort);
		rightSide = new Victor(rightPort);
		gyro = new ADXRS450_Gyro();
		gyro.calibrate();
	}
	
	public void updateGyro()
	{
		currentValue = gyro.getAngle();
	}
	
	public void updateSetPoint()
	{
		setPoint = gyro.getAngle();
	}
	
	public void drive(double speed) {
		updateGyro();
		double difference = setPoint-currentValue;
		double motorPower = difference/180;
		drive(motorPower, speed);
	}
	
	public int getPIDVal()
	{
		return 10;
		
	}
	
	public void drive(double turn, double speed)
	{
		leftSide.set(speed+turn);
		rightSide.set(speed-turn);
	}
 	
	public void stopMotors()
	{
		leftSide.set(0);
		rightSide.set(0);
	}
	
	public int getLeftMotorVal()
	{
		return 1;
	}
	public int getRightMotorVal()
	{
		return 0;
	}
}
