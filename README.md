# ascii-art

# Main Commands

## Silo


### Light Prince
``` bash
$ java -jar release/ascii-art.jar -a silo -i examples/images/littleprince.png -t examples/texts/littleprince.txt --bgc white --fgc black -o examples/lightprince.png 
Beginning mapping. Please be patient.

Image printed to: examples/lightprince.png
```

### Dark Prince
``` bash
$ java -jar release/ascii-art.jar -a silo -i examples/images/littleprince.png -t examples/texts/littleprince.txt --bgc black --fgc white -o examples/darkprince.png 
Beginning mapping. Please be patient.

Image printed to: examples/darkprince.png
```


## PixelSwap
``` bash
$ java -jar release/ascii-art.jar -a pixelswap -i examples/images/building.jpg -o examples/building.png 
Image printed to: examples/building.png
```

## Console

``` bash
java -jar release/ascii-art.jar -a console -i examples/images/building.jpg --dx 142 --dy 44
```

# Helpers

## Dim
``` bash
$ java -jar release/ascii-art.jar -a dim -i examples/images/building.jpg 
dx: 283	dy: 87
```


## Sample

``` bash
java -jar release/ascii-art.jar -a sample -i examples/images/littleprince.png -m black --invert
```

## Fonts
``` bash
java -jar release/ascii-art.jar -a FONTS
```
