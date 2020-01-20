package org.ctdworld.propath.helper;

import java.util.Random;

public class OtpHelper
{
    private static String otp;
    Random random = new Random();

    public void createOtp()
    {
        int intOtp = random.nextInt(999999);
        otp = String.valueOf(intOtp);
    }

    public String getOtp()
    {
        return otp;
    }
}
