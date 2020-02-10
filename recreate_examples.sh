#!/bin/bash
#echo "java -jar release/ascii-art.jar -h"
#java -jar release/ascii-art.jar -h

echo "java -jar release/ascii-art.jar -i examples/images/littleprince.png --bgc red --fgc white -m black --invert -t examples/texts/littleprince.txt -o examples/darkprince.png"
java -jar release/ascii-art.jar -i examples/images/littleprince.png --bgc black --fgc white -m black --invert -t examples/texts/littleprince.txt -o examples/darkprince.png

echo "java -jar release/ascii-art.jar -i examples/images/littleprince.png --bgc white --fgc black -m black -t examples/texts/littleprince.txt -o examples/lightprince.png "
java -jar release/ascii-art.jar  -i examples/images/littleprince.png --bgc white --fgc black -m black -t examples/texts/littleprince.txt -o examples/lightprince.png

echo "java -jar release/ascii-art.jar -a sample -i examples/images/littleprince.png -m black --invert"
java -jar release/ascii-art.jar -a sample -i examples/images/littleprince.png -m black --invert

echo "java -jar release/ascii-art.jar -a fonts"
java -jar release/ascii-art.jar -a fonts

echo "java -jar release/ascii-art.jar -a color"
java -jar release/ascii-art.jar -a colors
