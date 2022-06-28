import java.util.*;
import java.text.DecimalFormat;
import java.lang.Math;
import java.lang.String;
import java.io.*;
public class airport {

    private static int clock = 0;

    public static void main(String[]args) throws CloneNotSupportedException, IOException
    {
        Queue legQueue = new Queue();       //queue of each leg of pattern
        runway runwayArray[] = new runway[10];          //array of runways --- CHANGE??????
        plane planeArray[] = new plane[10];    //array of plane classes

        makeRunway(runwayArray);
        makePlane(planeArray, runwayArray[0]);
        run(legQueue, planeArray, runwayArray);
    }

    public static void makeQueue(Queue legQueue){ 
        //method to build queue of each leg in pattern

        legQueue.enqueue("45");
        legQueue.enqueue("downwind");
        legQueue.enqueue("abeam");
        legQueue.enqueue("base");
        legQueue.enqueue("final");
        legQueue.enqueue("upwind");
        legQueue.enqueue("crosswind");
    }

    public static void makeRunway(runway [] runwayArray){           ///edit to no runway array and one runway?
        //method to build array of runway classes from user input
        //hardcode instead of user input?

        Scanner in = new Scanner(System.in);
        int runway = 0;
        int WindMagnitude = 0;
        int WindDirection = 0;

        
        System.out.println("Enter the runway in use: "); //Check for Valid Runway Input
        do {
            runway = in.nextInt();
        } while (runway != 29 && runway != 36 && runway != 18 && runway != 11);

        System.out.println("Enter a wind direction. This can be as high as 360 degrees, or as low as 1 degree."); //Wind Direction
        do {
            WindDirection = in.nextInt();
        } while (WindDirection < 1 || WindDirection > 360);
        

        System.out.println("Enter a magnitude for wind. The wind can be as high as 25, or as low as 0.");// Wind Speed
        do {
            WindMagnitude = in.nextInt();
        } while (WindMagnitude < 0 || WindMagnitude > 30);

        in.close();

        System.out.println("");


        int runwayLength=0; double landX=0; double landY=0; double takeOffX=0; double takeOffY=0;

        switch(runway) //initialize runway in space based off of runway in use
        {
            case 36:
                runwayLength = 5264;
                landX = 0; landY = 4000;
                takeOffX = 5264; takeOffY = 4000;
                break;
            case 18:
                runwayLength = 5264;
                landX = 5264; landY = 4000;
                takeOffX = 0; takeOffY = 4000;
                break;
            case 11: 
                runwayLength = 4000;
                landX = 5264; landY = 4000;
                takeOffX = 3895.92; takeOffY = 241.23;
                break;
            case 29:
                runwayLength = 4000;
                landX = 3895.92; landY = 241.23;
                takeOffX = 5264; takeOffY = 4000;
                break;
            default:
                System.out.println("invalid runway. please enter 36, 18, 29, or 11");
        }
        runway myRunway = new runway(runway, runwayLength, takeOffX, takeOffY, landX, landY, WindMagnitude, WindDirection);
        runwayArray[0] = myRunway;

    }

    public static void makePlane(plane [] planeArray, runway myRunway){
        //method to add a plane to the plane array
        //adds name, x and y position, and velocity

        double x = myRunway.start45X;  //start on 45
        double y = myRunway.start45Y;
        double Vno = 212.664; double Vso = 74.264; double Vfe = 145.152;    //c172
        double Vno2 = 175.532; double Vso2 = 70.888; double Vfe2 = 150.215; //c150
        double velocity = 150.0;
        double heading = 0;

        switch (myRunway.runway) //initialize runway in space based off of runway in use
        {
            case 18:
            heading = 45;
            case 11:
            heading = 135;
            case 36:
            heading = 225;
            case 29:
            heading = 315;

        }

        /*Scanner in = new Scanner(System.in);
        if(in.nextLine()=="c172")
            Vno = 212.664; Vso = 74.264; Vfe = 145.152;
        if(in.nextLine() == "c150")
            Vno2 = 175.532; Vso2 = 70.888; Vfe2 = 150.215;*/

        planeArray[0] = new plane("UAV", x, y, Vno, Vso, Vfe, velocity, heading);
        planeArray[1] = new plane("plane2", x, y, Vno2, Vso2, Vfe2, velocity, heading);

    }

