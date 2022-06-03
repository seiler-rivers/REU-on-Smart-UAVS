class plane
{
    public String planeName;
    public double x;
    public double y;
    public double z;
    public double velocity;
  //  public int accel;

    public plane (String planeName, double x, double y, double velocity)
    {
        this.planeName = planeName;
        this.x = x;
        this.y = y;
        z = 1000;
        this.velocity = velocity;
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