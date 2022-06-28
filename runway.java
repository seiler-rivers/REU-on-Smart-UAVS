class runway {

    public int runway;
    public double length;
    public double y;
    public double landX;
    public double landY;
    public double takeOffX;
    public double takeOffY;
    public int WindMagnitude;
    public int WindDirection;

    public double downwindX;
    public double downwindY;
    public double baseX;
    public double baseY;
    public double crosswindX;
    public double crosswindY;
    public double finalX;
    public double finalY;
    public double end45X;
    public double end45Y;
    public double start45X;
    public double start45Y;
    public double midPtX;
    public double midPtY;
    public double abeamX;
    public double abeamY;
    public double touchDownX;
    public double touchDownY;


    public runway (int runway, double length, double takeOffX, double takeOffY, double landX, double landY, int WindMagnitude, int WindDirection) {
        this.runway = runway;
        this.length = length;
        this.landX = landX;
        this.landY = landY;
        this.takeOffX = takeOffX;
        this.takeOffY = takeOffY;
        this.WindDirection = WindDirection;
        this.WindMagnitude = WindMagnitude;

        //coordinate of start of crosswind
        crosswindX = newX(takeOffX, takeOffY, landX, landY, -3500);
        crosswindY = newY(takeOffX, takeOffY, landX, landY, -3500);

        //coordinate of start of final
        finalX = newX(takeOffX, takeOffY, landX, landY, length + 3500);
        finalY = newY(takeOffX, takeOffY, landX, landY, length + 3500);

        //coordinate of start of downwind, exactly 3500 ft from crosswind point
        downwindX = crosswindX + ((3500/ (length + 7000) )*(finalY - crosswindY));
        downwindY = crosswindY + ((3500/ (length + 7000) )*(crosswindX - finalX));

        //coordinate of start of base, exactly 3500 ft from final point
        baseX = finalX + ((3500/ (length + 7000) )*(finalY - crosswindY));
        baseY = finalY + ((3500/ (length + 7000) )*(crosswindX - finalX));

        //midpoint on runway
        midPtX = newX(takeOffX, takeOffY, landX, landY, length/2);
        midPtY = newY(takeOffX, takeOffY, landX, landY, length/2);

        //point where 45 connects with runway, 3500 ft from midpoint
        end45X = midPtX + ((3500/ (length/2) )*(landY - midPtY));
        end45Y = midPtY + ((3500/ (length/2) )*(midPtX - landX));

        //45 vector
        double x = ((( (Math.sqrt(2))/2 ) * (downwindX - end45X)) - (( (Math.sqrt(2))/2 ) * (downwindY - end45Y))) + end45X;
        double y = ((( (Math.sqrt(2))/2 ) * (downwindX - end45X)) + (( (Math.sqrt(2))/2 ) * (downwindY - end45Y))) + end45Y;

        //start of 45, one mile out from end of 45 along the 45 vector
        start45X = newX(end45X, end45Y, x, y, 5280);
        start45Y = newY(end45X, end45Y, x, y, 5280);

        //point on downwind abeam end of runway, start descent
        abeamX = landX + ((3500/ length)*(landY - takeOffY));
        abeamY = landY + ((3500/length) *(takeOffX - landX));

        //point of touchdown on runway, 500 feet from end of runway
        touchDownX = newX(landX, landY, takeOffX, takeOffY, 500);
        touchDownY = newY(landX, landY, takeOffX, takeOffY, 500);
    }


    public static double newX(double startX, double startY, double endX, double endY, double newDistance)
    {//method to find an x value some distance along a vector

        double Vx = endX - startX;
        double Vy = endY - startY;
        double norm = Math.sqrt( Math.pow(Vx, 2) + Math.pow(Vy, 2) );
        
        double newX = (newDistance * (Vx / norm)) + startX;
        return newX; 
    }

    public static double newY(double startX, double startY, double endX, double endY, double newDistance )
    {   //method to find a y value some distance along a vector
        double Vx = endX - startX;
        double Vy = endY - startY;
        double norm = Math.sqrt( Math.pow(Vx, 2) + Math.pow(Vy, 2) );
        
        double newY = ( newDistance * (Vy / norm) ) + startY;
        return newY;   
    }
}
