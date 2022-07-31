# UAV Collision Avoidance Tactics in an Uncontrolled Airport Traffic Pattern


![](UAV-Collision-Avoidance-Tactics-in-an-Uncontrolled-Airport-Traffic-Pattern-/images/Final_side_view.png)


## Problem Description:
  The goal of this project is to create a 3D simulation of a UAV landing at an uncontrolled (non-towered) airport. Four (4) Traffic paths are created based on the two (2) runways at Auburn University Regional Airport, and a 3D flight simulation is conducted based on Cessna 172 flight performance. The UAV flies the correct path given a runway, avoids collision with other aircraft, and adjusts for wind displacement in-flight.



## Program Specification and Outline of Classes 
In this project, a "plane" is constructed with an initial position and velocity, given a runway, and set up to fly the coded path.

#### Classes used:
* Node.java
* Queue.java
* plane.java
* runway.java
* QueueException
* airport.java

### Node.java
This node class forms the building blocks of the Queue class

Constructs nodes and forms a linked list that will be used to create a queue

### Queue.java
This is a First In, First Out (FIFO) algorithm that is made from a circular linked list using the Node class

* isEmpty() determines if last node is null (if queue is empty)
* DequeueAll()  makes last node null
* Enqueue() adds a node and makes the linked list circular
* Dequeue() removes last item in queue, 3 cases: if queue is empty return error message, if there is one item in queue, and if there is more than one item in queue
* Peek() and front() method to look at/get front item
* clone() method to copy queue, throw clonenotsupportedexception

### plane.java
This is a class to store attributes of each aircraft in the pattern.

A plane is constructed with a name, x position, y position, and initial velocity.
Assuming the plane starts on the 45, height is initialized to 1000 ft.

Stall speed and the max speed of normal operation is pre-defined in this class according to the Cessna 150.

### runway.java
This is a class to store calculations for each essential point in the landing pattern.

An x and y variable is declared for every "turning point" in the pattern (wherever an aircraft would make a significant adjustment to the flight path, ie making a 90 degree turn to another leg or reaching the landing point)

* A runway is constructed with a heading, length, and X and Y positions for both ends of the runway.
* The runway is extended 3500 feet on each side to find the start of the crosswind and final legs
* Two lines extending 3500 feet perpendicular to the ends of the runway form the start of the base and downind
* The point 3500 feet perpendicular to the midpoint of the runway is the end of the 45 leg
* The downwind line (parallel to the runway) rotated 45 degrees counterclockwise and shortened to be 5280 feet is the start of the 45
* The point on the downwind leg directly across from end of runway is the point "abeam" the numbers
* The point on the runway 500 feet from the end is the touchdown point

Two methods are added in this class to compute a new x and y position given a start point, end point, and distance along a vector. These methods essentially use vector calculus to calculate a new point a certain length along a vector.


## Algorithm Design

What follows is an outline of the algorithm approach to the airport client file:

* a Queue is constructed to store legs of flight
* an array of plane classes to store each plane
* global clock int
* for each clock pulse, update position of each plane
* if time to turn a leg, dequeue the Leg Queue and update position according to new leg
* position data is output to a file containing the runway name

#### makeQueue()
 A method to construct a queue of each leg in pattern
 
#### makeRunway()
  A method to construct an object of the runway class
 - The user enters the heading of the runway in use, if invalid runway, error message is thrown
 - A switch statement with cases of each runway heading initialized the characteristcs of the runway object
 
#### makePlane()
A method to construct plane object(s) and add to the plane aray.

If more planes are to be added to the program, they would be constructed in this method.

#### run()
A method to increase plane positions with each clock pulse and open the file that will contain the output data
- a FileWriter and BufferedWriter object is used to output the file heading, formatted into columns with String.format
- the legQueue is constructed along with a clone for every other plane in the plane array
- While the clock is less than 40 (number may be changed to edit length of simulation), each plane's position is updated.
- the second plane's position is updated after 10 clock pules (may be changed to edit when plane 2 begins)

#### changeLeg()
A method to switch legs in the pattern once plane has reached correct position

if Queue is Empty
  - make a new Queue
  - dequeue from Queue to remove 45
  - increase Position

else if passed the (runway leg) End point
  - calculate distance from end point to current position
  - this distance shows how far from path the plane is
  - find new point along next leg given distance overshot
  - dequeue to move onto next leg
  - increase Position
  - adjust for wind

