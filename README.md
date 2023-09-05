# nextflow-images

Parse a bunch of images and sort them respect an origin (latitude, longitude)

# run

Sort by Equador (0,0):

`nextflow run main.nf --directory "/PATH/TO/IMAGES"`

Sort respect a point:

`nextflow run main.nf --directory "/PATH/TO/IMAGES" --origin "40,-74"`