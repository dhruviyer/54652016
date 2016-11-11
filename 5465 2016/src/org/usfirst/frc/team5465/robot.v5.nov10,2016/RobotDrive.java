package org.usfirst.frc.team5465.robot;

import edu.wpi.first.wpilibj.*;

public class RobotDrive 
{
	protected static Victor leftSide;
	protected static Victor rightSide;
	private double setPoint = 0;
	private double currentValue = 0;
	private ADXRS450_Gyro gyro;
	private double robot_speed;
	private double robot_turn;
	
	public RobotDrive(int leftPort, int rightPort, ADXRS450_Gyro gyro) 
	{
		leftSide = new Victor(leftPort);
		rightSide = new Victor(rightPort);
		this.gyro = gyro;
	}
	
	public void updateGyro()
	{
		currentValue = gyro.getAngle();
	}
	
	public void updateSetPoint()
	{
		setPoint = gyro.getAngle();
	}
	
	public double getSetPoint()
	{
		return setPoint;
	}
	
	public void drive(double speed) {
		updateGyro();
		double difference = setPoint-currentValue;
		double motorPower = difference/180;
		drive(motorPower, speed);
	}
	
	public double getPIDVal()
	{
		return setPoint-currentValue;
	}
	
	public void drive(double turn, double speed)
	{
		robot_speed = speed;
		robot_turn = turn;
		leftSide.set(speed+turn); //x and y speed were switched
		rightSide.set(speed-turn);
	}
 	
	public double getRobot_speed() {
		return robot_speed;
	}

	public double getRobot_turn() {
		return robot_turn;
	}

	public void stopMotors()
	{
		leftSide.set(0);
		rightSide.set(0);
	}
	
	public double getLeftMotorVal()
	{
		return leftSide.getSpeed();
	}
	public double getRightMotorVal()
	{
		return rightSide.getSpeed();
	}
	
	public double getGyroValue()
	{
		return gyro.getAngle();
	}
	
	public void setRaw(double left, double right)
	{
		leftSide.set(left);
		rightSide.set(right);
	}
}
