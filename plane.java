class plane
{
    public String planeName;
    public double x;
    public double y;
    public double z;
    public double velocity;
    public double accel;
    public double Dtraveled;
    public double Vno;
    public double Vso;
    public double Vfe;
    public double Vno2;
    public double Vso2;
    public double Vfe2;
    public double stall;
    public double heading;

    // planeArray[0] = new plane("UAV", x, y, Vno, Vso, Vfe);

    public plane (String planeName, double x, double y, double Vno, double Vso, double Vfe, double velocity, double heading) //double oldX, double oldY)
    {
        this.planeName = planeName;
        this.x = x;
        this.y = y;
        this.Vno = Vno;
        this.Vso = Vso;
        this.Vfe = Vfe;
        this.velocity = velocity;
        this.heading = heading;
        z = 1000;
        Dtraveled = velocity + accel;
    }

    public String toString()
    {
    return (planeName + "\t \t" +
            x + "\t" +
            x + "\t" +
            y + "\t" +
            velocity); //+ "\t" +
            //turnTime);
    }
}