    public static void run(Queue legQueue, plane[] planeArray, runway[] runwayArray) throws CloneNotSupportedException, IOException
    {
        DecimalFormat df = new DecimalFormat( "#0.000");
        String x2 = ""; String y2 = ""; String z2 = "";
        File file = new File(String.valueOf(runwayArray[0].runway) + "_plot.dat");
        String formatStr = "%-15s %-15s %-15s %-15s %-15s %-15s%n";
        try {
            FileWriter fw = new FileWriter(file);
            BufferedWriter b = new BufferedWriter(fw);
            b.write(String.format(formatStr, "#x", "y", "z", "x2", "y2", "z2"));            
            b.newLine();
            b.close();
        } catch (IOException e) {
            e.getMessage();
        }

        makeQueue(legQueue);
        Queue clone = (Queue) legQueue.clone();

        while(clock <= 230)
        {
            String x = String.valueOf(df.format(planeArray[0].x)); String y = String.valueOf(df.format(planeArray[0].y)); String z = String.valueOf(df.format(planeArray[0].z));
            try {
                FileWriter fw = new FileWriter(runwayArray[0].runway + "_plot.dat", true);
                BufferedWriter b = new BufferedWriter(fw);
                b.write(String.format(formatStr, x, y, z, x2, y2, z2));        
                b.newLine();
                b.close();
            } catch (IOException e) {
                e.getMessage();
            }
            changeLeg(legQueue, planeArray[0], runwayArray[0]);

            if(clock >= 500)   //second plane 100 seconds behind
            {
                changeLeg(clone, planeArray[1], runwayArray[0]);
                x2 = String.valueOf(df.format(planeArray[1].x)); y2 = String.valueOf(df.format(planeArray[1].y)); z2 = String.valueOf(df.format(planeArray[1].z));
            } 

            clock++;
        }
    }

