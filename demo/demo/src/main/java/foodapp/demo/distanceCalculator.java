package foodapp.demo;

public class distanceCalculator{
    private static final double Earth_Radius=6371;
    public static double distance(double lat1, double lon1, double lat2, double lon2){
        double latdistance=Math.toRadians(lat2-lat1);
        double longdistance=Math.toRadians(lon2-lon1);
        double a=Math.sin(latdistance/2)*Math.sin(latdistance/2)+Math.cos(Math.toRadians(lat1))*Math.cos(Math.toRadians(lat2))*
                Math.sin(longdistance/2)*Math.sin(longdistance/2);
        double c=2*Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
        return Earth_Radius*c;
    }
}
