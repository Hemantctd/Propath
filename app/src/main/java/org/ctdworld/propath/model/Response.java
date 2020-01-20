package org.ctdworld.propath.model;

import org.ctdworld.propath.helper.ConstHelper;

public class Response
{
    // response keys
    public static final String KEY_SUCCESS_STATUS = "success";

    // response values
    public static final String STATUS_SUCCESS = "1";
    public static final String STATUS_FAILED = "0";



    /* # there are two keys which are being used for response status which are "res" and "success"
     * so we are using both keys here, which key contains data it will be checked in getResponseStatus it returns status*/
    private String res;   // # don't remove
    private String success; // # don't remove





    /* # there are two keys which are being used for response status which are "res" and "success"
    * so this method is to get response status by check both keys */
    public String getResponseStatus(Response response)
    {
        if (response != null)
        {
            if (response.getSuccess() != null)
                return response.getSuccess();
            else
                return response.getRes();
        }
        return ConstHelper.NOT_FOUND_STRING;
    }




    private String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    private String getSuccess()
    {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
