/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.OmniDrive;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private Command m_autonomousCommand;
  private Command m_teleopCommand;
  private RobotContainer m_robotContainer;
  private Notifier m_follower, m_follower_arm, m_followerAstar;

  /**
  * This function is run when the robot is first started up and should be used for any
  * initialization code.
  */
 @Override
 public void robotInit() {
   // Instantiate our RobotContainer. 
   m_robotContainer = new RobotContainer();

   RobotContainer.m_omnidrive.gyro.zeroYaw();
   //Run PID in different thread at higher rate
   if (Constants.PID_THREAD ) {
    m_follower = new Notifier(() -> { RobotContainer.m_omnidrive.doPID(); });
    m_follower.startPeriodic(Constants.PID_DT);
  }
  if (Constants.PID_THREAD_ARM ) {
    m_follower_arm = new Notifier(() -> { RobotContainer.m_arm.doPID(); });
    m_follower_arm.startPeriodic(Constants.PID_DT_ARM);
  }
  m_followerAstar = new Notifier(() -> { RobotContainer.m_Astar.AstarProcess(); });
    m_followerAstar.startPeriodic(0.04);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();

    if (RobotContainer.m_sensor.getSwitch()==true) {
      CommandScheduler.getInstance().enable();
    }
  }

  /**
   * This function is called once each time the robot enters Disabled mode.
   */
  @Override
  public void disabledInit() {
  }


  @Override
  public void disabledPeriodic() {
  }

  /**
   * This autonomous runs the autonomous command selected by your {@link RobotContainer} class.
   */
  @Override
  public void autonomousInit() {
    //Initialise all subsystems here to initial state.
    //This allowed robot to rerun properly
    RobotContainer.m_arm.initialize();
    RobotContainer.m_omnidrive.initialise();
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
    //Initialise all subsystems here to initial state.
    //This allowed robot to rerun properly
    RobotContainer.m_arm.initialize();
    RobotContainer.m_omnidrive.initialise();

    m_teleopCommand = m_robotContainer.getTeleopCommand();

    // schedule the autonomous command (example)
    if (m_teleopCommand != null) {
      m_teleopCommand.schedule();
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
  }

  @Override
  public void testInit() {
    //Initialise all subsystems here to initial state.
    //This allowed robot to rerun properly
    RobotContainer.m_arm.initialize();
    RobotContainer.m_omnidrive.initialise();
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }

  /**
   * added here to satisfy the watchdog
   */
  @Override
  public void simulationInit(){
  }
  
  /**
   * added here to satisfy the watchdog
   */ 
  @Override
  public void simulationPeriodic(){
  }
}
