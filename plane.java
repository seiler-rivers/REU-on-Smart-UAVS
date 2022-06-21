
class plane
{
    public String planeName;
    public double x;
    public double y;
    public double z;
    public double velocity;
    public double accel;
    public double Dtraveled;
    public double Vno;  //normal operating range
    public double Vso;  //power off stall, landing configuration
    public double Vfe;  //max flaps extended speed

    //all airspeed is calibrated airspeed, not indicated

    public plane (String planeName, double x, double y, double Vno, double Vso, Double Vfe)
    {
        this.planeName = planeName;
        this.x = x;
        this.y = y;
        z = 1000;
        this.Vno = Vno;
        velocity = Vno;
        Dtraveled = velocity + accel;
        this.Vso = Vso; 
        this.Vfe = Vfe;
    }

    public String toString()
    {
    return (planeName + "\t \t" +
            x + "\t" +
            y + "\t" +
            z + "\t" +
            velocity); //+ "\t" +
            //turnTime);
    }
}
