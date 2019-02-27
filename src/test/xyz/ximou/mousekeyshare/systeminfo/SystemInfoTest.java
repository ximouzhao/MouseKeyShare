package xyz.ximou.mousekeyshare.systeminfo;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SystemInfoTest {

    @Test
    void getSystemType() {
        //Assert.assertEquals(SystemInfo.getSystemType(),SystemType.MAC_OS);
        Assert.assertEquals(SystemInfo.getSystemType(),SystemType.WINDOWS);
    }
}