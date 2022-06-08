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
    public double stall;

    public plane (String planeName, double x, double y, double velocity)
    {
        this.planeName = planeName;
        this.x = x;
        this.y = y;
        z = 1000;
        this.velocity = velocity;
        Dtraveled = velocity + accel;
        Vno = 10830;
        stall = 4860; // clean, landing config is 4354.5
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
