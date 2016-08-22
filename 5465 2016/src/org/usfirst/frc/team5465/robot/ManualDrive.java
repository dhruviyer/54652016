package org.usfirst.frc.team5465.robot;

import edu.wpi.first.wpilibj.*;

public class ManualDrive extends RobotDrive
{
	private Victor leftSide;
	private Victor rightSide;

	public ManualDrive(int leftPort, int rightPort) 
	{
		super(leftPort, rightPort);
		this.leftSide = super.leftSide;
		this.rightSide = super.rightSide;
	}
	
	public void drive(double x, double y)
	{
		leftSide.set(y+x);
		rightSide.set(y-x);
	}
}
