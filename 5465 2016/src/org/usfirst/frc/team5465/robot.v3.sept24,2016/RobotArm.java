package org.usfirst.frc.team5465.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;

public class RobotArm 
{
	Solenoid forwardSolenoid = new Solenoid(0);
	Solenoid reverseSolenoid = new Solenoid(1);
	
	private CANTalon armTalon;
	
	public RobotArm()
	{
		armTalon = new CANTalon(0);
	}
	
	public void moveRobotArm(double y)
	{
		armTalon.set(y);
	}
	
	public void stopRobotArm()
	{
		armTalon.set(0);
	}
	public double getRobotArmMotorValue()
	{
		return 5;
	}
	
	public boolean getPiston()
	{
		return forwardSolenoid.get();
	}
	
	public void actuate(boolean out)
	{
		if(out)
		{
			forwardSolenoid.set(true);
			reverseSolenoid.set(false);
		}
		else
		{
			forwardSolenoid.set(false);
			reverseSolenoid.set(true);
		}
	}
}