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
    public double prevX; double prevY; double prevZ;
    public double heading;

    //all airspeed is calibrated airspeed, not indicated

    public plane (String planeName, double x, double y, double velocity, double Vso, double Vs, double Vfe, double heading)
    {
        this.planeName = planeName;
        this.x = x;
        this.y = y;
        z = 1000;
        this.velocity = velocity;
        prevX = x; prevY = y; prevZ = z;
        this.Vso = Vso; 
        this.Vfe = Vfe;
        this.heading = heading;
    }
}
