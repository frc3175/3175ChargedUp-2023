package frc.robot;

import java.io.IOException;

import org.photonvision.PhotonCamera;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPoint;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.apriltag.AprilTagFieldLayout.OriginPosition;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.autos.automodes.Auto;
import frc.robot.commands.*;
import frc.robot.subsystems.*;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
    /* Controllers */
    private final XboxController driver = new XboxController(0);

    /* Camera */   
    private final PhotonCamera photonCamera = new PhotonCamera("OV5647");

    /* Drive Controls */
    private final int translationAxis = XboxController.Axis.kLeftY.value;
    private final int strafeAxis = XboxController.Axis.kLeftX.value;
    private final int rotationAxis = XboxController.Axis.kRightX.value;

    /* Driver Buttons */
    private final JoystickButton zeroGyro = new JoystickButton(driver, XboxController.Button.kX.value);
    private final JoystickButton robotCentric = new JoystickButton(driver, XboxController.Button.kRightBumper.value);
    private final JoystickButton m_trackAprilTag = new JoystickButton(driver, XboxController.Button.kLeftBumper.value);
    //private final JoystickButton m_odometryAlign = new JoystickButton(driver, XboxController.Button.kRightBumper);

    /* Subsystems */
    public static final SwerveDrivetrain m_drivetrain = new SwerveDrivetrain();
    private final PoseEstimatorSubsystem m_poseEstimator = new PoseEstimatorSubsystem(photonCamera, m_drivetrain);
    //private final Limelight m_limelight = new Limelight();

    AprilTagFieldLayout layout;

    /* Commands */
    //private final LimelightAprilTag m_followAprilTag = new LimelightAprilTag(m_limelight, m_drivetrain);


    /** The container for the robot. Contains subsystems, OI devices, and commands. */
    public RobotContainer() {

        m_drivetrain.setDefaultCommand(
            new SwerveDrive(
                m_drivetrain, 
                () -> -driver.getRawAxis(translationAxis), 
                () -> -driver.getRawAxis(strafeAxis), 
                () -> -driver.getRawAxis(rotationAxis), 
                () -> robotCentric.getAsBoolean()
            )
        );

        try {
        layout = AprilTagFieldLayout.loadFromResource(AprilTagFields.k2023ChargedUp.m_resourceFile);
        layout.setOrigin(
          OriginPosition.kRedAllianceWallRightSide);
        } catch (IOException e) {
            DriverStation.reportError("Failed to load AprilTagFieldLayout", e.getStackTrace());
            layout = null;
        }

        // Configure the button bindings
        configureButtonBindings();
        
    }

    /**
     * Use this method to define your button->command mappings. Buttons can be created by
     * instantiating a {@link GenericHID} or one of its subclasses ({@link
     * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
     * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
     */
    private void configureButtonBindings() {

        /* Driver Buttons */
        zeroGyro.onTrue(new InstantCommand(() -> m_drivetrain.zeroGyro()));
        //m_trackAprilTag.whileTrue(m_followAprilTag);

        Pose2d tagPose = layout.getTagPose(2).get().toPose2d();
    
            m_trackAprilTag.whileTrue(new OdometryAlign(m_drivetrain, 
                                                        new PathConstraints(1, 1), 
                                                        new PathPoint(
                                                            new Translation2d(tagPose.getX(), 
                                                                              tagPose.getY()), 
                                                            tagPose.getRotation()), 
                                                        m_poseEstimator));


       

    }

    public Command getAutonomousCommand() {

        return Auto.exampleAuto();

    }

}