    public static void changeLeg(Queue legQueue, plane myplane, runway myRunway){
        //method to switch legs in the pattern once the plane has reached the correct position


        if(legQueue.isEmpty())
        {
            makeQueue(legQueue);    //start over to keep cycling through
            legQueue.dequeue();
            increasePos(legQueue, myRunway, myplane, myRunway);
        }
        else if(passed(myplane, myRunway, legQueue) && legQueue.front() == "45") //if done with 45
        {
            double d = Math.sqrt(( /*distance from end of 45 to where we are now, end of 45 z = 1000*/
                Math.pow(myRunway.end45X - myplane.x,2) + Math.pow(myRunway.end45Y - myplane.y, 2))+ Math.pow(1000 - myplane.z, 2));  

            newPoint(myRunway.end45X, myRunway.end45Y, 1000, myRunway.abeamX, myRunway.abeamY, 1000, d, myplane, myRunway.WindMagnitude, myRunway.WindDirection);
            legQueue.dequeue();
            increasePos(legQueue,myRunway, myplane, myRunway);
        }

        else if(passed(myplane, myRunway, legQueue) && legQueue.front() == "downwind") // if done with downwind
        {
            double d = Math.sqrt( /*distance from end of downwind to where we are now, end of dw z = 1000 */
                Math.pow(myRunway.abeamX - myplane.x,2) + Math.pow(myRunway.abeamY - myplane.y, 2)+ Math.pow(1000 - myplane.z, 2));  
            
            newPoint(myRunway.abeamX, myRunway.abeamY,1000, myRunway.baseX , myRunway.baseY, 700, d, myplane, myRunway.WindMagnitude, myRunway.WindDirection);
            legQueue.dequeue();
             increasePos(legQueue, myRunway, myplane, myRunway);
        }

        else if(passed(myplane, myRunway, legQueue) && legQueue.front() == "abeam") // if done with abeam
        {
            double d = Math.sqrt( /*distance from abeam point to where we are now, end of abeam z = 700*/
                Math.pow(myRunway.baseX - myplane.x,2) + Math.pow(myRunway.baseY - myplane.y, 2)+ Math.pow(700 - myplane.z, 2));  

            newPoint(myRunway.baseX, myRunway.baseY, 700, myRunway.finalX, myRunway.finalY, 400, d, myplane, myRunway.WindMagnitude, myRunway.WindDirection);
            legQueue.dequeue();
            increasePos(legQueue, myRunway, myplane, myRunway);
        }

        else if(passed(myplane, myRunway, legQueue) && legQueue.front() == "base") //if done with base
        {
            double d = Math.sqrt( /*distance from end of base to where we are now, end of base z = 400 */
                Math.pow(myRunway.finalX - myplane.x,2) + Math.pow(myRunway.finalY - myplane.y, 2)+ Math.pow(400 - myplane.z, 2));  

            newPoint(myRunway.finalX, myRunway.finalY, 400, myRunway.touchDownX , myRunway.touchDownY, 0, d, myplane, myRunway.WindMagnitude, myRunway.WindDirection);
            legQueue.dequeue();
             increasePos(legQueue, myRunway, myplane, myRunway);
        }
        else if(passed(myplane, myRunway, legQueue) && legQueue.front() == "final") // if done with final
        {
            double d = Math.sqrt( /*distance from end of final to where we are now, end of final z = 0 */
                Math.pow(myRunway.touchDownX - myplane.x,2) + Math.pow(myRunway.touchDownY - myplane.y, 2)+ Math.pow(0 - myplane.z, 2));  

            newPoint(myRunway.touchDownX, myRunway.touchDownY, 0, myRunway.crosswindX , myRunway.crosswindY, 700, d, myplane, myRunway.WindMagnitude, myRunway.WindDirection);

            legQueue.dequeue();  
             increasePos(legQueue, myRunway, myplane, myRunway);
        }
        else if(passed(myplane, myRunway, legQueue) && legQueue.front() == "upwind") // if done with upwind
        { 
            double d = Math.sqrt( /*distance from end of upwind to where we are now, end of upwind z = 700 */
            Math.pow(myRunway.crosswindX - myplane.x,2) + Math.pow(myRunway.crosswindY - myplane.y, 2)+ Math.pow(700 - myplane.z, 2)); 

            newPoint(myRunway.crosswindX, myRunway.crosswindY, 700, myRunway.downwindX , myRunway.downwindY, 1000, d, myplane, myRunway.WindMagnitude, myRunway.WindDirection);               
            legQueue.dequeue();
            increasePos(legQueue, myRunway, myplane, myRunway);
        }
        else if(passed(myplane, myRunway, legQueue) && legQueue.front() == "crosswind")    //if done with crosswind
        {
            double d = Math.sqrt( /*distance from end of crosswind to where we are now, end of crosswind z = 1000 */
            Math.pow(myRunway.downwindX - myplane.x,2) + Math.pow(myRunway.downwindY - myplane.y, 2)+ Math.pow(1000 - myplane.z, 2)); 

            newPoint(myRunway.downwindX, myRunway.downwindY, 1000, myRunway.abeamX , myRunway.abeamY, 1000, d, myplane, myRunway.WindMagnitude, myRunway.WindDirection);     
            legQueue.dequeue(); 
            increasePos(legQueue,myRunway, myplane, myRunway);
        }
        else {
           // System.out.println();
            increasePos(legQueue, myRunway, myplane, myRunway);   //if not done with any leg, increase time and update position
        }
    }

