# ascii-art
A set of tools for creating ascii art from existing images and text files.

The main algorithms are 
- silo: The flagship feature. Map a text file to a silhouette.
- console: Print an ANSI colored ascii representation of an image to the console.
- pixelswap: Create a true colored ascii pixelated version of an image.

## Examples

### Silo
#### LightPrince
![](examples/lightprince.png?raw=true)


#### DarkPrince
![](examples/darkprince.png?raw=true)


### PixelSwap
![](examples/pixelswap.png?raw=true)

### Console
![](examples/console.png?raw=true)

## Usage
``` 
java -jar release/ascii-art.jar -h
Usage: AsciiArt [-hV] [--bold] [--invert] [--italic] -a=<alg> [--angle=<ANGLE>]
                [-b=<border>] [--bgc=<COLOR>] [-e=<outputType>] [-f=<FONT>]
                [--fgc=<COLOR>] [-i=<FILE>] [--logging=<logLevel>] [-m=<COLOR>]
                [-o=<FILE>] [-s=<SIZE>] [-t=<FILE>] [-x=<dx>] [-y=<dy>]
Ascii Art generator
  -a, --algorithm=<alg>      Valid values: CONSOLE, FONTS, PIXELSWAP, SAMPLE,
                               SILO, DIM
      --angle=<ANGLE>        Matching angle
  -b, --border=<border>      Border width
      --bgc=<COLOR>          Background color
      --bold                 Make font bold
  -e, --ext=<outputType>     Output format. Valid values: JPG, PNG
  -f, --font=<FONT>          Font
      --fgc=<COLOR>          Foreground color
  -h, --help                 Show this help message and exit.
  -i, --image=<FILE>         Source image
      --invert               Inverts image (silo) or characters matching
                               (console, pixelswap).
      --italic               Make font italic
      --logging=<logLevel>   Valid values: TRACE, DEBUG, INFO, WARNING, ERROR,
                               NONE
  -m, --matcher=<COLOR>      Silhouette color to match
  -o, --out=<FILE>           Output filename
  -s, --size=<SIZE>          Font size
  -t, --text=<FILE>          Source text
  -V, --version              Print version information and exit.
  -x, --dx=<dx>              Desired output dimension x
  -y, --dy=<dy>              Desired output dimension y

```

## Algorithms
### Silo
Maps text to a silhouette. Does not break works across lines or gaps.
Requires an input image and text file. Outputs an image.


#### Light Prince
``` bash
java -jar release/ascii-art.jar -a silo -i examples/images/littleprince.png -t examples/texts/littleprince.txt --bgc white --fgc black -o examples/lightprince.png 
```

#### Dark Prince
``` bash
java -jar release/ascii-art.jar -a silo -i examples/images/littleprince.png -t examples/texts/littleprince.txt --bgc black --fgc white -o examples/darkprince.png --invert

```


### PixelSwap
Creates a true colored ascii pixelated version of an image.
Requires an input image. Outputs an image.
``` bash
java -jar release/ascii-art.jar -a pixelswap -i examples/images/building.jpg -o examples/pixelswap.png --bgc "DarkGray"
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
java -jar release/ascii-art.jar -a dim -i examples/images/building.jpg 
```


### Sample
A tool for determine the --angle for silo. The angle determines how likely a color block will match with the matcher color. Prints out map of the image with matched pixels for a variety of angle values.
``` bash
java -jar release/ascii-art.jar -a sample -i examples/images/littleprince.png -m black --invert
```

### Fonts
Prints out the list of fonts found on the system that can be used with the --font option.
``` bash
java -jar release/ascii-art.jar -a fonts
```

### Color
Prints out the list of possible colors that can be used with the --bgc --fbg -m arguments.
``` bash
java -jar release/ascii-art.jar -a color
```
