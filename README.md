# ascii-art

Map a text file uniformly to a silhouette. Does not break works across lines or gaps.
Requires an input image and text file. Outputs an image.




## Examples

#### LightPrince

``` bash
java -jar release/ascii-art.jar -i examples/images/littleprince.png --bgc white --fgc black -m black -t examples/texts/littleprince.txt -o examples/lightprince.png 
```
![](examples/lightprince.png?raw=true)


#### DarkPrince

``` bash
java -jar release/ascii-art.jar -i examples/images/littleprince.png --bgc white --fgc black -m black -t examples/texts/littleprince.txt -o examples/lightprince.png 
```
![](examples/darkprince.png?raw=true)


## Usage
``` 
java -jar release/ascii-art.jar -h
Usage: AsciiArt [-hV] [--bold] [--invert] [--italic] [-a=<alg>]
                [--angle=<ANGLE>] [-b=<border>] [--bgc=<COLOR>]
                [-e=<outputType>] [-f=<FONT>] [--fgc=<COLOR>] [-i=<FILE>]
                [--logging=<logLevel>] [-m=<COLOR>] [-o=<FILE>] [-s=<SIZE>]
                [-t=<FILE>]
Ascii Art generator
  -a, --algorithm=<alg>      Valid values: map, sample, fonts, colors
      --angle=<ANGLE>        Matching angle
  -b, --border=<border>      Border width
      --bgc=<COLOR>          Background color.
      --bold                 Make font bold
  -e, --ext=<outputType>     Output format. Valid values: jpg, png
  -f, --font=<FONT>          Font
      --fgc=<COLOR>          Foreground color.
  -h, --help                 Show this help message and exit.
  -i, --image=<FILE>         Source image
      --invert               Inverts image (silo) or characters matching
                               (console, pixelswap).
      --italic               Make font italic
      --logging=<logLevel>   Valid values: TRACE, DEBUG, INFO, WARNING, ERROR,
                               NONE
  -m, --matcher=<COLOR>      Silhouette color to match.
  -o, --out=<FILE>           Output filename
  -s, --size=<SIZE>          Font size
  -t, --text=<FILE>          Source text
  -V, --version              Print version information and exit.


```



## Helpers



### Sample
A tool for determine the --angle for silo. The angle determines how likely a color block will match with the matcher color. Prints out map of the imageWrapper with matched pixels for a variety of angle values.
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
java -jar release/ascii-art.jar -a colors
```

## Credits
- picocli https://picocli.info/
- webcolor http://www.java2s.com/Code/Java/2D-Graphics-GUI/Webcolorenum.htm