    public static boolean passed(plane myPlane, runway myRunway, Queue legQueue)
    {//boolean to determine if the turn point has been passed
     //called in changeleg method, returns true if turn point has been passed
     // checks each turn direction based off of runway in use

        String leg = (String) legQueue.front();
        boolean s = false;

        if(leg == "45") //check turn point of each leg
        { 
            switch (myRunway.runway) //case for each runway direction
            {
                case 29:
                case 36:
                    if(myPlane.y < myRunway.end45Y)
                        s =  true;
                    break;
                case 11:
                case 18:
                    if(myPlane.y > myRunway.end45Y)
                        s = true;
                    break;
            }
        }    
        else if(leg == "downwind")
        { 
            switch (myRunway.runway)
            {
                case 29:
                case 36:
                    if(myPlane.x < myRunway.abeamX)
                        s = true;
                    break;
                case 11:
                case 18:
                    if(myPlane.x > myRunway.abeamX)
                        s = true;
                    break;
            }
        }       
        else if(leg == "abeam" )
        { 
            switch (myRunway.runway)
            {
                case 29:
                case 36:
                    if(myPlane.x < myRunway.baseX)
                    {
                        s = true;
                    }
                    break;
                case 11:
                case 18:
                    if(myPlane.x > myRunway.baseX)
                        s = true;
                    break;
            }
        }
        else if(leg == "base")
        { 
            switch (myRunway.runway)
            {
                case 29:
                case 36:
                    if(myPlane.y < myRunway.finalY)
                        s = true;

                    break;
                case 11:
                case 18:
                    if(myPlane.y > myRunway.finalY)
                        s = true;
                    break;
            }
        }
        else if(leg == "final")
        { 
            switch (myRunway.runway)
            {
                case 29:
                case 36:
                    if(myPlane.x > myRunway.landX)
                        s = true;
                    break;
                case 11:
                case 18:
                    if(myPlane.x < myRunway.landX)
                        s = true;
                    break;
            }
        }
        else if(leg == "upwind")
        { 
            switch (myRunway.runway)
            {
                case 29:
                case 36:
                    if(myPlane.x > myRunway.crosswindX)
                        s = true;
                    break;
                case 11:
                case 18:
                    if(myPlane.x < myRunway.crosswindX)
                        s = true;
                    break;
            }
        }
        else if(leg == "crosswind")
        { 
            switch (myRunway.runway)
            {
                case 29:
                case 36:
                    if(myPlane.y > myRunway.downwindY)
                        s = true;
                    break;
                case 11:
                case 18:
                    if(myPlane.y < myRunway.downwindY)
                        s = true;    
                    break;
            }
        }
        return s;
    }

