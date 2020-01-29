package provider.pixel;

import org.junit.Test;

public class BooleanProviderTest {

    @Test
    public void angle() {
        Integer[] a= {0,1};
        Integer[] b= {1,0};

        System.out.println(BooleanPixelProvider.angle(a,b));
        System.out.println(BooleanPixelProvider.angle(a,a));

    }
}