"""

else
  - increase position if not done with any leg

#### passed()
A boolean method to determine if the turning point on a leg has been passed
  - Called in changeLeg method during if statements
  - returns true if turning point has been passed
  - checks each direction based on runway in use

if ( leg ) // check each turn point of each leg

    switch ( runway heading )
    
          case 29:
          case 36:
                if passed condition
                    passed = true
          case 11:
          case 18:
              if passed condition
                  passed = true

return passed

#### increasePos()
A method to update plane position beased on current leg and write out position to file
- construct FileWriter and BufferedWriter objects
- construct DecimalFormat object to format output
- format output string into columns using String.format

if queue is empty
    return
    
Switch( current leg )
    case leg:
        write out position to file
        call newPoint method to update position
        Accelleration is added to velocity considering current velocity, distance to end leg, and end leg velocity
        current position, end of leg position, distance to go = new velocity
        return
        
repeat for each leg

#### newPoint()
A method to determine a new point a certain distance along a vector
1. Vector = End position - Start position
2. Calculate the norm
3. New Position = distance to go * (vector / norm ) + start position

Compare new position to start position to calculate unit heading vector

4. Heading Vector = New Position - Start position
5. Calculate the norm
6. Unit Vector Heading = Heading Vector / norm

Calculate Unit Vector for Wind

7. X: Wind speed * cos (Wind Direction) + start position
8. Y: Wind speed * sin (Wind Direction) + start position
9. Calculate norm
10. Unit Vector = Vector / Norm

New Position = (Unit Heading Vector * Velocity) + (Unit Wind Vector * Wind Speed) + Start Position


#### accel()
A method to calculate acceleration needed based on position, end of vector, and velocity desired at end of vector
- distance formula used to calculate distacne to end of vector
- velocity equation: final velocity squared = initial velocity squared + 2 * acceleration * distance
- given intial velocity and distance to end of vector, return acceleration

## Wind Correction

1. Calculate the difference between actual heading and expected heading

2. Previous point is calculated
    - Opposite Wind Direction
    - Opposite Heading Direction

3. Perform Wind Correction
    - Apply a force opposite the wind
    - The greater the difference, the greater the ratio
    - 3 ratios
    - Range of values

4. Calculate the new heading using the Wind-Corrected-Point and the Previous point

5. New Heading is stored

## Collision Avoidance

The goal is not to get in front of obstacle, but to stay a safe distance behind. Three different collision avoidance tactics 
- Accelerate
- Make a 360 degree turn
- Ascend to avoid immediate collision

Two other planes are in the pattern: one slower in front and one faster behind
Plane class string: “collision method”
Before new point, check collision method

#### 1. Accelerate 
First, the program measures the rate at which the UAV is overtaking or being overtaken by another plane. This is done by comparing the current distance between each plane and the previou distance between each plane. The rate is calculated by the current distance / previous distance. If this rate is less than 1 but greater than .9, the UAV will accelerate accordingly. This acceleration is calculated using the accel() method detailed in section 3.4. The current point is the UAV’s position, and the end point is the other plane’s previous coordinates and previous velocity. Thus, the UAV slows down for the slower aircraft in front and speeds up for the aircraft behind, as well as still hitting its target velocities at each essential point in the pattern. While this method is effective for keeping distance between aircraft as long as possible, it is not a conclusive solution for collision avoidance.

#### 2. Make a 360 Degree Turn 
This maneuver was chosen for collision avoidance because it is a typical maneuver performed in a traffic pattern to buy more time for slower aircraft. Not only does this maneuver create space for aircraft ahead of the UAV, but also allows fast aircraft behind to safely pass the UAV without a collision. This maneuver is performed if the distance between the UAV and any plane falls between 700 and 1700 feet. This maneuever will not occur within 200 feet of the ground, however, as performing a 360 that low would certainly be dangerous. The maneuver is broken into two parts: ”start360” and ”360” to prevent the UAV from spiraling continuously. ”Start360” establishes the center of the circle as a counterclockwise rotation 270 degrees in the < y, −x > direction and scales the radius to 500, making the circle have a diameter of 1000 feet. This direction was chosen to keep the circle on the outer edge of the pattern. The angle a indicates the direction in which the arc leaves the current point, and is calculated by calling the atan2() method from the Math class on the (x, y) coordinate found by subtracting the center coordinates from the current x and y position. Each clock pulse, the UAV velocity/radius is subtracted from a, ensuring an arc length of the velocity traveled in one clock pulse. Once the angle is less than 2pi, the collision method string is set to ”none” and the UAV breaks out of the 360.
#### 3. Ascend 
This maneuver is reserved for when the distance between the UAV and another aircraft falls below 700 feet. This means that an immediate collision is likely, so the aircraft ascends to avoid the other aircraft. When ascending. a plane pitches to Vx, its best angle of climb speed, which gives it the greatest altitude gain for a certain distance.

         

## Installation Process of GNUPLOT 
To visualize the path of the UAV in the airport runways, the 3D graphical program, gnuplot, was used. This program allows for 2D and 3D graph rendering of functions and data points. The following will provide a step-by-step process to download the program onto a MacOS system.

### Step-by-Step Installation:

  1. Open the terminal to install Homebrew
 
  2. Copy and paste the following in the command line: 
> /bin/bash-c"$(curl-fsSLhttps://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
        
To check if Homebrew is already installed, type this command in the terminal. This will give the version of Homebrew that is installed on your Mac.
> brew --version
  
  3. Next is to install gnuplot. Type the following command:
> brew install gnuplot
        
  4. To see if gnuplot is installed, type the following command in the terminal. This will display a list of the installed items on your Mac. Here, you should see gnuplot as one of the installed items.
> brew list
  
  5. Now that gnuplot is installed, run it by typing this in the terminal:
> gnuplot

The picture below shows GNUPLOT ran in the terminal

<img width="444" alt="gnuPlotSucessfulInstallation" src="https://user-images.githubusercontent.com/83298742/173807641-8dd7c4dc-27b8-4ef0-8b57-5f66d8e17ecb.png">

  
For a complete guide on how to install Homebrew or for other installation options, visit https://brew.sh

For GnuPlot documentation, visit http://www.gnuplot.info/docs_5.4/Gnuplot_5_4.pdf


## Bios
Seiler Rivers is a sophomore computer science student at Mercer University. As one of ten students to earn AOPA’s national scholarship to obtain her private pilot’s license, her love of aviation and computer science led her to research UAVs. She also is double majoring in music and is a self-proclaimed pianist, programmer, and pilot. Upon graduating, Seiler hopes to earn her master’s in Computer Science and eventually work as a software engineer.


Andrea Coppi is from Fort Worth, Texas, and attends the University of Dallas. She is a rising junior computer science and business double major. After high school, she attended a nearby community college, Tarrant County College, and competed in the Jim Bolen Math Competiton. As a result, received a full tuition scholarship to the private university. She is part of the university’s soccer team and programming team. After graduating, she plans to further her education and attain a master’s degree in cybersecurity.


