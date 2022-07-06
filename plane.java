class plane
{
    public String planeName;
    public double x;
    public double y;
    public double z;
    public double velocity;
    public double accel;
    public double Vno;  //normal operating range
    public double Vso;  //power off stall, landing configuration
    public double Vs;   //power off stall, clean configuration
    public double Vfe;  //max flaps extended speed
    public double Vx;
    public double climbRate;
    public double prevX; double prevY; double prevZ;
    public double heading;
    public String collisionMethod;
    public double maneuverArray[];  //for storing coordinates of collision avoidance maneuvers
    

    //all airspeed is calibrated airspeed, not indicated

    public plane (String planeName, double x, double y, double velocity, double Vso, double Vs, double Vfe, double Vx, double climbRate, double heading)
    {
        this.planeName = planeName;
        this.x = x;
        this.y = y;
        z = 1000;
        this.velocity = velocity;
        prevX = x; prevY = y; prevZ = z;
        this.Vso = Vso; 
        this.Vfe = Vfe;
        this.Vx = Vx;
        this.climbRate = climbRate;
        this.heading = heading;
        collisionMethod = "none";
        maneuverArray = new double[3];
    }
}
