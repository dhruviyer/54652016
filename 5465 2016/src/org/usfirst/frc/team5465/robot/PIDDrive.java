package org.usfirst.frc.team5465.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Victor;

public class PIDDrive extends RobotDrive
{
	private ADXRS450_Gyro gyro;
	private double setPoint = 0;
	private double currentValue = 0;
	private Victor leftSide;
	private Victor rightSide;
	
	public PIDDrive(ADXRS450_Gyro gyro, int leftPort, int rightPort)
	{
		super(leftPort, rightPort);
		this.leftSide = super.leftSide;
		this.rightSide = super.rightSide;
		this.gyro = gyro;
	}
	
	private void updateGyro()
	{
		currentValue = gyro.getAngle();
	}
	
	public void updateSetPoint()
	{
		setPoint = gyro.getAngle();
	}

	@Override
	public void drive(double x, double y) {
		updateGyro();
		double difference = setPoint-currentValue;
		double motorPower = difference/180;
		leftSide.set(y+motorPower);
		rightSide.set(y-motorPower);
	}
}