    public static void increasePos(Queue legQueue, runway mRunway, plane myplane, runway myRunway) 
    {   //method to update position base on current leg 

        DecimalFormat df = new DecimalFormat( "#0.000");

        if(legQueue.isEmpty())
        {
            return;
        }
        String leg = (legQueue.front()).toString();   
        switch(leg) {           //check what leg currently on, update position and time
            case "45": 
                System.out.println("45");
                System.out.println("clock: " + clock + " x: " + df.format(myplane.x) + " y: " + df.format(myplane.y) + " z: " + df.format(myplane.z) + " " + myplane.heading);
                //  System.out.println("velocity: " + myplane.velocity);
                //start and end height is 1000
                //no acceleration on the 45
                newPoint(myplane.x, myplane.y, myplane.z, myRunway.end45X, myRunway.end45Y, 1000, myplane.velocity, myplane, myRunway.WindMagnitude, myRunway.WindDirection);
                return;

            case "downwind": 
                System.out.println("downwind");
                System.out.println("clock: " + clock + " x: " + df.format(myplane.x) + " y: " + df.format(myplane.y) + " z: " + df.format(myplane.z) + " " + myplane.heading);
                // System.out.println("velocity: " + myplane.velocity);
                //start height 1000 at end of 45, end height 1000 abeam numbers
                myplane.velocity += accel (myplane.x, myRunway.abeamX, myplane.y, myRunway.abeamY, myplane.velocity, myplane.Vfe);  //vfe
                newPoint(myplane.x, myplane.y, myplane.z, myRunway.abeamX, myRunway.abeamY, 1000, myplane.velocity, myplane, myRunway.WindMagnitude, myRunway.WindDirection);
                //windCorrection(myplane.y, myRunway.downwindY, myplane, myRunway.WindMagnitude, myRunway.WindDirection, 1);
                return;

            case "abeam":
                System.out.println("abeam");
                System.out.println("clock: " + clock + " x: " + df.format(myplane.x) + " y: " + df.format(myplane.y) + " z: " + df.format(myplane.z) + " " + myplane.heading);
                // System.out.println("velocity: " + myplane.velocity);
                //start height 1000 abeam numbers, descned to 700
                myplane.velocity += accel (myplane.x, myRunway.baseX, myplane.y, myRunway.baseY, myplane.velocity, (1.4 * myplane.Vso));
                newPoint(myplane.x, myplane.y, myplane.z, myRunway.baseX, myRunway.baseY, 700, myplane.velocity, myplane, myRunway.WindMagnitude, myRunway.WindDirection);
                //windCorrection(myRunway.abeamY, myplane.y, myplane.z, myRunway.baseX, myRunway.baseY, 700, myplane.velocity, myplane, myRunway);
                //myplane.velocity += accel (myplane.x, myRunway.baseX, myplane.y, myRunway.baseY, myplane.velocity, (1.4 * myplane.Vso));
                return;

            case "base":
                System.out.println("base" + " " + myplane.heading);

                //System.out.println("clock: " + clock + " x: " + df.format(myplane.x) + " y: " + df.format(myplane.y) + " z: " + df.format(myplane.z));
                //System.out.println("velocity: " + myplane.velocity);
                myplane.velocity += accel (myplane.x, myRunway.finalX, myplane.y, myRunway.finalY, myplane.velocity, (1.3 * myplane.Vso));
                newPoint(myplane.x, myplane.y, myplane.z, myRunway.finalX, myRunway.finalY, 400, myplane.velocity, myplane, myRunway.WindMagnitude, myRunway.WindDirection);
                //windCorrection(myplane.x, myRunway.finalX, myplane, myRunway.WindMagnitude, myRunway.WindDirection, 0.55);
                return;

            case "final":   //NOT FULL STOP, CHECK LANDING VELOCITY
                System.out.println("final"+ " " + myplane.heading);
                // System.out.println("clock: " + clock + " x: " + df.format(myplane.x) + " y: " + df.format(myplane.y) + " z: " + df.format(myplane.z));
                //System.out.println("velocity: " + myplane.velocity);
                myplane.velocity += accel (myplane.x, myRunway.touchDownX, myplane.y, myRunway.touchDownY, myplane.velocity, (myplane.Vso * .8));
                newPoint(myplane.x, myplane.y, myplane.z, myRunway.touchDownX, myRunway.touchDownY, 0, myplane.velocity, myplane, myRunway.WindMagnitude, myRunway.WindDirection);
                //windCorrection(myplane.y, myRunway.landY, myplane, myRunway.WindMagnitude, myRunway.WindDirection, 0.85);
                return;

            case "upwind":
                System.out.println("upwind"+ " " + myplane.heading);
                //  System.out.println("clock: " + clock + " x: " + df.format(myplane.x) + " y: " + df.format(myplane.y) + " z: " + df.format(myplane.z));
                // System.out.println("velocity: " + myplane.velocity);
                myplane.velocity += accel (myplane.x, myRunway.crosswindX, myplane.y, myRunway.crosswindY, myplane.velocity,  (.8 * myplane.Vno));
                newPoint(myplane.x, myplane.y, myplane.z, myRunway.crosswindX, myRunway.crosswindY, 700, myplane.velocity, myplane, myRunway.WindMagnitude, myRunway.WindDirection);
                //windCorrection(myplane.y, myRunway.crosswindY, myplane, myRunway.WindMagnitude, myRunway.WindDirection, 0.85);
                return;

            case "crosswind":
                System.out.println("crosswind"+ " " + myplane.heading);
                //System.out.println("clock: " + clock + " x: " + df.format(myplane.x) + " y: " + df.format(myplane.y) + " z: " + df.format(myplane.z));
                // System.out.println("velocity: " + myplane.velocity);
                myplane.velocity += accel (myplane.x, myRunway.downwindX, myplane.y, myRunway.downwindY, myplane.velocity, myplane.Vno);
                newPoint(myplane.x, myplane.y, myplane.z, myRunway.downwindX, myRunway.downwindY, 1000, myplane.velocity, myplane, myRunway.WindMagnitude, myRunway.WindDirection);
                //windCorrection(myplane.x, myRunway.downwindX, myplane, myRunway.WindMagnitude, myRunway.WindDirection, 0.55);
                return;
        }
    }

