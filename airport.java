import java.util.*;
import java.text.DecimalFormat;
import javax.lang.model.util.ElementScanner6;
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
        makePlane(planeArray, runwayArray);
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
        } while (WindMagnitude < 0 || WindMagnitude > 25);

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
                System.out.println("invalid runway. please enter 36, 18, 29, or 11. Exiting Program");
                System.exit(1);
                
        }
        runway myRunway = new runway(runway, runwayLength, takeOffX, takeOffY, landX, landY, WindMagnitude, WindDirection);
        runwayArray[0] = myRunway;
        //runwayArray[1] = new runway(29, 4000, 5264, 4000, 3895.92, 241.23);
    }

    public static void makePlane(plane [] planeArray, runway runwayArray[]){
        //method to add a plane to the plane array
        //adds name, x and y position, and velocity

        double x = runwayArray[0].start45X;  //start on 45
        double y = runwayArray[0].start45Y;
        double Vso = 74.264; double Vs = 85; double Vfe = 145.152; double Vx = 101; double climbRate = 13; double velocity = Vfe * 1.1;   //c172
        double Vso2 = 70.888; double Vs2 = 82; double Vfe2 = 150.215; double Vx2 = 94; double climbRate2 = 11.2; double velocity2 = Vfe2 * 1.1;    //c150
        double Vso3 = Vso * 1.2; double Vs3 = Vs * 1.2; double Vfe3 = Vfe * 1.2; double Vx3 = 105; double climbRate3 = 14;  double velocity3 = Vfe3 * 1;     //faster c172
        double heading = 0;

        switch (runwayArray[0].runway) //initialize runway in space based off of runway in use
        {
            case 18:
            heading = 45;
                break;
            case 11:
            heading = 115;
                break;
            case 36:
            heading = 225;
                break;
            case 29:
            heading = 295;
                break;
            default:
            System.exit(1);
        }

        planeArray[0] = new plane("c172", x, y, velocity, Vso, Vs, Vfe, Vx, climbRate, heading, "touch and go");     //UAV
        planeArray[1] = new plane("c150", x, y, velocity2, Vso2, Vs2, Vfe2, Vx2, climbRate2, heading, "touch and go"); //plane
        planeArray[2] = new plane("fast c172", x, y, velocity3 , Vso3 , Vs3 , Vfe3, Vx3, climbRate3, heading, "touch and go"); //plane
    }

    public static void run(Queue legQueue, plane[] planeArray, runway[] runwayArray) throws CloneNotSupportedException, IOException
    {
        DecimalFormat df = new DecimalFormat( "#0.000");
        String x = ""; String y = ""; String z = ""; String x2 = ""; String y2 = ""; String z2 = ""; String x3 = ""; String y3 = ""; String z3 = "";
        File file = new File(String.valueOf(runwayArray[0].runway) + "_plot.dat");
        String formatStr = "%-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s%n";
        try {
            FileWriter fw = new FileWriter(file);
            BufferedWriter b = new BufferedWriter(fw);
            b.write(String.format(formatStr, "#x", "y", "z", "x2", "y2", "z2", "x3", "y3", "z3"));            
            b.newLine();
            b.close();
        } catch (IOException e) {
            e.getMessage();
        }

        makeQueue(legQueue);
        Queue clone = (Queue) legQueue.clone();
        Queue clone2 = (Queue) legQueue.clone();


        while(clock <=550)
        {
            try {
                FileWriter fw = new FileWriter(runwayArray[0].runway + "_plot.dat", true);
                BufferedWriter b = new BufferedWriter(fw);
                b.write(String.format(formatStr, x, y, z, x2, y2, z2, x3, y3, z3));        
                b.newLine();
                b.close();
            } catch (IOException e) {
                e.getMessage();
            }

            System.out.println("clock: " + clock);
            //System.out.print(planeArray[1].planeName + " ");    //c150 plane starts first
            //System.out.println("velocity: " + df.format(planeArray[1].velocity));
            x = String.valueOf(df.format(planeArray[1].x));  y = String.valueOf(df.format(planeArray[1].y));  z = String.valueOf(df.format(planeArray[1].z));
            changeLeg(legQueue, planeArray[1], runwayArray[0]);

            
            if(clock >= 15)   //UAV 20 seconds behind
            {
                //System.out.print(planeArray[0].planeName + " ");    //c172 UAV follows
                //System.out.println("velocity: " + df.format(planeArray[0].velocity));
                collide(planeArray, 1, runwayArray[0], clone);
                changeLeg(clone, planeArray[0], runwayArray[0]);
                x2 = String.valueOf(df.format(planeArray[0].x)); y2 = String.valueOf(df.format(planeArray[0].y)); z2 = String.valueOf(df.format(planeArray[0].z));
                double dd = Math.sqrt( 
                    Math.pow(planeArray[1].x - planeArray[0].x,2) + Math.pow(planeArray[1].y - planeArray[0].y, 2)+ Math.pow(planeArray[1].z - planeArray[0].z, 2));
        
                    if(dd <= 200)
                    {
                        System.out.println("collision  with " + planeArray[1].planeName + " occurred at clock " + clock);
                        System.exit(1);
                    }
                    System.out.println();
            } 

            if(clock >= 20)   //other plane 40 seconds behind
            {
                //System.out.print(planeArray[2].planeName + " ");    //fast 172 follows
                //System.out.println("velocity: " + planeArray[2].velocity);
                changeLeg(clone2, planeArray[2], runwayArray[0]);
                collide(planeArray, 2, runwayArray[0], clone);
                x3 = String.valueOf(df.format(planeArray[2].x)); y3 = String.valueOf(df.format(planeArray[2].y)); z3 = String.valueOf(df.format(planeArray[2].z));
                double d = Math.sqrt( 
                    Math.pow(planeArray[2].x - planeArray[0].x,2) + Math.pow(planeArray[2].y - planeArray[0].y, 2)+ Math.pow(planeArray[2].z - planeArray[0].z, 2));
        
                    if(d <= 200)
                    {
                        //System.out.println("collision  with " + planeArray[2].planeName + " occurred at clock " + clock);
                        System.exit(1);
                    }
                    System.out.println();
            }

            System.out.println();
            clock++;
        }
}

    public static void changeLeg(Queue legQueue, plane myplane, runway myRunway){
        //method to switch legs in the pattern once the plane has reached the correct position

    if(myplane.collisionMethod == "none" )
    {
        if(legQueue.isEmpty())
        {
            makeQueue(legQueue);    //start over to keep cycling through
            legQueue.dequeue();
            increasePos(legQueue, myplane, myRunway);
        }

    else if(passed(myplane, myRunway, legQueue) && legQueue.front() == "45") //if done with 45
    {
        double d = Math.sqrt(( /*distance from end of 45 to where we are now, end of 45 z = 1000*/
                   Math.pow(myRunway.end45X - myplane.x,2) + Math.pow(myRunway.end45Y - myplane.y, 2))+ Math.pow(1000 - myplane.z, 2));
        newPoint(myRunway.end45X, myRunway.end45Y, 1000, myRunway.abeamX, myRunway.abeamY, 1000, d, myplane, myRunway.WindMagnitude, myRunway.WindDirection);
        legQueue.dequeue();
        switch (myRunway.runway){
            case 36:
            myplane.heading = 180;
            case 18:
            myplane.heading = 360;
            case 11:
            myplane.heading = 70;
            case 29:
            myplane.heading = 250;
        increasePos(legQueue, myplane, myRunway);
        }
    }

    else if(passed(myplane, myRunway, legQueue) && legQueue.front() == "downwind") // if done with downwind
    {
        double d = Math.sqrt( /*distance from end of downwind to where we are now, end of dw z = 1000 */
                   Math.pow(myRunway.abeamX - myplane.x,2) + Math.pow(myRunway.abeamY - myplane.y, 2)+ Math.pow(1000 - myplane.z, 2));  
        newPoint(myRunway.abeamX, myRunway.abeamY,1000, myRunway.baseX , myRunway.baseY, 700, d, myplane, myRunway.WindMagnitude, myRunway.WindDirection);
        switch (myRunway.runway){
            case 36:
            myplane.heading = 180;
            case 18:
            myplane.heading = 360;
            case 11:
            myplane.heading = 70;
            case 29:
            myplane.heading = 250;
        }
        legQueue.dequeue();
        increasePos(legQueue, myplane, myRunway);
    }

    else if(passed(myplane, myRunway, legQueue) && legQueue.front() == "abeam") // if done with abeam
    {
        double d = Math.sqrt( /*distance from abeam point to where we are now, end of abeam z = 700*/
                   Math.pow(myRunway.baseX - myplane.x,2) + Math.pow(myRunway.baseY - myplane.y, 2)+ Math.pow(700 - myplane.z, 2));  
        newPoint(/*myRunway.baseX, myRunway.baseY, 700, */ myplane.x, myplane.y, myplane.z, myRunway.finalX, myRunway.finalY, 400, d, myplane, myRunway.WindMagnitude, myRunway.WindDirection);
        switch (myRunway.runway){
            case 36:
            myplane.heading = 270;
            case 18:
            myplane.heading = 90;
            case 11:
            myplane.heading = 160;
            case 29:
            myplane.heading = 340;
        }
        legQueue.dequeue();
        increasePos(legQueue, myplane, myRunway);
    }

    else if(passed(myplane, myRunway, legQueue) && legQueue.front() == "base") //if done with base
    {
        double d = Math.sqrt( /*distance from end of base to where we are now, end of base z = 400 */
                   Math.pow(myRunway.finalX - myplane.x,2) + Math.pow(myRunway.finalY - myplane.y, 2)+ Math.pow(400 - myplane.z, 2));  
        newPoint(myRunway.finalX, myRunway.finalY, 400, myRunway.touchDownX , myRunway.touchDownY, 0, d, myplane, myRunway.WindMagnitude, myRunway.WindDirection);
        switch (myRunway.runway){
            case 36:
            myplane.heading = 360;
            case 18:
            myplane.heading = 180;
            case 11:
            myplane.heading = 250;
            case 29:
            myplane.heading = 70;
        }
        legQueue.dequeue();
        increasePos(legQueue, myplane, myRunway);
    }

    else if(passed(myplane, myRunway, legQueue) && legQueue.front() == "final") // if done with final
    {
        double d = Math.sqrt( /*distance from end of final to where we are now, end of final z = 0 */
                   Math.pow(myRunway.touchDownX - myplane.x,2) + Math.pow(myRunway.touchDownY - myplane.y, 2)+ Math.pow(0 - myplane.z, 2));  
        newPoint(myRunway.touchDownX, myRunway.touchDownY, 0, myRunway.crosswindX , myRunway.crosswindY, 700, d, myplane, myRunway.WindMagnitude, myRunway.WindDirection);
        switch (myRunway.runway){
            case 36:
            myplane.heading = 360;
            case 18:
            myplane.heading = 180;
            case 11:
            myplane.heading = 250;
            case 29:
            myplane.heading = 70;
        }
        legQueue.dequeue();
        increasePos(legQueue, myplane, myRunway);
    }

    else if(passed(myplane, myRunway, legQueue) && legQueue.front() == "upwind") // if done with upwind
    { 
        double d = Math.sqrt( /*distance from end of upwind to where we are now, end of upwind z = 700 */
                   Math.pow(myRunway.crosswindX - myplane.x,2) + Math.pow(myRunway.crosswindY - myplane.y, 2)+ Math.pow(700 - myplane.z, 2)); 
        newPoint(myRunway.crosswindX, myRunway.crosswindY, 700, myRunway.downwindX , myRunway.downwindY, 1000, d, myplane, myRunway.WindMagnitude, myRunway.WindDirection);               
        switch (myRunway.runway){
            case 36:
            myplane.heading = 90;
            case 18:
            myplane.heading = 270;
            case 11:
            myplane.heading = 340;
            case 29:
            myplane.heading = 160;
        }   
        legQueue.dequeue();
        increasePos(legQueue, myplane, myRunway);
    }

    else if(passed(myplane, myRunway, legQueue) && legQueue.front() == "crosswind")    //if done with crosswind
    {
        double d = Math.sqrt( /*distance from end of crosswind to where we are now, end of crosswind z = 1000 */
                   Math.pow(myRunway.downwindX - myplane.x,2) + Math.pow(myRunway.downwindY - myplane.y, 2)+ Math.pow(1000 - myplane.z, 2)); 
        newPoint(myRunway.downwindX, myRunway.downwindY, 1000, myRunway.abeamX , myRunway.abeamY, 1000, d, myplane, myRunway.WindMagnitude, myRunway.WindDirection);     
        switch (myRunway.runway){
            case 36:
            myplane.heading = 180;
            case 18:
            myplane.heading = 360;
            case 11:
            myplane.heading = 70;
            case 29:
            myplane.heading = 250;
        }                    
        legQueue.dequeue(); 
        increasePos(legQueue, myplane, myRunway);
    }

    else {
    increasePos(legQueue, myplane, myRunway);   //if not done with any leg, increase time and update position
    }
}

    else if(myplane.collisionMethod == "360")
    {
        System.out.println("360: ");
        double angle = myplane.maneuverArray[2];
        double L = myplane.velocity;
        angle -= L/500;
        
        myplane.maneuverArray[2] = angle;
        myplane.x = myplane.maneuverArray[0] + 500 * Math.cos(angle);
        myplane.y = myplane.maneuverArray[1] + 500 * Math.sin(angle);
        System.out.println("angle: " + angle);
        if(myRunway.runway == 11 || myRunway.runway == 18)
        {
            if(angle - 5* (L/500)<= -(2*(Math.PI )))
            myplane.collisionMethod = "none";     
        }
        else
        {
            if(angle + 2*(L/500)<= -(2*(Math.PI )))
            myplane.collisionMethod = "none";
        }
    }

    else if(myplane.collisionMethod == "start360")
    {
        System.out.println("start 360: " + myplane.x + " " + myplane.y);

        increasePos(legQueue, myplane, myRunway);

        double x = myplane.x - myplane.prevX;
        double y = myplane.y - myplane.prevY;

        double Vx = y;
        double Vy = -x;

        double norm = Math.sqrt( Math.pow(Vx, 2) + Math.pow(Vy, 2));
        double centerX = ( 500 * (Vx / norm) ) + myplane.x;    //center of the circle coordinates
        double centerY = ( 500 * (Vy / norm) ) + myplane.y;
        double angle =  Math.atan2(myplane.y - centerY, myplane.x - centerX);   //angle from current point, center, to new point along circle
        myplane.maneuverArray[0] = centerX; myplane.maneuverArray[1] = centerY; myplane.maneuverArray[2] = angle;

        System.out.println("center: " + centerX + " " + centerY);
        System.out.println("angle: " + angle);

        double L = myplane.velocity;
        angle = angle - L/500;
        angle = 2*(Math.PI)+angle;
        myplane.x = centerX + 500 * Math.cos(angle);
        myplane.y = centerY + 500 * Math.sin(angle);
        myplane.collisionMethod = "360";
    }
    else {
        increasePos(legQueue, myplane, myRunway);   //if not done with any leg, increase time and update position
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

    public static void increasePos(Queue legQueue, plane myplane, runway myRunway) 
    {   //method to update position base on current leg 

        DecimalFormat df = new DecimalFormat( "#0.000");

        if(legQueue.isEmpty())
        {
            return;
        }
        String leg = (legQueue.front()).toString();   
        switch(leg) {           //check what leg currently on, update position and time
            case "45": 
                //System.out.println("45" + " " + myplane.heading);
                //System.out.println("clock: " + clock + " x: " + df.format(myplane.x) + " y: " + df.format(myplane.y) + " z: " + df.format(myplane.z));
                //System.out.println("velocity: " + myplane.velocity);
                //start and end height is 1000
                //no acceleration on the 45
                if(myplane.collisionMethod == "ascend")
                {
                    double d  = Math.sqrt( Math.pow(myplane.x - myRunway.end45X,2)
                    + Math.pow(myplane.y - myRunway.end45Y, 2)
                    + Math.pow(myplane.z - 1000, 2));
                    double time = d/myplane.Vx;
                    double endZ = (myplane.climbRate * time) + myplane.z;
                    double vv = endZ - myplane.z;
                    //System.out.println("ascended: " + vv);
                    newPoint(myplane.x, myplane.y, myplane.z, myRunway.end45X, myRunway.end45Y, endZ, myplane.velocity, myplane, myRunway.WindMagnitude, myRunway.WindDirection);
                }
                else
                {
                myplane.prevX = myplane.x; myplane.prevY = myplane.y; myplane.prevZ = myplane.z;
                newPoint(myplane.x, myplane.y, myplane.z, myRunway.end45X, myRunway.end45Y, 1000, myplane.velocity, myplane, myRunway.WindMagnitude, myRunway.WindDirection);
                windCorrection(myplane, myRunway.WindDirection, myRunway.WindMagnitude, 295, legQueue, myRunway);  //RW 36: 225 || RW 18: 45 || RW 11: 115 || RW 29: 295
                }
                return;

            case "downwind": 
                //System.out.println("downwind" + " " + myplane.heading );
                //System.out.println("clock: " + clock + " x: " + df.format(myplane.x) + " y: " + df.format(myplane.y) + " z: " + df.format(myplane.z));
                // System.out.println("velocity: " + myplane.velocity);
                //start height 1000 at end of 45, end height 1000 abeam numbers
                //myplane.prevX = myplane.x; myplane.prevY = myplane.y; myplane.prevZ = myplane.z;
                if(myplane.collisionMethod == "ascend")
                {
                    double d  = Math.sqrt( Math.pow(myplane.x - myRunway.abeamX,2)
                    + Math.pow(myplane.y - myRunway.abeamY, 2)
                    + Math.pow(myplane.z - 1000, 2));
                    double time = d/myplane.Vx;
                    double endZ = (myplane.climbRate * time) + myplane.z;
                    //System.out.println("end z : " + endZ);
                    double vv = endZ - myplane.z;
                    //System.out.println("ascended: " + vv);
                    newPoint(myplane.x, myplane.y, myplane.z, myRunway.abeamX, myRunway.abeamY, endZ, myplane.velocity, myplane, myRunway.WindMagnitude, myRunway.WindDirection);
                }
                else 
                {
                myplane.prevX = myplane.x; myplane.prevY = myplane.y; myplane.prevZ = myplane.z;
                myplane.velocity += accel (myplane.x, myRunway.abeamX, myplane.y, myRunway.abeamY, myplane.velocity, myplane.Vfe);  //86 kts
                newPoint(myplane.x, myplane.y, myplane.z, myRunway.abeamX, myRunway.abeamY, 1000, myplane.velocity, myplane, myRunway.WindMagnitude, myRunway.WindDirection);
                windCorrection(myplane, myRunway.WindDirection, myRunway.WindMagnitude, 250, legQueue, myRunway);  //RW 36:  || RW 18: 360 || RW 11: 70 || RW 29: 250
                }
                return;

            case "abeam":
                //System.out.println("abeam" + " " + myplane.heading);
                //System.out.println("clock: " + clock + " x: " + df.format(myplane.x) + " y: " + df.format(myplane.y) + " z: " + df.format(myplane.z));
                // System.out.println("velocity: " + myplane.velocity);
                //start height 1000 abeam numbers, descned to 700
                //myplane.prevX = myplane.x; myplane.prevY = myplane.y; myplane.prevZ = myplane.z;
                if(myplane.collisionMethod == "ascend")
                {
                    double d  = Math.sqrt( Math.pow(myplane.x - myRunway.baseX,2)
                    + Math.pow(myplane.y - myRunway.baseY, 2)
                    + Math.pow(myplane.z - 1000, 2));
                    double time = d/myplane.Vx;
                    double endZ = (myplane.climbRate * time) + myplane.z;                  
                    double vv = endZ - myplane.z;
                    //System.out.println("ascended: " + vv);
                    newPoint(myplane.x, myplane.y, myplane.z, myRunway.baseX, myRunway.baseY, endZ, myplane.velocity, myplane, myRunway.WindMagnitude, myRunway.WindDirection);
                }
                else 
                {
                myplane.prevX = myplane.x; myplane.prevY = myplane.y; myplane.prevZ = myplane.z;
                myplane.velocity += accel (myplane.x, myRunway.baseX, myplane.y, myRunway.baseY, myplane.velocity, (.9 * myplane.Vfe)); //77 kts
                newPoint(myplane.x, myplane.y, myplane.z, myRunway.baseX, myRunway.baseY, 700, myplane.velocity, myplane, myRunway.WindMagnitude, myRunway.WindDirection);
                windCorrection(myplane, myRunway.WindDirection, myRunway.WindMagnitude, 250, legQueue, myRunway);  //RW 36: 225 || RW 18: 360 || RW 11: 70 || RW 29: 250
                }
                return;

            case "base":
                //System.out.println("base" + " " + myplane.heading);
                //System.out.println("clock: " + clock + " x: " + df.format(myplane.x) + " y: " + df.format(myplane.y) + " z: " + df.format(myplane.z));
                //System.out.println("velocity: " + myplane.velocity);
                //myplane.prevX = myplane.x; myplane.prevY = myplane.y; myplane.prevZ = myplane.z;
                if(myplane.collisionMethod == "ascend")
                 {
                    double d  = Math.sqrt( Math.pow(myplane.x - myRunway.finalX,2)
                    + Math.pow(myplane.y - myRunway.finalY, 2)
                    + Math.pow(myplane.z - 1000, 2));
                    double time = d/myplane.Vx;
                    double endZ = (myplane.climbRate * time) + myplane.z;
                    double vv = endZ - myplane.z;
                    //System.out.println("ascended: " + vv);
                    newPoint(myplane.x, myplane.y, myplane.z, myRunway.finalX, myRunway.finalY, endZ, myplane.velocity, myplane, myRunway.WindMagnitude, myRunway.WindDirection);
                }
                else 
                {
                myplane.prevX = myplane.x; myplane.prevY = myplane.y; myplane.prevZ = myplane.z;
                myplane.velocity += accel (myplane.x, myRunway.finalX, myplane.y, myRunway.finalY, myplane.velocity, (1.6 * myplane.Vso));  //70 kts
                newPoint(myplane.x, myplane.y, myplane.z, myRunway.finalX, myRunway.finalY, 400, myplane.velocity, myplane, myRunway.WindMagnitude, myRunway.WindDirection);
                windCorrection(myplane, myRunway.WindDirection, myRunway.WindMagnitude, 340, legQueue, myRunway);  //RW 36: 225 || RW 18: 90 || RW 11: 160 || RW 29: 340
                }
                return;

            case "final":   //NOT FULL STOP, CHECK LANDING VELOCITY
                //System.out.println("final" + " " + myplane.heading);
                // System.out.println("clock: " + clock + " x: " + df.format(myplane.x) + " y: " + df.format(myplane.y) + " z: " + df.format(myplane.z));
                //System.out.println("velocity: " + myplane.velocity);
                //myplane.prevX = myplane.x; myplane.prevY = myplane.y; myplane.prevZ = myplane.z;
                if(myplane.collisionMethod == "ascend")
                 {
                    double d  = Math.sqrt( Math.pow(myplane.x - myRunway.touchDownX,2)
                    + Math.pow(myplane.y - myRunway.touchDownY, 2)
                    + Math.pow(myplane.z - 1000, 2));
                    double time = d/myplane.Vx;
                    double endZ = (myplane.climbRate * time) + myplane.z;
                    double vv = endZ - myplane.z;
                    System.out.println("ascended: " + vv);
                    newPoint(myplane.x, myplane.y, myplane.z, myRunway.touchDownX, myRunway.touchDownY, endZ, myplane.velocity, myplane, myRunway.WindMagnitude, myRunway.WindDirection);
                }
                else 
                {
                myplane.prevX = myplane.x; myplane.prevY = myplane.y; myplane.prevZ = myplane.z;
                myplane.velocity += accel (myplane.x, myRunway.touchDownX, myplane.y, myRunway.touchDownY, myplane.velocity, (myplane.Vso)); //44
                newPoint(myplane.x, myplane.y, myplane.z, myRunway.touchDownX, myRunway.touchDownY, 0, myplane.velocity, myplane, myRunway.WindMagnitude, myRunway.WindDirection);
                windCorrection(myplane, myRunway.WindDirection, myRunway.WindMagnitude, 70, legQueue, myRunway);  //RW 36: 225 || RW 18: 180 || RW 11: 250 || RW 29: 70
                }
                return;

            case "upwind":
                //System.out.println("upwind");// + " " + myplane.heading);
                //  System.out.println("clock: " + clock + " x: " + df.format(myplane.x) + " y: " + df.format(myplane.y) + " z: " + df.format(myplane.z));
                // System.out.println("velocity: " + myplane.velocity);
                //myplane.prevX = myplane.x; myplane.prevY = myplane.y; myplane.prevZ = myplane.z;
                if(myplane.collisionMethod == "ascend")
                 {
                    double d  = Math.sqrt( Math.pow(myplane.x - myRunway.crosswindX,2)
                    + Math.pow(myplane.y - myRunway.crosswindY, 2)
                    + Math.pow(myplane.z - 1000, 2));
                    double time = d/myplane.Vx;
                    double endZ = (myplane.climbRate * time) + myplane.z;
                    double vv = endZ - myplane.z;
                    System.out.println("ascended: " + vv);
                    newPoint(myplane.x, myplane.y, myplane.z, myRunway.crosswindX, myRunway.crosswindY, endZ, myplane.velocity, myplane, myRunway.WindMagnitude, myRunway.WindDirection);
                }
                else 
                {
                myplane.prevX = myplane.x; myplane.prevY = myplane.y; myplane.prevZ = myplane.z;
                myplane.velocity += accel (myplane.x, myRunway.crosswindX, myplane.y, myRunway.crosswindY, myplane.velocity,  (.89 * myplane.Vfe));
                newPoint(myplane.x, myplane.y, myplane.z, myRunway.crosswindX, myRunway.crosswindY, 700, myplane.velocity, myplane, myRunway.WindMagnitude, myRunway.WindDirection);
                windCorrection(myplane, myRunway.WindDirection, myRunway.WindMagnitude, 70, legQueue, myRunway);  //RW 36: 225 || RW 18: 180 || RW 11: 250 || RW 29: 70
                }
                return;

            case "crosswind":
                //System.out.println("crosswind");// + " " + myplane.heading);
                //System.out.println("clock: " + clock + " x: " + df.format(myplane.x) + " y: " + df.format(myplane.y) + " z: " + df.format(myplane.z));
                // System.out.println("velocity: " + myplane.velocity);
                //myplane.prevX = myplane.x; myplane.prevY = myplane.y; myplane.prevZ = myplane.z;
                if(myplane.collisionMethod == "ascend")
                 {
                    double d  = Math.sqrt( Math.pow(myplane.x - myRunway.downwindX,2)
                    + Math.pow(myplane.y - myRunway.downwindY, 2)
                    + Math.pow(myplane.z - 1000, 2));
                    double time = d/myplane.Vx;
                    double endZ = (myplane.climbRate * time) + myplane.z;
                    double vv = endZ - myplane.z;
                    System.out.println("ascended: " + vv);
                    newPoint(myplane.x, myplane.y, myplane.z, myRunway.downwindX, myRunway.downwindY, endZ, myplane.velocity, myplane, myRunway.WindMagnitude, myRunway.WindDirection);
                }
                else 
                {
                myplane.prevX = myplane.x; myplane.prevY = myplane.y; myplane.prevZ = myplane.z;
                myplane.velocity += accel (myplane.x, myRunway.downwindX, myplane.y, myRunway.downwindY, myplane.velocity, (myplane.Vfe * 1.1));
                newPoint(myplane.x, myplane.y, myplane.z, myRunway.downwindX, myRunway.downwindY, 1000, myplane.velocity, myplane, myRunway.WindMagnitude, myRunway.WindDirection);
                windCorrection(myplane, myRunway.WindDirection, myRunway.WindMagnitude, 160, legQueue, myRunway);  //RW 36: 225 || RW 18: 270 || RW 11: 340 || RW 29: 160
                }
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

        //---------Calculate Heading--------------------------

        double Hx = newX - startX; //P: Hx = 
        double Hy = newY - startY;
        double Hz = newZ - startZ;

        double headingMag = Math.sqrt( Math.pow(Hx, 2) + Math.pow(Hy, 2) + Math.pow(Hz, 2));

        double unitHx = Hx / headingMag;
        double unitHy = Hy / headingMag;
        double unitHz = Hz / headingMag;

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

        myplane.x = startX + (unitWindX * windMag) + (unitHx * myplane.velocity);
        myplane.y = startY + (unitWindY * windMag) + (unitHy * myplane.velocity);
        myplane.z = startZ +                         (unitHz * myplane.velocity);

    }

    public static void windCorrection(plane myplane, double WindDirection, double WindMagnitude, double expected, Queue legQueue, runway myRunway){

        String leg = (legQueue.front()).toString(); 
    
            double actual = myplane.heading;
            double diff = actual - expected;

                if ((myRunway.runway == 18 || myRunway.runway == 29 || myRunway.runway == 11 || (myRunway.runway == 36 && leg == "final") || (myRunway.runway == 36 && leg == "upwind"))){
                    if (diff < 0){
                       diff = diff * -1;
                   }
                } 

            double oppWind = WindDirection - 180;
            //System.out.println(actual + " - " + expected + " =  " + diff + ".");
            //System.out.println("C : ("+ myplane.x + "," + myplane.y + ")");

            double prevX = myplane.velocity * Math.cos(((expected - 180) * Math.PI) / 180) + WindMagnitude * Math.cos(((WindDirection - 180) * Math.PI) / 180) + myplane.x;
            double prevY = myplane.velocity * Math.sin(((expected - 180) * Math.PI) / 180) + WindMagnitude * Math.sin(((WindDirection - 180) * Math.PI) / 180) + myplane.y;

            //System.out.println("P : ("+ prevX + "," + prevY + ")");

            double ratio1 = 1.5;
            double ratio2 = 0.5;

            if ((myRunway.runway == 18 || myRunway.runway == 29 || myRunway.runway == 11 || (myRunway.runway == 36 && leg == "final") || (myRunway.runway == 36 && leg == "upwind"))){
                ratio1 = 0.55;
                ratio2 = 0.35;
            }

            if (diff > 5.0){
                myplane.x = ratio1 * (WindMagnitude * Math.cos((oppWind * Math.PI) / 180)) + myplane.x;
                myplane.y = ratio1 * (WindMagnitude * Math.sin((oppWind * Math.PI) / 180)) + myplane.y;
            }
        
            if (diff > 3.0 && diff < 5.0){
                myplane.x = ratio2 *(WindMagnitude * Math.cos((oppWind * Math.PI) / 180)) + myplane.x ;
                myplane.y = ratio2 *(WindMagnitude * Math.sin((oppWind * Math.PI) / 180)) + myplane.y;
            }
        
            if (diff < -5.0){
                myplane.x = ratio1 * (WindMagnitude * Math.cos((WindDirection * Math.PI) / 180)) + myplane.x;
                myplane.y = ratio1 * (WindMagnitude * Math.sin((WindDirection * Math.PI) / 180)) + myplane.y;
                //System.out.println("AWC:(" + myplane.x + "," + myplane.y + ")");
            }
        
            if (diff < -3.0 && diff > -5.0){
                myplane.x = ratio2 * (WindMagnitude * Math.cos((WindDirection * Math.PI) / 180)) + myplane.x;
                myplane.y = ratio2 * (WindMagnitude * Math.sin((WindDirection * Math.PI) / 180)) + myplane.y;
            }

            double deltaX = prevX - myplane.x;
            double deltaY = prevY - myplane.y;
            double rad = Math.atan(deltaY/deltaX);  //Magnitude of Heading in Radians
            double deg = ((rad * 180) / Math.PI); //+ 180; //Convert to Degrees

            if (myRunway.runway == 36){    
                deg = deg + 360;
            }
            /* if (myRunway.runway == 18){
                deg = deg;
            } */

            //System.out.println(deltaY + "/" + deltaX + "=" + rad + " DEG --> " + deg);
            //System.out.println("AWC:(" + myplane.x + "," + myplane.y + ")");
            //System.out.println("HEADING AWC: " + deg);
            myplane.heading = deg;
        }

          

    public static double accel(double startx, double endx, double starty, double endy, double startv, double endv)
    {
        double d = Math.sqrt(( 
             Math.pow(endx - startx,2) + Math.pow(endy - starty, 2))+ Math.pow(endv - startv, 2));

        double a = ( (Math.pow(endv,2) - Math.pow(startv, 2)) / (2*d) );
        return a;
    }

    public static void collide(plane planeArray[], int planeNum, runway myrunway, Queue legqueue)
    {
        double rate = rateOfCollision(planeArray, planeNum);
        double d1 = 10000; double d2 = 100000;

        double d = Math.sqrt( Math.pow(planeArray[0].x - planeArray[planeNum].x,2)
        + Math.pow(planeArray[0].y - planeArray[planeNum].y, 2)
        + Math.pow(planeArray[0].z - planeArray[planeNum].z, 2));

        if(planeArray[1].x != myrunway.start45X)    //keep plane from ascending at start
        {
             d1 = Math.sqrt( Math.pow(planeArray[0].x - planeArray[1].x,2)
        + Math.pow(planeArray[0].y - planeArray[1].y, 2)
        + Math.pow(planeArray[0].z - planeArray[1].z, 2));
        }
        if(planeArray[2].x != myrunway.start45X)
        {
            d2 = Math.sqrt( Math.pow(planeArray[0].x - planeArray[2].x,2)
            + Math.pow(planeArray[0].y - planeArray[2].y, 2)
            + Math.pow(planeArray[0].z - planeArray[2].z, 2));
        }
        
        if(rate >= .9 && rate < 1 && planeArray[0].collisionMethod == "none") //slow to stall speed based on other plane position
        {
            if(planeArray[0].velocity > planeArray[0].Vs)
            {
                double v = accel(planeArray[0].x, planeArray[planeNum].prevX, planeArray[0].y, planeArray[planeNum].prevY, planeArray[0].velocity, planeArray[planeNum].velocity);
                planeArray[0].velocity += v;
                System.out.println("collision correction: " + v);
            }
            else
                System.out.println("stall speed");
        }
        if((planeArray[0].collisionMethod == "none" && d < 1700 && d > 500 && planeArray[0].z > 200) )
        {
            System.out.println("got here with plane: " + planeNum);
            System.out.println("360");
            planeArray[0].collisionMethod = "start360";
        }
        if((d1 < 500 || d2 < 500))
        {
            System.out.println("got here");
            System.out.println("d1: " + d1 + " d2 " + d2);
            planeArray[0].collisionMethod = "ascend";
            planeArray[0].velocity += accel(planeArray[0].x, planeArray[planeNum].x, planeArray[0].y, planeArray[planeNum].y, planeArray[0].velocity, planeArray[0].Vx);
        }
        if(d1 >= 500 && d2>= 500 && planeArray[0].collisionMethod =="ascend")
        {
                planeArray[0].collisionMethod = "none";
                if(passed(planeArray[0], myrunway, legqueue))
                {
                    System.out.println("passed!!");
                    legqueue.dequeue();
                }   
                System.out.println("end ascend");
        }
        System.out.println("collision method: " + planeArray[0].collisionMethod);
    }

    public static double rateOfCollision(plane planeArray[], int planeNum)
    {
        double rate = 0.0;

        double distance = Math.sqrt( Math.pow(planeArray[0].x - planeArray[planeNum].x,2)
             + Math.pow(planeArray[0].y - planeArray[planeNum].y, 2)
             + Math.pow(planeArray[0].z - planeArray[planeNum].z, 2));

        double prevDistance = Math.sqrt( Math.pow(planeArray[0].prevX - planeArray[planeNum].prevX,2)
             + Math.pow(planeArray[0].prevY - planeArray[planeNum].prevY, 2)
             + Math.pow(planeArray[0].prevZ - planeArray[planeNum].prevZ, 2));

        rate = distance/prevDistance;
        System.out.println("distance " + planeNum + ": " + distance);
        System.out.println("rate of closure: " + rate);

        return rate;
    }
    
}
    
