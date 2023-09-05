@Grab(group='org.apache.commons', module='commons-imaging', version='1.0-alpha3')

import org.apache.commons.imaging.*
import org.apache.commons.imaging.common.*
import org.apache.commons.imaging.formats.*
import org.apache.commons.imaging.formats.jpeg.*
import org.apache.commons.imaging.formats.tiff.constants.*

class Images{
    
    static def extractCoord( path ){
        def metadata = Imaging.getMetadata(path.toFile())
        if( !metadata || !metadata.metaClass.getMetaMethod("getExif") )
            return null

        def latitude = metadata.exif?.GPS?.latitudeAsDegreesNorth
        def longitude = metadata.exif?.GPS?.longitudeAsDegreesEast
        
        [latitude, longitude]
    }

    static double metersTo( a,  b) {
        double lat1 = a[0] as double
        double lng1 = a[1] as double
        double lat2 = b[0] as double
        double lng2 = b[1] as double
        double radioTierra = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double va1 = Math.pow(sindLat, 2) + Math.pow(sindLng, 2) * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double va2 = 2 * Math.atan2(Math.sqrt(va1), Math.sqrt(1 - va1));
        double meters = radioTierra * va2;
        meters;
    }

}