    public static void newPoint(double startX, double startY, double startZ, double endX, double endY, double endZ, double newDistance, plane myplane, double WindMagnitude, double WindDirection)
    { //method to determine new point a certain distance along a vector
      //distance is velocity (distance traveled in one clock pulse)
      //plane position is updated based on what leg currently on
      
        double Vx = endX - startX;
        double Vy = endY - startY;
        double Vz = endZ - startZ;
        double norm = Math.sqrt( Math.pow(Vx, 2) + Math.pow(Vy, 2) + Math.pow(Vz, 2));
        
        double newX = ( newDistance * (Vx / norm) ) + startX;
        double newY = ( newDistance * (Vy / norm) ) + startY;
        double newZ = ( newDistance * (Vz / norm) ) + startZ;

        //double WindX = WindMagnitude * Math.cos(WindDirection * Math.PI / 180);
        //double WindY = WindMagnitude * Math.sin(WindDirection * Math.PI / 180);

        //myplane.x = newX + WindX;
        //myplane.y = newY + WindY;
        //myplane.z = newZ;

        //---------Calculate Heading--------------------------

        double Hx = newX - startX; //P: Hx = 
        double Hy = newY - startY;
        double Hz = newZ - startZ;

        double headingMag = Math.sqrt( Math.pow(Hx, 2) + Math.pow(Hy, 2) + Math.pow(Hz, 2));

        double unitHx = Hx / headingMag;
        double unitHy = Hy / headingMag;
        double unitHz = Hz / headingMag;

        double radians = Math.atan(Hy/Hx);  //Magnitude of Heading in Radians
        double degrees = ((radians * 180) / Math.PI) + 180; //Convert to Degrees

        myplane.heading = degrees;

        //---------Calculate Wind--------------------------

        double windX = WindMagnitude * Math.cos((WindDirection * Math.PI) / 180) + startX; // wind vector X
        double windY = WindMagnitude * Math.sin((WindDirection * Math.PI) / 180) + startY; // wind vector Y

        double wX = windX - startX; 
        double wY = windY - startY;

        double windMag = Math.sqrt( Math.pow(wX, 2) + Math.pow(wY, 2) );

        double unitWindX = wX / windMag;
        double unitWindY = wY / windMag;

        if  (windMag == 0){
            unitWindX = 0;
            unitWindY = 0;
        }

        //---------Calculate NewPoint--------------------------

        myplane.x = newX + (unitWindX * windMag) + (unitHx * myplane.velocity);
        myplane.y = newY + (unitWindY * windMag) + (unitHy * myplane.velocity);
        myplane.z = newZ +                         (unitHz * myplane.velocity);

    }



    public static void windCorrection(){

        double w1, w2, w3; //weight factors
        double newHeading; // = 
        double P, I, D;

        //---------Proportional Calculation--------------------------



    }
        

    public static double accel(double startx, double endx, double starty, double endy, double startv, double endv)
    {
        double d = Math.sqrt(( 
             Math.pow(endx - startx,2) + Math.pow(endy - starty, 2))+ Math.pow(endv - startv, 2));

        double a = ( (Math.pow(endv,2) - Math.pow(startv, 2)) / (2*d) );
        return a;
    }
}
