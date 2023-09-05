 
ecuador = [0, 0] 
origin = params.origin ? params.origin.split(",") : ecuador

class Image{
    def file
    def coord
    def order
}

process EXIF_GPS{
    input:
        path fImage
    output:
        val image
    exec:
        coord = Images.extractCoord( file("$params.directory/$fImage") )
        image = new Image(file:fImage, coord: coord)
}

process PROCESS_IMAGE{
    input:
        each img
    output:
        val target    
    exec:
        target = file("$params.directory/$img.file").copyTo(file("$params.outputDir/$img.order-$img.file"))        
}

workflow{
    def images = Channel.fromPath("$params.directory/*.jpg")

    images //read all images        
        | EXIF_GPS // extract gpf information    
        | branch { // diferenciate if exif information or not
            no_info: !it.coord?[0]
            with_info: it.coord
        }
        | set{ gps_images } // send to new channel
            
    gps_images.with_info // only images with gps info    
        | toSortedList{ a, b-> // sort respect how far are from origin 
            Images.metersTo(origin,a.coord) <=> Images.metersTo(origin, b.coord)                                 
        }    
        | map { // assign the index
            it.eachWithIndex{ img, idx-> img.order = idx} 
        }
        | set{ images_sorted } //send to new channel    

    images_sorted
        | PROCESS_IMAGE
        | view

}