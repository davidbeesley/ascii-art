# ascii-art
A set of tools for creating ascii art from existing images and text files.

The main algorithms are 
- silo: The flagship feature. Map a text file to a silhouette.
- console: Print an ANSI colored ascii representation of an image to the console.
- pixelswap: Create a true colored ascii pixelated version of an image.

## Examples

### Silo
![](examples/lightprince.png?raw=true
![](examples/darkprince.png?raw=true

### PixelSwap
![](examples/building.png?raw=true)

### Console
![](examples/console.png?raw=true)

## Usage
``` 
$ java -jar release/ascii-art.jar -h
Usage: AsciiArt [-hV] [--bold] [--invert] [--italic] -a=<alg> [--angle=<ANGLE>]
                [-b=<border>] [--bgc=<COLOR>] [-e=<outputType>] [-f=<FONT>]
                [--fgc=<COLOR>] [-i=<FILE>] [--logging=<logLevel>] [-m=<COLOR>]
                [-o=<FILE>] [-s=<SIZE>] [-t=<FILE>] [-x=<dx>] [-y=<dy>]
Ascii Art generator
  -a, --algorithm=<alg>      Valid values: CONSOLE, FONTS, PIXELSWAP, SAMPLE,
                               SILO, DIM
      --angle=<ANGLE>        matching angle
  -b, --border=<border>      border width
      --bgc=<COLOR>          background color
      --bold                 make font bold
  -e, --ext=<outputType>     Output format. Valid values: JPG, PNG
  -f, --font=<FONT>          font
      --fgc=<COLOR>          foreground color
  -h, --help                 Show this help message and exit.
  -i, --image=<FILE>         source image
      --invert               inverts image (i.e. black to white or character
                               large to small
      --italic               make font italic
      --logging=<logLevel>   Valid values: TRACE, DEBUG, INFO, WARNING, ERROR,
                               NONE
  -m, --matcher=<COLOR>      silhouette color to match
  -o, --out=<FILE>           output filename
  -s, --size=<SIZE>          font size
  -t, --text=<FILE>          source text
  -V, --version              Print version information and exit.
  -x, --dx=<dx>              desired output dimension x
  -y, --dy=<dy>              desired output dimension y

```

## Algorithms
### Silo
Maps text to a silhouette. Does not break works across lines or gaps.
Requires an input image and text file. Outputs an image.


#### Light Prince
``` bash
$ java -jar release/ascii-art.jar -a silo -i examples/images/littleprince.png -t examples/texts/littleprince.txt --bgc white --fgc black -o examples/lightprince.png 
Beginning mapping. Please be patient.

Image printed to: examples/lightprince.png
```

#### Dark Prince
``` bash
$ java -jar release/ascii-art.jar -a silo -i examples/images/littleprince.png -t examples/texts/littleprince.txt --bgc black --fgc white -o examples/darkprince.png 
Beginning mapping. Please be patient.

Image printed to: examples/darkprince.png
```


### PixelSwap
Creates a true colored ascii pixelated version of an image.
Requires an input image. Outputs an image.
``` bash
$ java -jar release/ascii-art.jar -a pixelswap -i examples/images/building.jpg -o examples/building.png 
Image printed to: examples/building.png
```

### Console
Prints an ANSI colored ascii representation of an image to the console.
Requires an input image. Output to the console.

``` bash
java -jar release/ascii-art.jar -a console -i examples/images/building.jpg --dx 142 --dy 44
```

## Helpers

### Dim
Prints the ascii aspect ratio of the original image. 
Requires an input image.
You can also specify a desired font to get the aspect ratio in that font.
``` bash
$ java -jar release/ascii-art.jar -a dim -i examples/images/building.jpg 
dx: 283	dy: 87
```


### Sample
A tool for determine the --angle for silo. The angle determines how likely a color block will match with the matcher color. Prints out map of the image with matched pixels for a variety of angle values.
``` bash
java -jar release/ascii-art.jar -a sample -i examples/images/littleprince.png -m black --invert
```

### Fonts
Prints out the list of fonts found on the system that can be used with the --font option.
``` bash
java -jar release/ascii-art.jar -a FONTS
```
