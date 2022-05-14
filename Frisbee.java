import java.lang.Math; import java.io.*;
/**
 * The class Frisbee contains the method simulate which uses Euler’s
 * method to calculate the position and the velocity of a frisbee in
 * two dimensions.
 *
 * @OriginalAuthor Vance Morrison
 * @OriginalVersion March 4, 2005
 *
 * @modifiedby Matthew Magro
 * @CurrentVersion May 14th, 2022
 *
 */

class FrisbeeTest{
    private static double x; //The x position of the frisbee.
    private static double y; //The y position of the frisbee.
    private static double vx; //The x velocity of the frisbee.
    private static double vy; //The y velocity of the frisbee.
    private static final double g = -9.81;//The acceleration of gravity (m/s^2).
    private static final double m = 0.069; //The mass of a standard frisbee in kilograms.
    private static final double RHO = 1.23; //The density of air in kg/m^3.
    private static final double AREA = 0.0102608; //The area of a standard Vex spin up disc in meters squared.
    private static final double CL0 = 0.1; //The lift coefficient at alpha = 0.
    private static final double CLA = 1.4; //The lift coefficient dependent on alpha.
    private static final double CD0 = 0.08; //The drag coefficent at alpha = 0.
    private static final double CDA = 2.72; //The drag coefficient dependent on alpha.
    private static final double ALPHA0 = -4;
    private static int instance;
    private static String file;

    /**
     * A method that uses Euler’s method to simulate the flight of a frisbee in
     * two dimensions, distance and height (x and y, respectively).
     *
     */
    public static void simulate(double y0, double vx0, double vy0, double a, double deltaT)
    {
        double alpha = a;
        instance++;
        file = "disc" + instance + ".csv";




        //Initial position x = 0.
        x = 0;
        //Initial position y = y0.
        y = y0;
        //Initial x velocity vx = vx0.
        vx = vx0;
        //Initial y velocity vy = vy0.
        vy = vy0;

        final double originalAlpha = alpha;
        final double originalVectorAngle = Math.toDegrees(Math.atan(vy/vx));


        try{
            //A PrintWriter object to write the output to a spreadsheet.

            PrintWriter pw = new PrintWriter(new BufferedWriter (new FileWriter(file)));
            //A loop index to monitor the simulation steps.
            int k = 0;
            //A while loop that performs iterations until the y position
            //reaches zero (i.e. the frisbee hits the ground).
            while(y>0){
                //The change in velocity in the y direction obtained setting the
                //net force equal to the sum of the gravitational force and the
                //lift force and solving for delta v.

                double cl = CL0 + CLA*alpha*Math.PI/180;
                //Calculation of the lift coefficient using the relationship given
                // by S. A. Hummel.


                double cd = CD0 + CDA*Math.pow((alpha-ALPHA0)*Math.PI/180,2);
                //Calculation of the drag coefficient (for Prandtl’s relationship)
                //using the relationship given by S. A. Hummel.

                double deltavy = (RHO*Math.pow(vx,2)*AREA*cl/2/m+g)*deltaT;

                //The change in velocity in the x direction, obtained by
                //solving the force equation for delta v. (The only force //present is the drag force).
                double deltavx = -RHO*Math.pow(vx,2)*AREA*cd*deltaT;

                //The new positions and velocities are calculated using
                //simple introductory mechanics.
                vx = vx + deltavx;
                vy = vy + deltavy;
                x = x + vx*deltaT;
                y = y + vy*deltaT;
                //Only the output from every tenth iteration will be sent
                //to the spreadsheet so as to decrease the number of data points.
                double currentVectorAngle = Math.toDegrees(Math.atan(vy/vx));
                alpha = (originalAlpha+originalVectorAngle)-currentVectorAngle;
                if(k%10 == 0){
                    pw.print(x + "," + y + "," + vx);
                    //System.out.println(x + "," + y + "," + vx);
                    pw.println();
                    pw.flush();
                }
                k++;
            }

            pw.close();
        }
        catch(Exception e){
            System.out.println("Error, file " + file + " is in use.");}
    }
}

class calcConstants{
    double launchAngleDegree;
    double launchAngleRadian;
    double velocityVector;
    double xVelocity;
    double yVelocity;
    double launchHeight;
    double angleOfAttack;

    public calcConstants(double lA, double vV, double lH, double aOA){
        //launch angle in degrees
        //total velocity vector in meter per second
        //launchHeight in inches
        launchAngleDegree = lA;
        velocityVector = vV;
        launchHeight = lH;
        angleOfAttack = aOA;

        launchAngleRadian =  Math.toRadians(launchAngleDegree);
        yVelocity = velocityVector * Math.sin(launchAngleRadian); //calcs the yVelocity with trig
        xVelocity = velocityVector * Math.cos(launchAngleRadian); //calcs the xVelocity with trig
        launchHeight = (launchHeight/12)/3.281; //conversion to feet from inches, then conversion from feet to meters
    }

    public double getYVector(){

        return yVelocity;
    }
    public double getXVector(){

        return xVelocity;
    }

    public double getLaunchAngle(){
        return launchAngleDegree;
    }

    public double getLaunchHeight(){

        return launchHeight;
    }

    public void runSim(){
        FrisbeeTest.simulate(launchHeight, xVelocity, yVelocity, angleOfAttack, 0.001);
        //simulate(double y0, double vx0, double vy0, double a, double deltaT) <- header for simulate method
        // y0 is the initial y position in meters
        // vx0 is the initial velocity in the x direction of the disc in meters per second
        // vy0 is the initial velocity in the y direction
        // alpha is the initial angle of attack of the disc
        // not sure what deltaT is, but it was a constant set in the MIT paper
    }

}


public class Frisbee{
    public static void main(String args[]){
        //launch angle in degrees: lA
        //total velocity vector in meter per second: vV
        //launchHeight in inches: lH
        calcConstants disc1 = new calcConstants(30, 7, 1, 0);
        disc1.runSim();

        calcConstants disc2 = new calcConstants(0, 20, 16, 50);
        disc2.runSim();

        //Many objects with different parameters can be created to test different configurations



    }

}


