package com.github.davidbeesley.asciiart.util;

import com.github.davidbeesley.asciiart.util.logger.*;
public enum ImageType {
    JPG("jpg"), PNG("png");

    private String extension;

    ImageType(String extension) {
        this.extension = extension;
    }

    @Override
    public String toString() {
        return extension;
    }

    public static ImageType parse(String text) {
        switch (text) {
            case "jpg":
            case "JPG":
                return JPG;
            case "png":
            case "PNG":
                return PNG;
            default:
                Logger.getInstance().warning("Invalid image type: " + text);
                return null;
        }
    }
}
